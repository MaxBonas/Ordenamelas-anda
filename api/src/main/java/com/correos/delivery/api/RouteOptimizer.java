package com.correos.delivery.api;

import com.example.routes.Address;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

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

    /** TODO Replace with the actual Google Maps API key. */
    private static final String API_KEY = "YOUR_GOOGLE_MAPS_KEY";
    private static final String ENDPOINT = "https://routes.googleapis.com/directions/v2:computeRoutes";

    private final OkHttpClient client;

    public RouteOptimizer() {
        this(new OkHttpClient());
    }

    public RouteOptimizer(OkHttpClient client) {
        this.client = client;
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
        // TODO Construct request JSON matching Google Maps Routes API format
        return "{}";
    }

    private List<Address> parseOptimizedStops(String json, List<Address> original) {
        // TODO Parse the response JSON and reorder the list accordingly
        return original;
    }
}
