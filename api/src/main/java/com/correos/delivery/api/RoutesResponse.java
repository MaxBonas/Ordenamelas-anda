package com.correos.delivery.api;

import java.util.List;

public class RoutesResponse {
    public List<Route> routes;

    public static class Route {
        public List<Integer> optimizedIntermediateWaypointIndex;
    }
}
