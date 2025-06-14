package com.example.routes

import com.correos.delivery.export.GpxExporter
import com.correos.delivery.export.KmlExporter
import java.io.File
import java.io.IOException

/**
 * Represents a route consisting of an origin, destination and optional
 * intermediate waypoints.
 */
data class Route(
    val origin: Address,
    val destination: Address,
    val waypoints: List<Address> = emptyList()
) {

    /**
     * Convenience method returning all stops in order starting from the origin
     * and ending at the destination.
     */
    fun allStops(): List<Address> = listOf(origin) + waypoints + destination

    /**
     * Export this route to a GPX file using [GpxExporter].
     *
     * @throws IOException if the file cannot be written
     */
    @Throws(IOException::class)
    fun toGpx(): File = GpxExporter().export(allStops())

    /**
     * Export this route to a KML file using [KmlExporter].
     *
     * @throws IOException if the file cannot be written
     */
    @Throws(IOException::class)
    fun toKml(): File = KmlExporter().export(allStops())
}
