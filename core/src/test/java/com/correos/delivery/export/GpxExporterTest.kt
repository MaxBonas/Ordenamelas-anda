package com.correos.delivery.export

import com.example.routes.Address
import org.junit.Assert.*
import org.junit.Test
import javax.xml.parsers.DocumentBuilderFactory

class GpxExporterTest {
    @Test
    fun exportGeneratesGpxFileWithAllAddresses() {
        val addresses = listOf(
            Address("12345", "Street A", "1", 1.0, 2.0),
            Address("54321", "Street B", "2", 3.0, 4.0)
        )
        val exporter = GpxExporter()
        val file = exporter.export(addresses)
        try {
            assertTrue(file.exists())

            val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
            val root = doc.documentElement
            assertEquals("gpx", root.nodeName)

            val trksegs = doc.getElementsByTagName("trkseg")
            assertTrue(trksegs.length > 0)

            val trkpts = doc.getElementsByTagName("trkpt")
            assertEquals(addresses.size, trkpts.length)
        } finally {
            file.delete()
        }
    }
}
