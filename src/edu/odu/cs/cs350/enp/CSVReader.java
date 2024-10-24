package edu.odu.cs.cs350.enp;

import java.io.*;
import java.util.*;

public class CSVReader<T> {

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

    private boolean processCharacter(char currentChar, int currentIndex, String line, List<String> fields, StringBuilder currentField, boolean insideQuotes) {
        if (insideQuotes) {
            return handleInsideQuotes(currentChar, currentIndex, line, currentField);
        } else {
            return handleOutsideQuotes(currentChar, fields, currentField);
        }
    }

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

    private void processCurrentField(List<String> fields, StringBuilder currentField) {
        fields.add(currentField.toString().trim());
        currentField.setLength(0); // Clear the current field
    }

    private boolean isEscapedQuote(int currentIndex, String line) {
        return currentIndex + 1 < line.length() && line.charAt(currentIndex + 1) == '"';
    }

    private boolean isQuoteCharacter(char currentChar) {
        return currentChar == '"';
    }

    private boolean isFieldSeparator(char currentChar) {
        return currentChar == ',';
    }

    private void processFieldCharacter(StringBuilder currentField, char currentChar) {
        currentField.append(currentChar);
    }

    // Default method to process a row, this can be overridden by subclasses if needed
    public T processRow(String[] row, String[] headers) throws Exception {
        // Default implementation: return the row as an array of fields
        // If T is expected to be something specific, this would need to change
        // Here, we assume T is just String[] for demonstration
        return (T) row;
    }

    // Modify to use generic List<T>
    public List<T> processCSV(String csvFilePath) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String[] headers = readHeaders(br);
            return readData(br, headers);
        }
    }

    protected String[] readHeaders(BufferedReader br) throws IOException {
        String line = br.readLine(); // Read header
        if (line == null) return new String[0];
        return parseCSVLine(line);
    }

    protected List<T> readData(BufferedReader br, String[] headers) throws Exception {
        List<T> data = new ArrayList<>();
        String row;
        while ((row = br.readLine()) != null) {
            String[] fields = parseCSVLine(row); // Parse each row
            data.add(processRow(fields, headers));
        }
        return data;
    }
}
    