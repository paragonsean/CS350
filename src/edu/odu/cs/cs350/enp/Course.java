package edu.odu.cs.cs350.enp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A Course containing a collection of sections, organized by course details.
 * Each course can have multiple sections, and details such as enrollment, capacity, etc., are maintained.
 * 
 * @author zeil
 */
public class Course implements Iterable<Section> {

    private String title;
    private String subject;  // e.g., "CS" for Computer Science
    private int courseNumber;  // e.g., 350 for "CS350"
    private ArrayList<Section> sections;  // Holds all sections of this course
    private int overallEnrollment;  // Total enrollment for the course across all sections
    private int overallCapacity;  // Total capacity across all sections
    private String crossListGroup;  // Used to link sections that are cross-listed

    /**
     * Create a new course with no sections.
     * @param title title of the course
     * @param subject the department offering the course (e.g., "CS")
     * @param courseNumber the course number (e.g., 350 for "CS350")
     * @param crossListGroup the cross-list group identifier, if applicable
     */
    public Course(String title, String subject, int courseNumber, String crossListGroup) {
        this.title = title;
        this.subject = subject;
        this.courseNumber = courseNumber;
        this.sections = new ArrayList<>();
        this.overallEnrollment = 0;
        this.overallCapacity = 0;
        this.crossListGroup = crossListGroup;
    }

    /**
     * Add a section to the course.
     * @param section the section to add
     */
    public void addSection(Section section) {
        sections.add(section);
        overallEnrollment += section.getEnrolledStudents();
        overallCapacity += section.getCapacity();
    }

    /**
     * Get the total enrollment across all sections.
     * @return total enrollment
     */
    public int getOverallEnrollment() {
        return overallEnrollment;
    }

    /**
     * Get the total capacity across all sections.
     * @return total capacity
     */
    public int getOverallCapacity() {
        return overallCapacity;
    }

    /**
     * Get the number of sections in this course.
     * @return number of sections
     */
    public int getNumSections() {
        return sections.size();
    }

    /**
     * Get the course title.
     * @return the course title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the course subject (e.g., "CS").
     * @return the course subject
     */
    public String getSubject() {
        return subject;
    }
    
    /**
     * Get the course number (e.g., 350 for "CS350").
     * @return the course number
     */
    public int getCourseNumber() {
        return courseNumber;
    }

    /**
     * Get the cross-list group identifier.
     * @return cross-list group identifier
     */
    public String getCrossListGroup() {
        return crossListGroup;
    }

    /**
     * Get the list of sections in the course.
     * @return list of sections
     */
    public List<Section> getSections() {
        return new ArrayList<>(sections);  // Return a copy of the sections list to avoid external modification
    }

    @Override
    public Iterator<Section> iterator() {
        return sections.iterator();
    }

    /**
     * Return a string representation of the course.
     * @return string representation
     */
    @Override
    public String toString() {
        return String.format("%s %d: %s (Enrollment: %d, Capacity: %d, Sections: %d)",
                             subject, courseNumber, title, overallEnrollment, overallCapacity, getNumSections());
    }
}