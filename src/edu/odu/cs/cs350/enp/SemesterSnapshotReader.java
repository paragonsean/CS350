package edu.odu.cs.cs350.enp;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

public class SemesterSnapshotReader {
    private static final Logger logger = Logger.getLogger(SemesterSnapshotReader.class.getName());
    private DateTimeFormatter dateTimeFormatter;
    private List<Snapshot> snapshots; // 3.2.2.1 Attributes
    private CSVReader<Course> csvReader; // Use CSVReader as a utility

    public SemesterSnapshotReader(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
        this.snapshots = new ArrayList<>();
        this.csvReader = new CSVReader<>(); // Initialize the CSVReader
    }

    public List<Snapshot> getValidSnapshots(String semesterDir, LocalDate preRegistrationStart, LocalDate addDeadline) throws Exception {
        List<Path> csvFiles = listCsvFiles(semesterDir);
        List<Snapshot> validSnapshots = filterSnapshots(csvFiles, preRegistrationStart, addDeadline);

        if (validSnapshots.size() < 2) {
            throw new IOException("Insufficient snapshots in " + Paths.get(semesterDir).getFileName());
        }

        return validSnapshots;
    }

    private List<Path> listCsvFiles(String semesterDir) throws IOException {
        List<Path> csvFiles = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(semesterDir), "*.csv")) {
            for (Path path : directoryStream) {
                csvFiles.add(path);
            }
        }
        return csvFiles;
    }

    private List<Snapshot> filterSnapshots(List<Path> csvFiles, LocalDate preRegistrationStart, LocalDate addDeadline) throws Exception {
        List<Snapshot> filteredSnapshots = new ArrayList<>();
        for (Path path : csvFiles) {
            String fileName = path.getFileName().toString();
            LocalDate snapshotDate;
            try {
                snapshotDate = LocalDate.parse(fileName.substring(0, 10), dateTimeFormatter);
            } catch (Exception e) {
                throw new RuntimeException("Error parsing date from file name: " + fileName, e);
            }
            if (!snapshotDate.isBefore(preRegistrationStart) && !snapshotDate.isAfter(addDeadline)) {
                filteredSnapshots.add(new Snapshot(fileName, snapshotDate));
            }
        }
        filteredSnapshots.sort(Comparator.comparing(Snapshot::getDate));
        return filteredSnapshots;
    }

    /**
     * Maps the parsed CSV fields to a Course object.
     * This method will be called when processing each row in the CSV file.
     * 
     * @param fields  The fields in the CSV row
     * @param headers The headers of the CSV file
     * @return A Course object built from the CSV row
     */
    public Course mapFieldsToCourse(String[] fields, String[] headers) {
        Map<String, Integer> headerMap = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            headerMap.put(headers[i].trim(), i);
        }

        try {
            return new Course(
                fields[headerMap.get("CRN")],
                fields[headerMap.get("SUBJ")],
                fields[headerMap.get("CRSE")],
                parseInt(fields[headerMap.get("XLST CAP")]),
                parseInt(fields[headerMap.get("ENR")]),
                fields[headerMap.get("LINK")],
                fields[headerMap.get("XLST GROUP")],
                parseInt(fields[headerMap.get("OVERALL CAP")]),
                parseInt(fields[headerMap.get("OVERALL ENR")])
            );
        } catch (Exception e) {
            logger.warning("Error processing row: " + Arrays.toString(fields) + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Processes a snapshot CSV file and returns a list of Course objects.
     *
     * @param snapshotPath The path to the snapshot CSV file.
     * @param snapshotDate The date of the snapshot.
     * @return A list of Course objects.
     * @throws Exception If an error occurs while processing the snapshot.
     */
    public List<Course> processSnapshot(String snapshotPath, LocalDate snapshotDate) throws Exception {
        List<Course> snapshotData = new ArrayList<>();
        try {
            // Expect a list of Course objects instead of raw String[] data
            List<Course> rawData = csvReader.processCSV(snapshotPath); 
            snapshotData.addAll(rawData);
        } catch (IOException e) {
            logger.severe("Error reading snapshot file: " + snapshotPath + " - " + e.getMessage());
        }
        return snapshotData;
    }
    

    /**
     * Parses a string value into an integer.
     *
     * @param value The string to parse.
     * @return The parsed integer, or 0 if the string is null or empty.
     */
    private int parseInt(String value) {
        if (value == null || value.trim().isEmpty()) return 0;
        return Integer.parseInt(value.trim());
    }

    /**
     * Lists all available snapshots.
     *
     * @return A list of all snapshots.
     */
    public List<Snapshot> listSnapshots() {
        return new ArrayList<>(snapshots);
    }

    /**
     * Filters snapshots by a date range.
     *
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     * @return A list of snapshots within the specified date range.
     */
    public List<Snapshot> filterSnapshotsByDate(LocalDate startDate, LocalDate endDate) {
        List<Snapshot> filteredSnapshots = new ArrayList<>();
        for (Snapshot snapshot : snapshots) {
            if (!snapshot.getDate().isBefore(startDate) && !snapshot.getDate().isAfter(endDate)) {
                filteredSnapshots.add(snapshot);
            }
        }
        return filteredSnapshots;
    }
}
