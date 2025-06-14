package com.correos.delivery.api;

import com.correos.delivery.core.model.Address;
import java.util.ArrayList;
import java.util.List;

public class ComputeRoutesRequest {
    public String travelMode = "DRIVE";
    public boolean optimizeWaypointOrder = true;
    public Waypoint origin;
    public Waypoint destination;
    public List<Waypoint> intermediates;

    public static ComputeRoutesRequest fromStops(List<Address> stops) {
        ComputeRoutesRequest req = new ComputeRoutesRequest();
        req.origin = new Waypoint(stops.get(0));
        req.destination = new Waypoint(stops.get(stops.size() - 1));
        if (stops.size() > 2) {
            req.intermediates = new ArrayList<>();
            for (int i = 1; i < stops.size() - 1; i++) {
                req.intermediates.add(new Waypoint(stops.get(i)));
            }
        }
        return req;
    }

    public static class Waypoint {
        public Location location;
        public Waypoint(Address a) {
            this.location = new Location(a);
        }
    }

    public static class Location {
        public LatLng latLng;
        public Location(Address a) {
            this.latLng = new LatLng(a);
        }
    }

    public static class LatLng {
        public double latitude;
        public double longitude;
        public LatLng(Address a) {
            this.latitude = a.getLatitude();
            this.longitude = a.getLongitude();
        }
    }
}
