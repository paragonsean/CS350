package edu.odu.cs.cs350.enp;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DateValidator {
    private DateTimeFormatter dateFormatter;

    public DateValidator(DateTimeFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    public LocalDate[] validateDatesTxt(String semesterDir) throws IOException, DateTimeParseException {
        Path datesFile = getDatesFilePath(semesterDir);
        List<String> lines = readDatesFile(datesFile);
        return parseDates(lines);
    }

    private Path getDatesFilePath(String semesterDir) throws IOException {
        Path datesFile = Paths.get(semesterDir, "dates.txt");
        if (!Files.exists(datesFile)) {
            throw new IOException("Missing dates.txt in " + Paths.get(semesterDir).getFileName());
        }
        return datesFile;
    }

    private List<String> readDatesFile(Path datesFile) throws IOException {
        List<String> lines = Files.readAllLines(datesFile);
        if (lines.size() < 2) {
            throw new IOException("Invalid dates.txt format in " + datesFile.getFileName());
        }
        return lines;
    }

    private LocalDate[] parseDates(List<String> lines) throws DateTimeParseException {
        LocalDate preRegistrationStart = LocalDate.parse(lines.get(0).trim(), dateFormatter);
        LocalDate addDeadline = LocalDate.parse(lines.get(1).trim(), dateFormatter);
        return new LocalDate[]{preRegistrationStart, addDeadline};
    }
}