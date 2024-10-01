package edu.odu.cs.cs350.enp;

import java.util.ArrayList;
import java.util.Iterator;
import java.time.LocalDate;

public class Snapshot implements  Iterable<Course> {

    ArrayList<Course> courseList = new ArrayList<Course>();
    LocalDate date = LocalDate.now();

    // Default constructor
    public Snapshot() {
        date = LocalDate.now();
        courseList = new ArrayList<Course>();
    }

    // Parameterized constructor
    public Snapshot(LocalDate date, ArrayList<Course> inCourseList) {
        this.date = date;
        this.courseList = new ArrayList<Course>(inCourseList);
    }
     // Parameterized constructor
     public Snapshot(LocalDate date) {
        this.date = date;
        
    }
    // Setters and getters
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return this.date;
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
    @Override
    public Iterator<Course> iterator() {
        return courseList.iterator();
    }
    // Implement the clone() method for deep cloning
   
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Course course : courseList) {
            sb.append(course.toString()).append(", ");
        }
        sb.append("\n");
        return sb.toString();
    }
}