package com.correos.delivery.export;

import com.example.routes.Address;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Utility class that exports a list of {@link Address} instances to a simple
 * KML file so it can be loaded by navigation apps supporting this format.
 */
public class KmlExporter {

    /**
     * Write the provided route to a KML file.
     *
     * @param route ordered list of addresses representing the route
     * @return file pointing to the generated KML document
     * @throws IOException if the file cannot be written
     */
    public File export(List<Address> route) throws IOException {
        File file = File.createTempFile("route", ".kml");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n");
            writer.write("  <Document>\n");
            writer.write("    <name>Route</name>\n");
            for (Address a : route) {
                writer.write(String.format(
                        "    <Placemark><name>%s %s %s</name><Point><coordinates>%f,%f</coordinates></Point></Placemark>\n",
                        a.getPostalCode(), a.getStreet(), a.getNumber(),
                        a.getLongitude(), a.getLatitude()));
            }
            writer.write("  </Document>\n");
            writer.write("</kml>\n");
        }
        return file;
    }
}
