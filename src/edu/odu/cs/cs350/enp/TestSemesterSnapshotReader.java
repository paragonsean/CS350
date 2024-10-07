package edu.odu.cs.cs350.enp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestSemesterSnapshotReader {

    private SemesterSnapshotReader snapshotReader;
    private String semesterDir;
    private DateTimeFormatter dateTimeFormatter;
    private LocalDate preRegistrationStart;
    private LocalDate addDeadline;

    @BeforeEach
    public void setUp() throws IOException {
        // Initialize DateTimeFormatter
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // Initialize SemesterSnapshotReader with DateTimeFormatter
        snapshotReader = new SemesterSnapshotReader(dateTimeFormatter);
        
        // Set up semester directory and pre-registration/add-deadline dates
        semesterDir = "test_semester";
        preRegistrationStart = LocalDate.parse("2024-01-10", dateTimeFormatter);
        addDeadline = LocalDate.parse("2024-01-31", dateTimeFormatter);
        
        // Create a temporary directory for the test
        Path semesterDirPath = Paths.get(semesterDir);
        Files.createDirectories(semesterDirPath);
        
        // Create some test CSV files with snapshot data
        Files.write(semesterDirPath.resolve("2024-01-12.csv"), List.of(
            "CRN,SUBJ,CRSE,XLST CAP,ENR,LINK,XLST GROUP,OVERALL CAP,OVERALL ENR",
            "12345,CS,350,100,90,A1,B1,200,180"
        ));
        Files.write(semesterDirPath.resolve("2024-02-05.csv"), List.of(
            "CRN,SUBJ,CRSE,XLST CAP,ENR,LINK,XLST GROUP,OVERALL CAP,OVERALL ENR",
            "67890,MATH,101,50,40,B2,C3,100,80"
        ));
    }

    @Test
    public void testGetValidSnapshots() throws Exception {
        List<Snapshot> validSnapshots = snapshotReader.getValidSnapshots(semesterDir, preRegistrationStart, addDeadline);
        
        // Test that valid snapshots within the date range are returned
        assertThat(validSnapshots.size(), is(1));
        assertThat(validSnapshots.get(0).getDate(), equalTo(LocalDate.parse("2024-01-12", dateTimeFormatter)));
    }



    @Test
    public void testProcessSnapshot() throws Exception {
        // Process a specific snapshot file and ensure the correct data is returned
        List<Course> courses = snapshotReader.processSnapshot(semesterDir + "/2024-01-12.csv", LocalDate.parse("2024-01-12", dateTimeFormatter));
        
        assertThat(courses.size(), is(1));
        Course course = courses.get(0);
        assertThat(course.getCRN(), equalTo("12345"));
        assertThat(course.getSUBJ(), equalTo("CS"));
        assertThat(course.getCRSE(), equalTo("350"));
        assertThat(course.getXLST_CAP(), is(100));
        assertThat(course.getENR(), is(90));
        assertThat(course.getOVERALL_CAP(), is(200));
        assertThat(course.getOVERALL_ENR(), is(180));
    }

    @Test
    public void testListSnapshots() {
        // Add snapshots to the reader for testing the listing method
        Snapshot snapshot1 = new Snapshot("2024-01-12.csv", LocalDate.parse("2024-01-12", dateTimeFormatter));
        Snapshot snapshot2 = new Snapshot("2024-02-05.csv", LocalDate.parse("2024-02-05", dateTimeFormatter));
        snapshotReader.listSnapshots().add(snapshot1);
        snapshotReader.listSnapshots().add(snapshot2);

        // Test listing of snapshots
        List<Snapshot> snapshots = snapshotReader.listSnapshots();
        assertThat(snapshots.size(), is(2));
        assertThat(snapshots, hasItems(snapshot1, snapshot2));
    }

    @Test
    public void testFilterSnapshotsByDate() {
        // Add some snapshots for testing
        snapshotReader.listSnapshots().add(new Snapshot("2024-01-12.csv", LocalDate.parse("2024-01-12", dateTimeFormatter)));
        snapshotReader.listSnapshots().add(new Snapshot("2024-02-05.csv", LocalDate.parse("2024-02-05", dateTimeFormatter)));
        
        // Filter snapshots by date range
        LocalDate startDate = LocalDate.parse("2024-01-01", dateTimeFormatter);
        LocalDate endDate = LocalDate.parse("2024-01-31", dateTimeFormatter);
        List<Snapshot> filteredSnapshots = snapshotReader.filterSnapshotsByDate(startDate, endDate);
        
        // Ensure only the snapshot within the date range is returned
        assertThat(filteredSnapshots.size(), is(1));
        assertThat(filteredSnapshots.get(0).getDate(), equalTo(LocalDate.parse("2024-01-12", dateTimeFormatter)));
    }
}