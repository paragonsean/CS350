package edu.odu.cs.cs350.enp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

public class TestCSVReader {

    private CSVReader csvReader;
    private String tempCsvFilePath;
    private LocalDate snapshotDate;

    @BeforeEach
    public void setUp() throws Exception {
        csvReader = new CSVReader();
        snapshotDate = LocalDate.now();

        // Create a temporary CSV file for testing
        tempCsvFilePath = "test_snapshot.csv";
        try (FileWriter writer = new FileWriter(tempCsvFilePath)) {
            writer.append("CRN,SUBJ,CRSE,XLST CAP,ENR,LINK,XLST GROUP,OVERALL CAP,OVERALL ENR\n");
            writer.append("12345,CS,350,100,90,A1,B1,200,180\n");
            writer.append("67890,MATH,101,50,40,B2,C3,100,80\n");
            writer.append("98765,PHYS,201,75,60,C1,D1,150,120\n");
        }
    }

    /**
     * Test method for {@link edu.odu.cs.cs350.enp.CSVReader#parseCSVLine(String)}.
     */
    @Test
    public void testParseCSVLine() {
        String line = "12345,CS,350,100,90,A1,B1,200,180";
        String[] expectedFields = {"12345", "CS", "350", "100", "90", "A1", "B1", "200", "180"};
        String[] parsedFields = csvReader.parseCSVLine(line);

        assertArrayEquals(expectedFields, parsedFields);
    }

    /**
     * Test method for {@link edu.odu.cs.cs350.enp.CSVReader#processCSV(String, String, LocalDate)}.
     */
    @Test
    public void testProcessCSV() throws Exception {
        Snapshot snapshot = csvReader.processCSV(tempCsvFilePath, "test_snapshot.csv", snapshotDate);

        assertThat(snapshot.getDate(), equalTo(snapshotDate));
        assertThat(snapshot.getCourseList().size(), is(3));

        Course course1 = snapshot.getCourse("350");
        assertThat(course1.getCRN(), equalTo("12345"));
        assertThat(course1.getSUBJ(), equalTo("CS"));
        assertThat(course1.getCRSE(), equalTo("350"));
        assertThat(course1.getENR(), is(90));
        assertThat(course1.getOVERALL_CAP(), is(200));

        Course course2 = snapshot.getCourse("101");
        assertThat(course2.getCRN(), equalTo("67890"));
        assertThat(course2.getSUBJ(), equalTo("MATH"));
        assertThat(course2.getCRSE(), equalTo("101"));

        Course course3 = snapshot.getCourse("201");
        assertThat(course3.getCRN(), equalTo("98765"));
        assertThat(course3.getSUBJ(), equalTo("PHYS"));
        assertThat(course3.getCRSE(), equalTo("201"));
    }

    /**
     * Test method for {@link edu.odu.cs.cs350.enp.CSVReader#createCourseFromCSV(String[], Map)}.
     */
    @Test
    public void testCreateCourseFromCSV() throws Exception {
        String[] fields = {"12345", "CS", "350", "100", "90", "A1", "B1", "200", "180"};
        Map<String, Integer> headerMap = new HashMap<>();
        headerMap.put("CRN", 0);
        headerMap.put("SUBJ", 1);
        headerMap.put("CRSE", 2);
        headerMap.put("XLST CAP", 3);
        headerMap.put("ENR", 4);
        headerMap.put("LINK", 5);
        headerMap.put("XLST GROUP", 6);
        headerMap.put("OVERALL CAP", 7);
        headerMap.put("OVERALL ENR", 8);

        Course course = csvReader.createCourseFromCSV(fields, headerMap);

        assertThat(course.getCRN(), equalTo("12345"));
        assertThat(course.getSUBJ(), equalTo("CS"));
        assertThat(course.getCRSE(), equalTo("350"));
        assertThat(course.getENR(), is(90));
        assertThat(course.getOVERALL_CAP(), is(200));
        assertThat(course.getLINK(), equalTo("A1"));
        assertThat(course.getXLST_GROUP(), equalTo("B1"));
    }

    

    /**
     * Cleanup after each test.
     */
    @AfterEach
    public void tearDown() throws IOException {
        // Delete the temporary CSV file after the test
        Path path = Paths.get(tempCsvFilePath);
        Files.deleteIfExists(path);
    }
}