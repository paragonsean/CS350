package edu.odu.cs.cs350.enp;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

public class SemesterSnapshotReader extends CSVReader<Course> {
    private static final Logger logger = Logger.getLogger(SemesterSnapshotReader.class.getName());
    private DateTimeFormatter dateTimeFormatter;
    private List<Snapshot> snapshots; // 3.2.2.1 Attributes

    public SemesterSnapshotReader(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
        this.snapshots = new ArrayList<>(); // Initialize the snapshots list
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
        List<Snapshot> snapshots = new ArrayList<>();
        for (Path path : csvFiles) {
            String fileName = path.getFileName().toString();
            LocalDate snapshotDate;
            try {
                snapshotDate = LocalDate.parse(fileName.substring(0, 10), dateTimeFormatter);
            } catch (Exception e) {
                throw new RuntimeException("Error parsing date from file name: " + fileName, e);
            }
            if (!snapshotDate.isBefore(preRegistrationStart) && !snapshotDate.isAfter(addDeadline)) {
                snapshots.add(new Snapshot(fileName, snapshotDate));
            }
        }
        snapshots.sort(Comparator.comparing(Snapshot::getDate));
        return snapshots;
    }

    @Override
    public Course processRow(String[] fields, String[] headers) throws Exception {
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
     * Processes a snapshot CSV file and returns a list of CourseData objects.
     *
     * @param snapshotPath the path to the snapshot CSV file
     * @param snapshotDate the date of the snapshot
     * @return a list of CourseData objects
     * @throws Exception if an error occurs while processing the snapshot
     */
    public List<Course> processSnapshot(String snapshotPath, LocalDate snapshotDate) throws Exception {
        List<Course> snapshotData = new ArrayList<>();
        try {
            List<Course> rawData = processCSV(snapshotPath);
            snapshotData.addAll(rawData);
        } catch (IOException e) {
            logger.severe("Error reading snapshot file: " + snapshotPath + " - " + e.getMessage());
        }
        return snapshotData;
    }

    private int parseInt(String value) {
        if (value == null || value.trim().isEmpty()) return 0;
        return Integer.parseInt(value.trim());
    }

    /**
     * Lists all snapshots.
     *
     * @return a list of all snapshots
     */
    public List<Snapshot> listSnapshots() {
        return new ArrayList<>(snapshots);
    }

    /**
     * Filters snapshots by date range.
     *
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a list of snapshots within the specified date range
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