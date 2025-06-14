package com.correos.delivery.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RoutesService {
    @POST("v2:computeRoutes?key={apiKey}")
    Call<RoutesResponse> computeRoutes(
            @Path("apiKey") String apiKey,
            @Body ComputeRoutesRequest request
    );
}
