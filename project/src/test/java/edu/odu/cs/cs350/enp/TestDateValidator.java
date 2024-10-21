package edu.odu.cs.cs350.enp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TestDateValidator {

    private DateValidator dateValidator;
    private String tempSemesterDir;
    private DateTimeFormatter dateFormatter;

    @BeforeEach
    public void setUp() throws IOException {
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dateValidator = new DateValidator(dateFormatter);

        // Create a temporary directory for testing
        tempSemesterDir = "test_semester_dir";
        Path semesterDirPath = Paths.get(tempSemesterDir);
        Files.createDirectories(semesterDirPath);

        // Create a temporary dates.txt file with valid data
        Path datesFilePath = semesterDirPath.resolve("dates.txt");
        List<String> datesContent = List.of("2024-01-10", "2024-01-31");
        Files.write(datesFilePath, datesContent);
    }

    /**
     * Test method for validateDatesTxt.
     */
    @Test
    public void testValidateDatesTxt() throws IOException {
        LocalDate[] dates = dateValidator.validateDatesTxt(tempSemesterDir);

        LocalDate expectedPreRegistrationStart = LocalDate.parse("2024-01-10", dateFormatter);
        LocalDate expectedAddDeadline = LocalDate.parse("2024-01-31", dateFormatter);

        assertNotNull(dates);
        assertEquals(2, dates.length);
        assertEquals(expectedPreRegistrationStart, dates[0]);
        assertEquals(expectedAddDeadline, dates[1]);
    }

    /**
     * Test method for getDatesFilePath.
     */
    @Test
    public void testGetDatesFilePath() throws IOException {
        Path datesFilePath = Paths.get(tempSemesterDir, "dates.txt");
        assertTrue(Files.exists(datesFilePath));
    }

    /**
     * Test method for missing dates.txt file.
     */
    @Test
    public void testMissingDatesFile() {
        // Remove the dates.txt file to simulate a missing file
        Path datesFilePath = Paths.get(tempSemesterDir, "dates.txt");
        try {
            Files.delete(datesFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        IOException thrown = assertThrows(IOException.class, () -> {
            dateValidator.validateDatesTxt(tempSemesterDir);
        });

        assertEquals("Missing dates.txt in test_semester_dir", thrown.getMessage());
    }

    /**
     * Test method for invalid dates.txt format.
     */
    @Test
    public void testInvalidDatesFileFormat() throws IOException {
        // Write an invalid dates.txt file with fewer than 2 lines
        Path datesFilePath = Paths.get(tempSemesterDir, "dates.txt");
        List<String> invalidDatesContent = List.of("2024-01-10");
        Files.write(datesFilePath, invalidDatesContent);

        IOException thrown = assertThrows(IOException.class, () -> {
            dateValidator.validateDatesTxt(tempSemesterDir);
        });

        assertEquals("Invalid dates.txt format in dates.txt", thrown.getMessage());
    }
}