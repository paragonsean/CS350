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

    private String title;  // Title of the course
    private String SUBJ;  // Identifies the department offering the course (e.g., "CS" for Computer Science)
    private int CRSE;  // Course number (e.g., 350 for "CS350")
    private ArrayList<Section> sections;  // Holds all sections of this course
    private int OVERALL_ENR;  // Total enrollment for the course across all sections
    private int OVERALL_CAP;  // Total capacity across all sections
    private String XLST_GROUP;  // Used to link sections that are cross-listed

    /**
     * Create a new course with no sections.
     * @param title title of the course
     * @param SUBJ the department offering the course (e.g., "CS")
     * @param CRSE the course number (e.g., 350 for "CS350")
     * @param XLST_GROUP the cross-list group identifier, if applicable
     */
    public Course(String title, String SUBJ, int CRSE, String XLST_GROUP) {
        this.title = title;
        this.SUBJ = SUBJ;
        this.CRSE = CRSE;
        this.sections = new ArrayList<>();
        this.OVERALL_ENR = 0;
        this.OVERALL_CAP = 0;
        this.XLST_GROUP = XLST_GROUP;
    }

    /**
     * Add a section to the course.
     * @param section the section to add
     */
    public void addSection(Section section) {
        sections.add(section);
        OVERALL_ENR += section.getENR();
        OVERALL_CAP += section.getXLST_CAP();
    }

    /**
     * Get the total enrollment across all sections.
     * @return total enrollment
     */
    public int getOVERALL_ENR() {
        return OVERALL_ENR;
    }

    /**
     * Get the total capacity across all sections.
     * @return total capacity
     */
    public int getOVERALL_CAP() {
        return OVERALL_CAP;
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
    public String getSUBJ() {
        return SUBJ;
    }
    
    /**
     * Get the course number (e.g., 350 for "CS350").
     * @return the course number
     */
    public int getCRSE() {
        return CRSE;
    }

    /**
     * Get the cross-list group identifier.
     * @return cross-list group identifier
     */
    public String getXLST_GROUP() {
        return XLST_GROUP;
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
                             SUBJ, CRSE, title, OVERALL_ENR, OVERALL_CAP, getNumSections());
    }
}