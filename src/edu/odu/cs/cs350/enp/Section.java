package edu.odu.cs.cs350.enp;

/**
 * Represents a single section of a course, holding enrollment, capacity, and other details.
 */
public class Section {

    private String CRN;  // unique identifier for each section
    private String link;  // Used to associate labs and recitations to a lecture
    private int enrolledStudents;  // Number of students enrolled in the section
    private int capacity;  // Maximum number of students that can enroll in this section
    private String teacherName;  // Teacher for this section

    // Constructor
    public Section(String CRN, String link, int enrolledStudents, int capacity, String teacherName) {
        this.CRN = CRN;
        this.link = link;
        this.enrolledStudents = enrolledStudents;
        this.capacity = capacity;
        this.teacherName = teacherName;
    }

    // Getters
    public String getCRN() {
        return CRN;
    }

    public String getLink() {
        return link;
    }

    public int getEnrolledStudents() {
        return enrolledStudents;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getTeacherName() {
        return teacherName;
    }

    @Override
    public String toString() {
        return String.format("Section %s (Teacher: %s, Enrolled: %d, Capacity: %d, Link: %s)",
                             CRN, teacherName, enrolledStudents, capacity, link);
    }
}