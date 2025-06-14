package com.correos.delivery.api;

import com.correos.delivery.core.model.Address;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import retrofit2.Call;
import retrofit2.Response;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RouteOptimizerTest {

    private RoutesService service;
    private RouteOptimizer optimizer;

    @Before
    public void setUp() {
        service = Mockito.mock(RoutesService.class);
        optimizer = new RouteOptimizer(service, "key");
    }

    @Test
    public void optimizeReturnsOrderedStops() throws Exception {
        List<Address> stops = Arrays.asList(
                new Address("1","A","1",0.0,0.0),
                new Address("2","B","2",1.0,1.0),
                new Address("3","C","3",2.0,2.0),
                new Address("4","D","4",3.0,3.0)
        );
        RoutesResponse responseObj = new RoutesResponse();
        RoutesResponse.Route route = new RoutesResponse.Route();
        route.optimizedIntermediateWaypointIndex = Arrays.asList(1,0);
        responseObj.routes = Collections.singletonList(route);

        Call<RoutesResponse> call = mock(Call.class);
        when(service.computeRoutes(any(), any())).thenReturn(call);
        when(call.execute()).thenReturn(Response.success(responseObj));

        List<Address> result = optimizer.optimize(stops);

        List<Address> expected = Arrays.asList(stops.get(0), stops.get(2), stops.get(1), stops.get(3));
        assertEquals(expected, result);
    }

    @Test
    public void optimizeSplitsLargeLists() throws Exception {
        // create 26 stops
        List<Address> stops = new java.util.ArrayList<>();
        for (int i = 0; i < 26; i++) {
            stops.add(new Address(String.valueOf(i), "S"+i, "1", i, i));
        }
        RoutesResponse responseObj = new RoutesResponse();
        RoutesResponse.Route route = new RoutesResponse.Route();
        // identity order
        route.optimizedIntermediateWaypointIndex = new java.util.ArrayList<>();
        for (int i = 0; i < 23; i++) {
            route.optimizedIntermediateWaypointIndex.add(i);
        }
        responseObj.routes = Collections.singletonList(route);

        Call<RoutesResponse> call = mock(Call.class);
        when(service.computeRoutes(any(), any())).thenReturn(call);
        when(call.execute()).thenReturn(Response.success(responseObj));

        List<Address> result = optimizer.optimize(stops);

        assertEquals(26, result.size());
        verify(service, times(2)).computeRoutes(any(), any());
    }
}
