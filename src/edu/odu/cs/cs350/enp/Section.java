package edu.odu.cs.cs350.enp;

/**
 * Represents a single section of a course, holding enrollment, capacity, and other details.
 */
public class Section {

    private String CRN;  // unique identifier for each section
    private String link;  // Used to associate labs and recitations to a lecture
    private int ENR;  // Number of students enrolled in the section
    private int XLST_CAP;  // cross-list cap: The maximum number of students that can enroll in this section
    private String teacherName;  // Teacher for this section
    private String XLST_GROUP;  // cross-list group for linking multiple sections

    // Constructor
    public Section(String CRN, String link, int ENR, int XLST_CAP,String XLST_GROUP) {
        this.CRN = CRN;
        this.link = link;
        this.ENR = ENR;
        this.XLST_CAP = XLST_CAP;
        
        this.XLST_GROUP = XLST_GROUP;
    }

    // Getters
    public String getCRN() {
        return CRN;
    }

    public String getLink() {
        return link;
    }

    public int getENR() {
        return ENR;
    }

    public int getXLST_CAP() {
        return XLST_CAP;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getXLST_GROUP() {
        return XLST_GROUP;
    }

    @Override
    public String toString() {
        return String.format("Section %s (Teacher: %s, Enrolled: %d, XLST_CAP: %d, Link: %s, XLST_GROUP: %s)",
                             CRN, teacherName, ENR, XLST_CAP, link, XLST_GROUP);
    }
}