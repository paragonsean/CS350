package edu.odu.cs.cs350.enp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * A Snapshot containing a collection of courses, organized by their CRNs.
 * 
 * This class stores and manages courses by their CRN.
 * 
 * @author zeil
 *
 */
public class Snapshot implements Cloneable {

    private String fileName;  // Name of the snapshot
    private Map<String, Course> courses;  // Map of CRN to Course
    private LocalDate snapshotDate;  // Date the snapshot was taken

    /**
     * Create a "blank" snapshot with empty strings for the file name,
     * the current date for the snapshot date,
     * and an empty list of courses.
     */
    public Snapshot() {
        this.fileName = "emptySnapshot";
        this.snapshotDate = LocalDateTime.now().toLocalDate();
        this.courses = new TreeMap<>();  // Using TreeMap to maintain order based on CRNs
    }

    /**
     * Create a new snapshot.
     * @param fileName name of the snapshot
     * @param snapshotDate date the snapshot was taken
     */
    public Snapshot(String fileName, LocalDate snapshotDate) {
        this.fileName = fileName;
        this.snapshotDate = snapshotDate;
        this.courses = new TreeMap<>();
    }

    /**
     * Get the file name of this snapshot.
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Set the file name of this snapshot.
     * @param fileName the file name to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Get the snapshot date.
     * @return the snapshot date
     */
    public LocalDate getSnapshotDate() {
        return snapshotDate;
    }

    /**
     * Set the snapshot date.
     * @param snapshotDate the snapshot date to set
     */
    public void setSnapshotDate(LocalDate snapshotDate) {
        this.snapshotDate = snapshotDate;
    }

    /**
     * Get the number of courses in this snapshot.
     * @return number of courses
     */
    public int numCourses() {
        return courses.size();
    }

    /**
     * Add a course to the snapshot.
     * 
     * If a course with the same CRN already exists, replaces the existing one.
     * 
     * @param course the course to add
     */
    public void addCourse(Course course) {
        courses.put(course.getTitle(), course);  // Use course title or CRN as the key
    }

    /**
     * Get the course previously placed at a given CRN.
     * 
     * @param crn a CRN in the snapshot
     * @return the course with that CRN, or null if no course has been put there.
     */
    public Course getCourse(String crn) {
        return courses.get(crn);
    }

    /**
     * Render the snapshot as a string in a format guaranteed to
     * contain all fields.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(fileName);
        sb.append(", ");
        sb.append(snapshotDate.toString());
        sb.append("\nCourses:");
        for (String crn : courses.keySet()) {
            sb.append("\n   ");
            sb.append(crn);
            sb.append("  ");
            sb.append(courses.get(crn).toString());
        }
        return sb.toString();
    }

    /**
     * Compares two snapshots for equality. They are considered equal if
     * all fields on them return equal results.
     *
     * @param obj object to be compared for equality with this snapshot
     * @return <tt>true</tt> if the specified object is equal to this one
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Snapshot)) {
            return false;
        }
        Snapshot other = (Snapshot) obj;
        return fileName.equals(other.fileName) &&
                snapshotDate.equals(other.snapshotDate) &&
                courses.equals(other.courses);
    }

  
  

    /**
     * Provide access to the list of CRNs in
     * this snapshot.
     * 
     * @return set of CRNs. CRNs are returned in ascending order.
     */
    public Set<String> getCRNs() {
        return courses.keySet();
    }
}