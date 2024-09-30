package edu.odu.cs.cs350.enp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CSVProcessorTest {

    private String csvFilePath;
    private CSVProcessor csvProcessor;

    @BeforeEach
    public void setUp() throws IOException {
        csvFilePath = "test_courses.csv";  // The path where the test CSV file will be saved

        // Create a sample CSV file for testing
        try (FileWriter writer = new FileWriter(csvFilePath)) {
            writer.write("CRN,SUBJ,CRSE,XLST CAP,ENR,LINK,XLST GROUP,OVERALL CAP,OVERALL ENR\n");
            writer.write("12345,CS,350,100,90,A1,B1,200,180\n");
            writer.write("67890,MATH,101,50,40,B2,C3,100,80\n");
        }

        csvProcessor = new CSVProcessor();  // Initialize CSVProcessor
    }

    @Test
    public void testProcessCSV() throws Exception {
        // Process the CSV file and generate a snapshot
        Snapshot snapshot = csvProcessor.processCSV(csvFilePath, "TestSnapshot", LocalDate.now());

        // Validate the snapshot content
        assertEquals("TestSnapshot", snapshot.getFileName(), "Snapshot file name should match.");
        assertEquals(2, snapshot.numCourses(), "Snapshot should contain 2 courses.");

        // Validate the first course
        Course cs350 = snapshot.getCourse("CS 350");
        assertNotNull(cs350, "CS 350 should be in the snapshot.");
        assertEquals("CS", cs350.getSubject(), "Subject should be CS.");
        assertEquals(350, cs350.getCourseNumber(), "Course number should be 350.");
     

        // Validate the second course
        Course math101 = snapshot.getCourse("MATH 101");
        assertNotNull(math101, "MATH 101 should be in the snapshot.");
        assertEquals("MATH", math101.getSubject(), "Subject should be MATH.");
        assertEquals(101, math101.getCourseNumber(), "Course number should be 101.");
     
    }

    @Test
    public void testInvalidRowHandling() throws Exception {
        // Test invalid row handling (missing fields or incorrect data types)
        try (FileWriter writer = new FileWriter(csvFilePath, true)) {
            writer.write("INVALID,ROW,FORMAT\n");
        }

        // Process the CSV file and validate that invalid rows are handled without crashing
        Snapshot snapshot = csvProcessor.processCSV(csvFilePath, "TestSnapshotWithInvalidRow", LocalDate.now());

        // Validate that the snapshot still contains valid courses
        assertEquals(2, snapshot.numCourses(), "Snapshot should contain 2 valid courses, ignoring invalid rows.");
    }
}