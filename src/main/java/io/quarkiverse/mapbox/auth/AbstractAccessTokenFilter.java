package io.quarkiverse.mapbox.auth;

import jakarta.ws.rs.core.UriBuilder;
import org.jboss.resteasy.reactive.client.spi.ResteasyReactiveClientRequestContext;
import org.jboss.resteasy.reactive.client.spi.ResteasyReactiveClientRequestFilter;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Injects the Mapbox access token as a query parameter on every outbound request to the Directions API.
 * Registered via {@code quarkus.rest-client.directions.providers} and {@code quarkus.rest-client.geocoding.providers}
 * so it applies only to that client.
 */
public abstract class AbstractAccessTokenFilter implements ResteasyReactiveClientRequestFilter {

    @Override
    public void filter(final ResteasyReactiveClientRequestContext requestContext) {

        final URI reqUri = requestContext.getUri();
        final URI resUri = mergeAndRebuildUri(reqUri);

        requestContext.setUri(resUri);
    }

    /**
     * Provide access token from your code.
     *
     * @return MapBox access token
     */
    protected abstract String getAccessToken();

    private URI mergeAndRebuildUri(URI originalUri) {

        final  String query = originalUri.getRawQuery();

        if (query == null || query.isEmpty()) {

            return originalUri;
        }

        final Map<String, String> mergedParams = Arrays.stream(query.split("&"))
                .map(param -> param.split("=", 2))
                .collect(Collectors.groupingBy(
                        pair -> decode(pair[0]),
                        LinkedHashMap::new,
                        Collectors.mapping(
                                pair -> pair.length > 1 ? decode(pair[1]) : "",
                                Collectors.joining(",")
                        )
                ));

        final  UriBuilder builder = UriBuilder.fromUri(originalUri);

        builder.replaceQuery(null);
        mergedParams.forEach(builder::queryParam);
        builder.queryParam("access_token", getAccessToken());

        final String path = originalUri.getPath().replace("%252C", ",").replace("%253B", ";");

        return builder
                .replacePath(path)
                .build();
    }

    private String decode(String value) {

        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
