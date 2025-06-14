package com.correos.delivery.export;

import com.example.routes.Address;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Utility class that exports a list of {@link Address} instances to a GPX file.
 * <p>
 * The generated file follows the GPX 1.1 schema with a simple track containing
 * all provided addresses as waypoints. The resulting file path can later be
 * shared with navigation apps that accept GPX imports.
 * </p>
 */
public class GpxExporter {

    /**
     * Write the provided route to a GPX file.
     *
     * @param route ordered list of addresses representing the route
     * @return file pointing to the generated GPX document
     * @throws IOException if the file cannot be written
     */
    public File export(List<Address> route) throws IOException {
        File file = File.createTempFile("route", ".gpx");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<gpx version=\"1.1\" creator=\"Ordenamelas-anda\">\n");
            writer.write("  <trk>\n");
            writer.write("    <name>Route</name>\n");
            writer.write("    <trkseg>\n");
            for (Address a : route) {
                writer.write(String.format(
                        "      <trkpt lat=\"%f\" lon=\"%f\"><name>%s %s %s</name></trkpt>\n",
                        a.getLatitude(), a.getLongitude(),
                        a.getPostalCode(), a.getStreet(), a.getNumber()));
            }
            writer.write("    </trkseg>\n");
            writer.write("  </trk>\n");
            writer.write("</gpx>\n");
        }
        return file;
    }
}
