package edu.odu.cs.cs350.enp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestDateValidator {

    private DateValidator dateValidator;
    private String tempSemesterDir;
    private SimpleDateFormat dateFormat;

    @BeforeEach
    public void setUp() throws IOException {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateValidator = new DateValidator(dateFormat);

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
     * Method to get the file path for dates.txt.
     */
    private Path getDatesFilePath(String semesterDir) throws IOException {
        Path datesFile = Paths.get(semesterDir, "dates.txt");
        if (!Files.exists(datesFile)) {
            throw new IOException("Missing dates.txt in " + Paths.get(semesterDir).getFileName());
        }
        return datesFile;
    }

    /**
     * Method to read the dates.txt file and return its lines.
     */
    private List<String> readDatesFile(Path datesFile) throws IOException {
        List<String> lines = Files.readAllLines(datesFile);
        if (lines.size() < 2) {
            throw new IOException("Invalid dates.txt format in " + datesFile.getFileName());
        }
        return lines;
    }

    /**
     * Method to parse the lines from dates.txt into Date objects.
     */
    private Date[] parseDates(List<String> lines) throws ParseException {
        Date preRegistrationStart = dateFormat.parse(lines.get(0).trim());
        Date addDeadline = dateFormat.parse(lines.get(1).trim());
        return new Date[]{preRegistrationStart, addDeadline};
    }

    /**
     * Test method for validateDatesTxt.
     */
    @Test
    public void testValidateDatesTxt() throws IOException, ParseException {
        Date[] dates = dateValidator.validateDatesTxt(tempSemesterDir);

        Date expectedPreRegistrationStart = dateFormat.parse("2024-01-10");
        Date expectedAddDeadline = dateFormat.parse("2024-01-31");

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
        Path datesFilePath = getDatesFilePath(tempSemesterDir);
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
            getDatesFilePath(tempSemesterDir);
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
            readDatesFile(datesFilePath);
        });

        assertEquals("Invalid dates.txt format in dates.txt", thrown.getMessage());
    }

    /**
     * Test method for parseDates.
     */
    @Test
    public void testParseDates() throws ParseException {
        List<String> datesContent = List.of("2024-01-10", "2024-01-31");
        Date[] parsedDates = parseDates(datesContent);

        Date expectedPreRegistrationStart = dateFormat.parse("2024-01-10");
        Date expectedAddDeadline = dateFormat.parse("2024-01-31");

        assertEquals(expectedPreRegistrationStart, parsedDates[0]);
        assertEquals(expectedAddDeadline, parsedDates[1]);
    }
}