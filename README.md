# API client for MapBox
## Directions API
[MapBox Directions API](https://docs.mapbox.com/api/navigation/directions/) is a service that provides route planning and navigation capabilities. It allows developers to calculate routes between locations, get turn-by-turn directions, and optimize routes for various modes of transportation such as driving, walking, and cycling.
## Geocoding API
[MapBox Geocoding API](https://docs.mapbox.com/api/search/geocoding/) is a service that allows developers to convert addresses into geographic coordinates (latitude and longitude) and vice versa. It provides forward geocoding (address to coordinates) and reverse geocoding (coordinates to address) capabilities, making it easier to integrate location-based features into applications.
# Usage
## Filter
### application.properties
```
# ---------------------     Mapbox
quarkus.rest-client.directions.providers=org.otomotive.tbturn.filter.MapBoxAccessTokenClientRequestFilter
quarkus.rest-client.geocoding.providers=org.otomotive.tbturn.filter.MapBoxAccessTokenClientRequestFilter
```
### Java
```java
import io.quarkiverse.mapbox.auth.AbstractAccessTokenFilter;

/**
 * Injects the Mapbox access token as a query parameter.
 */
public class MapBoxAccessTokenClientRequestFilter extends AbstractAccessTokenFilter {

    @Override
    protected String getAccessToken() {

        return ""; // TODO: inject your access token
    }
}
```