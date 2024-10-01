package edu.odu.cs.cs350.enp;

import java.io.IOException;
import java.nio.file.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DateValidator {
    private SimpleDateFormat dateFormat;

    public DateValidator(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Date[] validateDatesTxt(String semesterDir) throws IOException, ParseException {
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

    private Date[] parseDates(List<String> lines) throws ParseException {
        Date preRegistrationStart = dateFormat.parse(lines.get(0).trim());
        Date addDeadline = dateFormat.parse(lines.get(1).trim());
        return new Date[]{preRegistrationStart, addDeadline};
    }
}