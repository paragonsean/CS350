package org.example;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Snapshot implements Iterable<Course> {
    private static final Logger logger = LoggerFactory.getLogger(Snapshot.class);

    private String filename;  // Filename in the format 'yyyy-MM-dd.csv'
    private LocalDate snapshotDate;  // Snapshot date
    private Map<String, Course> coursesByKey;  // Courses for this snapshot, stored by course key

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{4}-\\d{2}-\\d{2})");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Default constructor
    public Snapshot() {
        this.filename = "0000-00-00.csv";
        this.snapshotDate = LocalDate.now();
        this.coursesByKey = new HashMap<>();
    }

    // Parameterized constructor that attempts to parse the date from the filename
    public Snapshot(String filename, List<Course> coursesInSemester) {
        this.filename = filename;
        this.snapshotDate = extractDateFromFilename(filename, DATE_PATTERN, DATE_FORMATTER);
        if (this.snapshotDate == null) {
            throw new IllegalArgumentException("Invalid filename format. Date not found in 'yyyy-MM-dd.csv' format.");
        }
        this.coursesByKey = new HashMap<>();
        for (Course course : coursesInSemester) {
            addCourse(course);
        }
    }

    // Parameterized constructor with a known date
    public Snapshot(LocalDate date, List<Course> coursesInSemester) {
        this.snapshotDate = date;
        this.coursesByKey = new HashMap<>();
        for (Course course : coursesInSemester) {
            addCourse(course);
        }
    }

    // Method to add a course to the snapshot with logging for add and merge actions
    public void addCourse(Course course) {
        String courseKey = course.getCourseKey();
        if (coursesByKey.containsKey(courseKey)) {
            logger.info("Merging course: {} with existing course in snapshot on date {}", courseKey, snapshotDate);
            coursesByKey.get(courseKey).mergeCourse(course);
        } else {
            logger.info("Adding new course: {} to snapshot on date {}", courseKey, snapshotDate);
            coursesByKey.put(courseKey, course);
        }
    }

    // Method to get a course by its course code (CRSE)
    public Course getCourse(String courseCode) {
        return coursesByKey.get(courseCode);
    }

    // Get total enrollments for all courses in this snapshot
    public Map<String, Integer> getCourseEnrollments() {
        Map<String, Integer> courseEnrollments = new HashMap<>();
        for (Map.Entry<String, Course> entry : coursesByKey.entrySet()) {
            String courseKey = entry.getKey();
            Course course = entry.getValue();
            int totalEnrollment = course.getTotalOfferingEnrollment();
            courseEnrollments.put(courseKey, totalEnrollment);
        }
        return courseEnrollments;
    }

    // Calculate the total section enrollment for all courses in this snapshot
    public int getTotalSectionEnrollment() {
        return coursesByKey.values().stream()
                .mapToInt(Course::getTotalSectionEnrollment)
                .sum();
    }

    // Calculate the total section capacity for all courses in this snapshot
    public int getTotalSectionCapacity() {
        return coursesByKey.values().stream()
                .mapToInt(Course::getTotalSectionCapacity)
                .sum();
    }

    // Extracts date from filename using regex pattern and formatter
    private LocalDate extractDateFromFilename(String filename, Pattern datePattern, DateTimeFormatter formatter) {
        Matcher matcher = datePattern.matcher(filename);
        if (matcher.find()) {
            try {
                return LocalDate.parse(matcher.group(1), formatter);
            } catch (DateTimeParseException e) {
                logger.warn("Error parsing date from filename {}: {}", filename, e.getMessage());
            }
        }
        return null;
    }

    // Getters for filename and date
    public String getFileName() {
        return this.filename;
    }

    @Override
    public Iterator<Course> iterator() {
        return coursesByKey.values().iterator();
    }
    
    public LocalDate getDate() {
        return this.snapshotDate;
    }

    // toString method to provide a human-readable representation of the snapshot
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Snapshot Filename: " + filename + "\nDate: " + snapshotDate + "\nCourses:\n");
        for (Course course : coursesByKey.values()) {
            sb.append(course.toString()).append("\n");
        }
        return sb.toString();
    }
}
