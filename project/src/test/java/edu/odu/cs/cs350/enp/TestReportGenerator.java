package edu.odu.cs.cs350.enp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestReportGenerator {

    private ReportGenerator reportGenerator;
    private SimpleDateFormat dateFormat;
    private List<Course> semesterData;

    @BeforeEach
    public void setUp() {
        reportGenerator = new ReportGenerator();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Set up sample semester data
        semesterData = new ArrayList<>();
        semesterData.add(new Course("12345", "CS", "350", 100, 90, "A1", "B1", 200, 180));
        semesterData.add(new Course("67890", "MATH", "101", 50, 40, "B2", "C3", 100, 80));
        semesterData.add(new Course("98765", "PHYS", "201", 75, 60, "C1", "D1", 150, 120));
    }

   





    @Test
    public void testGenerateProjectionReport() throws Exception {
        Date preRegistrationStart = dateFormat.parse("2024-01-01");
        Date addDeadline = dateFormat.parse("2024-01-31");
        Date lastSnapshotDate = dateFormat.parse("2024-01-15");

        // Capture the printed output (mocking the System.out printReport call is an option)
        reportGenerator.generateProjectionReport(semesterData, preRegistrationStart, addDeadline, lastSnapshotDate);
        
        // Test could verify output if it was directed to a log file or captured with a custom output stream.
    }
}