package edu.odu.cs.cs350.enp;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

public class EnrollmentProjection {

    protected static final Logger logger = Logger.getLogger(EnrollmentProjection.class.getName());

    protected List<String> historicalSemesterDirs;
    protected String targetSemesterDir;
    protected String outputFile;
    protected LocalDate endDate;
    protected LocalDate defaultEndDate;
    protected DateTimeFormatter dateFormatter;
    protected DateValidator dateValidator;
    protected SemesterSnapshotReader snapshotProcessor;

    public EnrollmentProjection(List<String> historicalSemesterDirs, String targetSemesterDir, String outputFile, String endDateStr) throws Exception {
        this.historicalSemesterDirs = historicalSemesterDirs;
        this.targetSemesterDir = targetSemesterDir;
        this.outputFile = outputFile;
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.dateValidator = new DateValidator(dateFormatter);

        this.endDate = resolveEndDate(endDateStr);
    }

    // Resolves end date or returns a default one
    protected LocalDate resolveEndDate(String endDateStr) {
        this.defaultEndDate = LocalDate.parse("2024-08-01", dateFormatter);
        if (endDateStr != null) {
            return LocalDate.parse(endDateStr, dateFormatter);
        }
        return this.defaultEndDate;
    }

    public void projectEnrollment() throws Exception {
        logger.info("Starting projection processing.");
        List<List<Course>> historicalData = processAllHistoricalSemesters();
        List<Course> targetData = processTargetSemester();
        // Further operations could be added here after target semester processing
    }

    // Processes all historical semesters
    protected List<List<Course>> processAllHistoricalSemesters() {
        List<List<Course>> historicalData = new ArrayList<>();
        for (String semesterDir : this.historicalSemesterDirs) {
            List<Course> semesterData = processIndividualSemester(semesterDir);
            if (semesterData != null) {
                historicalData.add(semesterData);
            }
        }
        return historicalData;
    }

    // Processes the target semester
    protected List<Course> processTargetSemester() throws Exception {
        try {
            logger.info("Processing target semester: " + this.targetSemesterDir);
            return processSemester(this.targetSemesterDir);
        } catch (IOException e) {
            logger.severe("Error processing target semester: " + e.getMessage());
            throw e;
        }
    }

    // Processes a single semester
    public List<Course> processSemester(String semesterDir) throws Exception {
        LocalDate[] dates = dateValidator.validateDatesTxt(semesterDir);
        LocalDate preRegistrationStart = dates[0];
        LocalDate addDeadline = dates[1];

        List<Snapshot> validSnapshots = snapshotProcessor.getValidSnapshots(semesterDir, preRegistrationStart, addDeadline);
        LocalDate lastSnapshotDate = validSnapshots.get(validSnapshots.size() - 1).getDate();

        List<Course> semesterData = collectSemesterData(validSnapshots, semesterDir);
        generateReport(semesterData, preRegistrationStart, addDeadline, lastSnapshotDate);

        return semesterData;
    }

    // Collects course data for a semester based on snapshots
    protected List<Course> collectSemesterData(List<Snapshot> snapshots, String semesterDir) throws Exception {
        List<Course> semesterData = new ArrayList<>();
        for (Snapshot snapshot : snapshots) {
            String snapshotPath = getSnapshotPath(semesterDir, snapshot);
            semesterData.addAll(snapshotProcessor.processSnapshot(snapshotPath, snapshot.getDate()));
        }
        return semesterData;
    }

    // Generates the report for the projection
    protected void generateReport(List<Course> semesterData, LocalDate preRegistrationStart, LocalDate addDeadline, LocalDate lastSnapshotDate) throws Exception {
        // Logic for generating the report goes here
    }

    // Generates snapshot path
    protected String getSnapshotPath(String semesterDir, Snapshot snapshot) {
        return Paths.get(semesterDir, snapshot.getFileName()).toString();
    }

    // Processes an individual semester safely (catches errors)
    protected List<Course> processIndividualSemester(String semesterDir) {
        try {
            logger.info("Processing historical semester: " + semesterDir);
            return processSemester(semesterDir);
        } catch (Exception e) {
            logger.severe("Error processing historical semester: " + semesterDir + ": " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            if (args.length < 4) {
                System.out.println("Usage: java EnrollmentProjection <historical_dirs> <target_dir> <output_file> [<end_date>]");
                System.exit(1);
            }

            List<String> historicalSemesterDirs = extractHistoricalSemesterDirs(args);
            String targetSemesterDir = args[args.length - 3];
            String outputFile = args[args.length - 2];
            String endDateStr = extractEndDateStr(args);

            EnrollmentProjection projectionSystem = new EnrollmentProjection(historicalSemesterDirs, targetSemesterDir, outputFile, endDateStr);
            projectionSystem.projectEnrollment();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Extracts historical semester directories from the arguments
    protected static List<String> extractHistoricalSemesterDirs(String[] args) {
        List<String> historicalSemesterDirs = new ArrayList<>();
        for (int i = 0; i < args.length - 3; i++) {
            historicalSemesterDirs.add(args[i]);
        }
        return historicalSemesterDirs;
    }

    // Extracts the end date string from the arguments if provided
    protected static String extractEndDateStr(String[] args) {
        if (args.length >= 5) {
            return args[args.length - 1];
        }
        return null;
    }
}