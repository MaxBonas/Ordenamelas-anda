package com.example.routes

import org.junit.Assert.assertTrue
import org.junit.Test
import com.correos.delivery.core.model.Address

class RouteExportTest {
    private val origin = Address("0", "Origin", "1", 0.0, 0.0)
    private val waypoint = Address("1", "Waypoint", "1", 0.5, 0.5)
    private val destination = Address("2", "Dest", "2", 1.0, 1.0)
    private val route = Route(origin, destination, listOf(waypoint))

    @Test
    fun toGpxCreatesGpxFile() {
        val file = route.toGpx()
        val text = file.readText()
        assertTrue(text.contains("<gpx"))
        assertTrue(text.contains("lat=\"0.0\" lon=\"0.0\""))
        assertTrue(text.contains("lat=\"0.5\" lon=\"0.5\""))
        assertTrue(text.contains("lat=\"1.0\" lon=\"1.0\""))
    }

    @Test
    fun toKmlCreatesKmlFile() {
        val file = route.toKml()
        val text = file.readText()
        assertTrue(text.contains("<kml"))
        assertTrue(text.contains(",0.0</coordinates>"))
        assertTrue(text.contains(",0.5</coordinates>"))
        assertTrue(text.contains(",1.0</coordinates>"))
    }
}
