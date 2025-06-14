package com.correos.delivery.api;

import com.example.routes.Address;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Client for interacting with Google Maps Route Optimization API.
 * <p>
 * The API key must be configured before calling {@link #optimize(List)}. Replace
 * the {@code API_KEY} constant with your own key or load it from configuration.
 * </p>
 */
public class RouteOptimizer {

    private static final String API_KEY = loadApiKey();
    private static final String ENDPOINT = "https://routes.googleapis.com/directions/v2:computeRoutes";

    private final OkHttpClient client;

    public RouteOptimizer() {
        this(new OkHttpClient());
    }

    public RouteOptimizer(OkHttpClient client) {
        this.client = client;
    }

    private static String loadApiKey() {
        String key = System.getProperty("google.maps.apiKey");
        if (key == null || key.isEmpty()) {
            key = System.getenv("GOOGLE_MAPS_API_KEY");
        }
        return key != null ? key : "";
    }

    /**
     * Optimize the order of the provided stops by invoking the Google Maps
     * Route Optimization endpoint.
     *
     * @param stops list of addresses to optimize
     * @return the ordered list of addresses as returned by the service or the
     * input list if an error occurs
     */
    public List<Address> optimize(List<Address> stops) {
        RequestBody body = RequestBody.create(buildRequestBody(stops), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(ENDPOINT + "?key=" + API_KEY)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if (response.isSuccessful() && responseBody != null) {
                String json = responseBody.string();
                return parseOptimizedStops(json, stops);
            }
        } catch (IOException e) {
            // TODO handle exceptions or log as needed
        }
        return stops;
    }

    private String buildRequestBody(List<Address> stops) {
        if (stops.isEmpty()) {
            return "{}";
        }

        JSONObject root = new JSONObject();
        root.put("travelMode", "DRIVE");
        root.put("optimizeWaypointOrder", true);

        root.put("origin", waypoint(stops.get(0)));
        Address destination = stops.get(stops.size() - 1);
        root.put("destination", waypoint(destination));

        if (stops.size() > 2) {
            JSONArray inter = new JSONArray();
            for (int i = 1; i < stops.size() - 1; i++) {
                inter.put(waypoint(stops.get(i)));
            }
            root.put("intermediates", inter);
        }

        return root.toString();
    }

    private List<Address> parseOptimizedStops(String json, List<Address> original) {
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray routes = obj.optJSONArray("routes");
            if (routes == null || routes.length() == 0) {
                return original;
            }

            JSONObject route = routes.getJSONObject(0);
            JSONArray order = route.optJSONArray("optimizedIntermediateWaypointIndex");
            if (order == null) {
                return original;
            }

            List<Address> result = new java.util.ArrayList<>();
            result.add(original.get(0));
            for (int i = 0; i < order.length(); i++) {
                int idx = order.getInt(i);
                result.add(original.get(idx + 1));
            }
            if (original.size() > 1) {
                result.add(original.get(original.size() - 1));
            }
            return result;
        } catch (Exception e) {
            return original;
        }
    }

    private JSONObject waypoint(Address a) {
        JSONObject latLng = new JSONObject();
        latLng.put("latitude", a.getLatitude());
        latLng.put("longitude", a.getLongitude());
        JSONObject location = new JSONObject();
        location.put("latLng", latLng);
        JSONObject waypoint = new JSONObject();
        waypoint.put("location", location);
        return waypoint;
    }
}
