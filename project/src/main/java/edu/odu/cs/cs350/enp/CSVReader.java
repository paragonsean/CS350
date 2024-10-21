package edu.odu.cs.cs350.enp;

import java.io.*;
import java.util.*;

/**
 * A generic CSVReader class to read and parse CSV files.
 * This class provides functionality for parsing CSV lines,
 * processing rows, and reading CSV files.
 * 
 * @param <T> the type of object each row will be processed into
 */
public class CSVReader<T> {

    /**
     * Parses a single line of a CSV file into an array of fields.
     * This method handles fields enclosed in quotes and escapes quotes properly.
     * 
     * @param line the line to parse from the CSV file
     * @return an array of fields parsed from the line
     */
    public String[] parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char currentChar = line.charAt(i);
            insideQuotes = processCharacter(currentChar, i, line, fields, currentField, insideQuotes);
        }

        processCurrentField(fields, currentField); // Add the last field after finishing the loop
        return fields.toArray(new String[0]);
    }

    /**
     * Processes a single character in a CSV line, handling it according to
     * whether it is inside or outside of a quoted section.
     * 
     * @param currentChar the current character being processed
     * @param currentIndex the index of the current character in the line
     * @param line the entire CSV line
     * @param fields the list of fields parsed so far
     * @param currentField the current field being constructed
     * @param insideQuotes flag indicating if the current character is inside quotes
     * @return true if the next character is inside quotes, false otherwise
     */
    private boolean processCharacter(char currentChar, int currentIndex, String line, List<String> fields, StringBuilder currentField, boolean insideQuotes) {
        if (insideQuotes) {
            return handleInsideQuotes(currentChar, currentIndex, line, currentField);
        } else {
            return handleOutsideQuotes(currentChar, fields, currentField);
        }
    }

    /**
     * Handles the logic for characters inside a quoted section of a CSV field.
     * 
     * @param currentChar the current character being processed
     * @param currentIndex the index of the current character in the line
     * @param line the entire CSV line
     * @param currentField the current field being constructed
     * @return true if the next character is still inside quotes, false if quotes have closed
     */
    private boolean handleInsideQuotes(char currentChar, int currentIndex, String line, StringBuilder currentField) {
        if (isQuoteCharacter(currentChar)) {
            if (isEscapedQuote(currentIndex, line)) {
                currentField.append('"');
                return true;
            } else {
                return false; // End of quoted section
            }
        } else {
            processFieldCharacter(currentField, currentChar);
            return true;
        }
    }

    /**
     * Handles the logic for characters outside of a quoted section of a CSV field.
     * 
     * @param currentChar the current character being processed
     * @param fields the list of fields parsed so far
     * @param currentField the current field being constructed
     * @return true if quotes were opened, false otherwise
     */
    private boolean handleOutsideQuotes(char currentChar, List<String> fields, StringBuilder currentField) {
        if (isQuoteCharacter(currentChar)) {
            return true; // Start of quoted section
        } else if (isFieldSeparator(currentChar)) {
            processCurrentField(fields, currentField); // End of field
            return false; // Still outside quotes
        } else {
            processFieldCharacter(currentField, currentChar);
            return false;
        }
    }

    /**
     * Adds the current field to the list of fields and resets the field builder.
     * 
     * @param fields the list of fields parsed so far
     * @param currentField the current field being constructed
     */
    private void processCurrentField(List<String> fields, StringBuilder currentField) {
        fields.add(currentField.toString().trim());
        currentField.setLength(0); // Clear the current field
    }

    /**
     * Checks if the current character is an escaped quote (i.e., two consecutive quotes).
     * 
     * @param currentIndex the index of the current character
     * @param line the entire CSV line
     * @return true if the current character is an escaped quote, false otherwise
     */
    private boolean isEscapedQuote(int currentIndex, String line) {
        return currentIndex + 1 < line.length() && line.charAt(currentIndex + 1) == '"';
    }

    /**
     * Checks if the current character is a quote character.
     * 
     * @param currentChar the character to check
     * @return true if the character is a quote, false otherwise
     */
    private boolean isQuoteCharacter(char currentChar) {
        return currentChar == '"';
    }

    /**
     * Checks if the current character is a field separator (comma).
     * 
     * @param currentChar the character to check
     * @return true if the character is a comma, false otherwise
     */
    private boolean isFieldSeparator(char currentChar) {
        return currentChar == ',';
    }

    /**
     * Appends the current character to the current field being constructed.
     * 
     * @param currentField the current field being constructed
     * @param currentChar the character to append
     */
    private void processFieldCharacter(StringBuilder currentField, char currentChar) {
        currentField.append(currentChar);
    }

    /**
     * Processes a row of the CSV file. This method can be overridden by subclasses
     * to provide custom processing logic.
     * 
     * @param row an array of fields representing a row in the CSV file
     * @param headers an array of headers from the CSV file
     * @return an object of type T representing the processed row
     * @throws Exception if an error occurs during row processing
     */
    public T processRow(String[] row, String[] headers) throws Exception {
        // Default implementation: return the row as an array of fields
        // If T is expected to be something specific, this would need to change
        // Here, we assume T is just String[] for demonstration
        return (T) row;
    }

    /**
     * Reads and processes a CSV file, converting each row into an object of type T.
     * 
     * @param csvFilePath the path to the CSV file
     * @return a list of objects representing the rows in the CSV file
     * @throws Exception if an error occurs while reading or processing the file
     */
    public List<T> processCSV(String csvFilePath) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String[] headers = readHeaders(br);
            return readData(br, headers);
        }
    }

    /**
     * Reads the headers of a CSV file.
     * 
     * @param br the BufferedReader for the CSV file
     * @return an array of headers
     * @throws IOException if an error occurs while reading the file
     */
    private String[] readHeaders(BufferedReader br) throws IOException {
        String line = br.readLine(); // Read header
        if (line == null) return new String[0];
        return parseCSVLine(line);
    }

    /**
     * Reads the data rows of a CSV file and processes each row into an object of type T.
     * 
     * @param br the BufferedReader for the CSV file
     * @param headers an array of headers from the CSV file
     * @return a list of objects representing the rows in the CSV file
     * @throws Exception if an error occurs during row processing
     */
    private List<T> readData(BufferedReader br, String[] headers) throws Exception {
        List<T> data = new ArrayList<>();
        String row;
        while ((row = br.readLine()) != null) {
            String[] fields = parseCSVLine(row); // Parse each row
            data.add(processRow(fields, headers));
        }
        return data;
    }
}
