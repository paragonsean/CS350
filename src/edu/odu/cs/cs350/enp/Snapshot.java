package edu.odu.cs.cs350.enp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Snapshot {
    private String filename;  // Filename in the format 'yyyy-MM-dd.csv'
    private LocalDate snapshotDate;  // Snapshot date
    private ArrayList<Course> coursesInSemester;  // Courses for this snapshot

    // Date format for verifying file names
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // Default constructor
    public Snapshot() {
        this.filename = "00000000.csv";
        this.snapshotDate = LocalDate.now();
        this.coursesInSemester = new ArrayList<>();
    }

    // Parameterized constructor
    public Snapshot(String filename, LocalDate date, ArrayList<Course> coursesInSemester) {
        if (!verifyFilename(filename)) {
            throw new IllegalArgumentException("Invalid filename format. It must be in 'yyyy-MM-dd.csv' format.");
        }
        this.filename = filename;
        this.snapshotDate = date;
        this.coursesInSemester = new ArrayList<>(coursesInSemester);  // Deep copy of the course list
    }

    // Another parameterized constructor for filename and date only
    public Snapshot(String filename, LocalDate date) {
        if (!verifyFilename(filename)) {
            throw new IllegalArgumentException("Invalid filename format. It must be in 'yyyy-MM-dd.csv' format.");
        }
        this.filename = filename;
        this.snapshotDate = date;
        this.coursesInSemester = new ArrayList<>();
    }

    // Parameterized constructor with date and course list
    public Snapshot(LocalDate date, ArrayList<Course> coursesInSemester) {
        this.snapshotDate = date;
        this.coursesInSemester = new ArrayList<>(coursesInSemester);  // Deep copy of the course list
    }

    // Method to add a course to the list of courses
    public void addCourse(Course addCourse) {
        coursesInSemester.add(addCourse);
    }

    // Method to get a course by its course code (CRSE)
    public Course getCourse(String courseCode) {
        for (Course course : coursesInSemester) {
            if (course.getCRSE().equals(courseCode)) {
                return course;
            }
        }
        return null;
    }

    // Getters and Setters
    public void setFileName(String filename) {
        if (!verifyFilename(filename)) {
            throw new IllegalArgumentException("Invalid filename format. It must be in 'yyyy-MM-dd.csv' format.");
        }
        this.filename = filename;
    }

    public String getFileName() {
        return this.filename;
    }

    public void setDate(LocalDate date) {
        this.snapshotDate = date;
    }

    public LocalDate getDate() {
        return this.snapshotDate;
    }

    public List<Course> getCourseList() {
        return new ArrayList<>(this.coursesInSemester);  // Return a deep copy of the course list
    }

    // Verify if the filename matches the 'yyyy-MM-dd.csv' format
    private boolean verifyFilename(String fileName) {
        if (!fileName.endsWith(".csv")) {
            return false;
        }

        String datePart = fileName.substring(0, fileName.length() - 4);

        try {
            dateFormat.setLenient(false);  // Ensure strict parsing of the date format
            dateFormat.parse(datePart);    // This will throw ParseException if the format is invalid
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    // toString method to provide a human-readable representation of the snapshot
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Snapshot Filename: " + filename + "\nDate: " + snapshotDate + "\nCourses:\n");
        for (Course course : coursesInSemester) {
            sb.append(course.toString()).append("\n");
        }
        return sb.toString();
    }
}