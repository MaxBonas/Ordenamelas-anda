package com.correos.delivery.api;

import com.correos.delivery.core.model.Address;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Client for interacting with Google Maps Route Optimization API using Retrofit.
 */
public class RouteOptimizer {

    private static final String BASE_URL = "https://routes.googleapis.com/directions/";

    private final RoutesService service;
    private final String apiKey;

    /**
     * Create an optimizer with a provided {@link RoutesService} and API key.
     */
    public RouteOptimizer(RoutesService service, String apiKey) {
        this.service = service;
        this.apiKey = apiKey;
    }

    /**
     * Convenience constructor that builds a default Retrofit service with 30s timeout.
     */
    public RouteOptimizer(String apiKey) {
        this(defaultService(), apiKey);
    }

    private static RoutesService defaultService() {
        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(30, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(RoutesService.class);
    }

    /**
     * Optimize the provided list of stops. May issue multiple requests if the
     * amount of stops exceeds the API limit (25 per request).
     *
     * @param stops ordered list of stops including origin and destination
     * @return optimized list of stops
     * @throws RouteOptimizationException if the request fails or response is invalid
     */
    public List<Address> optimize(List<Address> stops) throws RouteOptimizationException {
        if (stops == null || stops.size() <= 1) {
            return stops;
        }

        if (stops.size() <= 25) {
            return optimizeChunk(stops);
        }

        List<Address> result = new ArrayList<>();
        // process first chunk normally
        List<Address> first = optimizeChunk(stops.subList(0, 25));
        result.addAll(first);
        int index = 24; // last element of first chunk is also start of next
        while (index < stops.size() - 1) {
            int end = Math.min(index + 24, stops.size() - 1);
            List<Address> sub = stops.subList(index, end + 1);
            List<Address> part = optimizeChunk(sub);
            result.addAll(part.subList(1, part.size())); // skip duplicated origin
            index = end;
        }
        return result;
    }

    private List<Address> optimizeChunk(List<Address> chunk) throws RouteOptimizationException {
        ComputeRoutesRequest request = ComputeRoutesRequest.fromStops(chunk);
        Call<RoutesResponse> call = service.computeRoutes(apiKey, request);
        try {
            Response<RoutesResponse> response = call.execute();
            if (!response.isSuccessful() || response.body() == null ||
                    response.body().routes == null || response.body().routes.isEmpty()) {
                throw new RouteOptimizationException("Invalid response from service");
            }
            RoutesResponse.Route route = response.body().routes.get(0);
            List<Address> ordered = new ArrayList<>();
            ordered.add(chunk.get(0));
            if (route.optimizedIntermediateWaypointIndex != null) {
                for (Integer idx : route.optimizedIntermediateWaypointIndex) {
                    ordered.add(chunk.get(idx + 1));
                }
            } else {
                for (int i = 1; i < chunk.size() - 1; i++) {
                    ordered.add(chunk.get(i));
                }
            }
            ordered.add(chunk.get(chunk.size() - 1));
            return ordered;
        } catch (IOException e) {
            throw new RouteOptimizationException("Failed to call optimization API", e);
        }
    }
}
