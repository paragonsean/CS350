package edu.odu.cs.cs350;

import java.util.ArrayList;


public class Snapshot implements Cloneable {

    ArrayList<Course> courseList;
    String fileName;

    // Default constructor
    public Snapshot() {
        fileName = "emptySnapshot";
        courseList = new ArrayList<Course>();
    }

    // Parameterized constructor
    public Snapshot(String inFileName, ArrayList<Course> inCourseList) {
        this.fileName = inFileName;
        this.courseList = inCourseList;
    }

    // Setters and getters
    public void setFileName(String inName) {
        fileName = inName;
    }

    public String getFileName() {
        return fileName;
    }

    // Method to add a course to the course list
    public void addCourse(Course addCourse) {
        courseList.add(addCourse);
    }

    // Method to get a course by its CRSE
    public Course getCourse(String inCRSE) {
        for (Course course : courseList) {
            if (course.getCRSE().equals(inCRSE)) {
                return course;
            }
        }
        return null;
    }

    // Getter for the course list
    public ArrayList<Course> getCourseList() {
        return courseList;
    }

    // Implement the clone() method for deep cloning
    @Override
    public Snapshot clone() {
        try {
            Snapshot clonedSnapshot = (Snapshot) super.clone();
            // Deep copy of the courseList (clone each Course object)
            clonedSnapshot.courseList = new ArrayList<Course>();
            for (Course course : this.courseList) {
                clonedSnapshot.courseList.add(course.clone());  // Cloning each Course
            }
            return clonedSnapshot;
        } catch (CloneNotSupportedException e) {
            // This should not happen since we are implementing Cloneable
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Snapshot{fileName='" + fileName + "', courseList=[");
        for (Course course : courseList) {
            sb.append(course.toString()).append(", ");
        }
        sb.append("]}");
        return sb.toString();
    }
}