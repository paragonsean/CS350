package edu.odu.cs.cs350.enp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.time.LocalDate;



/**
 * A CSVProcessor specifically designed to process CSV data into Snapshot objects.
 */
public class CSVProcessor {

    // Method to parse a single line of CSV and return an array of fields
    public String[] parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char currentChar = line.charAt(i);

            if (insideQuotes) {
                if (currentChar == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        currentField.append('"');
                        i++;
                    } else {
                        insideQuotes = false;
                    }
                } else {
                    currentField.append(currentChar);
                }
            } else {
                if (currentChar == '"') {
                    insideQuotes = true;
                } else if (currentChar == ',') {
                    fields.add(currentField.toString().trim());
                    currentField.setLength(0);
                } else {
                    currentField.append(currentChar);
                }
            }
        }
        fields.add(currentField.toString().trim());
        return fields.toArray(new String[0]);
    }

    // Method to process the entire CSV file and return a Snapshot object
    public Snapshot processCSV(String csvFilePath, String fileName, LocalDate snapshotDate) throws Exception {
        Snapshot snapshot = new Snapshot(fileName, snapshotDate);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line = br.readLine(); // Read header
            if (line == null) return snapshot;

            String[] headers = parseCSVLine(line); // Parse the header line
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                headerMap.put(headers[i].trim(), i);
            }

            String row;
            while ((row = br.readLine()) != null) {
                String[] fields = parseCSVLine(row); // Parse each row
                try {
                    Course course = createCourseFromCSV(fields, headerMap);
                    snapshot.addCourse(course);
                } catch (Exception e) {
                    System.err.println("Error processing row: " + Arrays.toString(fields) + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading snapshot file: " + csvFilePath + " - " + e.getMessage());
        }

        return snapshot;
    }

    // Method to create a Course object from the parsed CSV fields
    private Course createCourseFromCSV(String[] fields, Map<String, Integer> headerMap) {
        String crn = fields[headerMap.get("CRN")];
        String subj = fields[headerMap.get("SUBJ")];
        int courseNumber = Integer.parseInt(fields[headerMap.get("CRSE")]);
        int xlstCap = Integer.parseInt(fields[headerMap.get("XLST CAP")]);
        int enr = Integer.parseInt(fields[headerMap.get("ENR")]);
        String link = fields[headerMap.get("LINK")];
        String xlstGroup = fields[headerMap.get("XLST GROUP")];
        int overallCap = Integer.parseInt(fields[headerMap.get("OVERALL CAP")]);
        int overallEnr = Integer.parseInt(fields[headerMap.get("OVERALL ENR")]);
        String title = subj + " " + courseNumber;

        // Create the course object
        return new Course(crn, subj, courseNumber, xlstCap, enr, link, xlstGroup, overallCap, overallEnr, title);
    }
}
