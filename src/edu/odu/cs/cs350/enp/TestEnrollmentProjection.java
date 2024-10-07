package edu.odu.cs.cs350.enp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestEnrollmentProjection {

    private EnrollmentProjection enrollmentProjection;
    private SemesterSnapshotReader snapshotReaderMock;
    private DateValidator dateValidatorMock;
    private List<String> historicalSemesterDirs;
    private String targetSemesterDir;
    private String outputFile;

    @BeforeEach
    public void setUp() throws Exception {
        // Mocking dependencies
        snapshotReaderMock = Mockito.mock(SemesterSnapshotReader.class);
        dateValidatorMock = Mockito.mock(DateValidator.class);

        // Example test data
        historicalSemesterDirs = Arrays.asList("semester1", "semester2");
        targetSemesterDir = "target_semester";
        outputFile = "projection_output.txt";

        // Injecting mock DateValidator into the EnrollmentProjection constructor
        enrollmentProjection = new EnrollmentProjection(historicalSemesterDirs, targetSemesterDir, outputFile, null);
        
        // Setting up the snapshot processor and date validator mocks
        enrollmentProjection.snapshotProcessor = snapshotReaderMock;
        enrollmentProjection.dateValidator = dateValidatorMock;
    }

    @Test
    public void testResolveEndDate() throws Exception {
        // Custom end date passed
        LocalDate expectedEndDate = LocalDate.parse("2023-12-31", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        EnrollmentProjection projectionWithEndDate = new EnrollmentProjection(historicalSemesterDirs, targetSemesterDir, outputFile, "2023-12-31");
        assertEquals(expectedEndDate, projectionWithEndDate.resolveEndDate("2023-12-31"));

        // Default end date
        LocalDate defaultEndDate = LocalDate.parse("2024-08-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        assertEquals(defaultEndDate, enrollmentProjection.resolveEndDate(null));
    }

    @Test
    public void testProcessHistoricalSemesters() throws Exception {
        // Mocking semester data
        List<Course> mockCourses = Arrays.asList(
                new Course("12345", "CS", "350", 100, 90, "A1", "B1", 200, 180)
        );
        when(snapshotReaderMock.processSnapshot(anyString(), any(LocalDate.class))).thenReturn(mockCourses);

        // Setting up mock snapshots
        when(snapshotReaderMock.getValidSnapshots(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Arrays.asList(
                        new Snapshot("2023-01-10.csv", LocalDate.of(2023, 1, 10)),
                        new Snapshot("2023-01-20.csv", LocalDate.of(2023, 1, 20))
                ));

        // Simulate processing the semester and verify
        List<List<Course>> historicalData = enrollmentProjection.processAllHistoricalSemesters();
        assertNotNull(historicalData);
        assertEquals(2, historicalData.size()); // Should process two semesters
    }

    @Test
    public void testProcessTargetSemester() throws Exception {
        // Mocking valid dates for the target semester
        when(dateValidatorMock.validateDatesTxt(anyString()))
                .thenReturn(new LocalDate[]{LocalDate.of(2023, 1, 10), LocalDate.of(2023, 2, 10)});

        // Mocking valid snapshots for the target semester
        when(snapshotReaderMock.getValidSnapshots(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Arrays.asList(
                        new Snapshot("2023-01-10.csv", LocalDate.of(2023, 1, 10)),
                        new Snapshot("2023-01-20.csv", LocalDate.of(2023, 1, 20))
                ));

        // Process the target semester and verify
        List<Course> targetData = enrollmentProjection.processTargetSemester();
        assertNotNull(targetData);
    }

    @Test
    public void testGenerateReport() throws Exception {
        // This test can be extended to verify how the report generation works
        // Ensure that the method is being called with valid inputs
        List<Course> mockCourses = Arrays.asList(
                new Course("12345", "CS", "350", 100, 90, "A1", "B1", 200, 180)
        );
        LocalDate preRegistrationStart = LocalDate.of(2023, 1, 10);
        LocalDate addDeadline = LocalDate.of(2023, 2, 10);
        LocalDate lastSnapshotDate = LocalDate.of(2023, 1, 20);

        enrollmentProjection.generateReport(mockCourses, preRegistrationStart, addDeadline, lastSnapshotDate);
        // Verify that report logic works as expected here
    }
}