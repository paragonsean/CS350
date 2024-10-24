package edu.odu.cs.cs350.enp;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class TestCSVReader {

    @Test
    void testParseCSVLine() {
        CSVReader<String[]> csvReader = new CSVReader<>();
        String line = "\"John, Doe\",25,\"New York\"";

        // Expected result: 3 fields: "John, Doe", 25, "New York"
        String[] expected = {"John, Doe", "25", "New York"};
        String[] result = csvReader.parseCSVLine(line);

        // Assert that the result matches the expected output
        assertArrayEquals(expected, result);
    }

    @Test
    void testParseCSVLineWithEscapedQuotes() {
        CSVReader<String[]> csvReader = new CSVReader<>();
        String line = "\"Jane \"\"JD\"\" Doe\",30,\"Los Angeles\"";

        // Expected result: 3 fields: "Jane \"JD\" Doe", 30, "Los Angeles"
        String[] expected = {"Jane \"JD\" Doe", "30", "Los Angeles"};
        String[] result = csvReader.parseCSVLine(line);

        // Assert that the result matches the expected output
        assertArrayEquals(expected, result);
    }

    @Test
    void testProcessRow() throws Exception {
        CSVReader<String[]> csvReader = new CSVReader<>();
        String[] headers = {"First Name", "Age", "City"};
        String[] row = {"John", "25", "New York"};

        // Process row using default implementation (returns the row itself)
        String[] result = csvReader.processRow(row, headers);

        // Assert that the result matches the input row
        assertArrayEquals(row, result);
    }

    @Test
    void testProcessCSVFromFile() throws Exception {
        CSVReader<String[]> csvReader = new CSVReader<>();

        // Path to a sample CSV file
        String csvFilePath = "src/test/resources/sample.csv"; // Replace with your actual file path

        // Process the CSV file
        List<String[]> data = csvReader.processCSV(csvFilePath);

        // Verify that data was read correctly (modify the assertions based on the actual CSV content)
        assertNotNull(data, "Data should not be null");
        assertFalse(data.isEmpty(), "Data should not be empty");

        // Optionally verify the content based on the expected CSV file
        // For example:
        // String[] expectedHeaders = {"Header1", "Header2", "Header3"};
        // assertArrayEquals(expectedHeaders, data.get(0));
    }

    @Test
    void testReadHeaders() throws IOException {
        CSVReader<String[]> csvReader = new CSVReader<>();

        // Prepare a CSV file content (in-memory using StringReader)
        String csvContent = "First Name,Age,City\nJohn,25,New York\nJane,30,Los Angeles";
        BufferedReader reader = new BufferedReader(new StringReader(csvContent));

        // Read the headers from the CSV
        String[] headers = csvReader.readHeaders(reader);

        // Verify the headers
        String[] expectedHeaders = {"First Name", "Age", "City"};
        assertArrayEquals(expectedHeaders, headers);
    }

    @Test
    void testReadData() throws Exception {
        CSVReader<String[]> csvReader = new CSVReader<>();

        // Prepare a CSV file content (in-memory using StringReader)
        String csvContent = "First Name,Age,City\nJohn,25,New York\nJane,30,Los Angeles";
        BufferedReader reader = new BufferedReader(new StringReader(csvContent));

        // Read headers and data
        String[] headers = csvReader.readHeaders(reader);
        List<String[]> data = csvReader.readData(reader, headers);

        // Verify the data
        String[] row1 = {"John", "25", "New York"};
        String[] row2 = {"Jane", "30", "Los Angeles"};

        assertArrayEquals(row1, data.get(0));
        assertArrayEquals(row2, data.get(1));
    }
}
