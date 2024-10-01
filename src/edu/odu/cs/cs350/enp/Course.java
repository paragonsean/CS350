package edu.odu.cs.cs350.enp;

import java.util.Objects;

public class Course implements Cloneable {
    private String CRN;           // Unique identifier for each section
    private String SUBJ;          // Department offering the course (e.g., "CS")
    private String CRSE;          // Course number (e.g., "350")
    private int XLST_CAP;         // Cross-list cap (maximum students in the section)
    private int ENR;              // Number of students enrolled
    private String LINK;          // Lab/recitation link code
    private String XLST_GROUP;    // Cross-list group identifier
    private int OVERALL_CAP;      // Maximum number of students in the overall offering
    private int OVERALL_ENR;      // Total students enrolled in the overall offering

    // Constructor
    public Course(String CRN, String SUBJ, String CRSE, int XLST_CAP, int ENR, String LINK, String XLST_GROUP, int OVERALL_CAP, int OVERALL_ENR) {
        this.CRN = CRN;
        this.SUBJ = SUBJ;
        this.CRSE = CRSE;
        this.XLST_CAP = XLST_CAP;
        this.ENR = ENR;
        this.LINK = LINK;
        this.XLST_GROUP = XLST_GROUP;
        this.OVERALL_CAP = OVERALL_CAP;
        this.OVERALL_ENR = OVERALL_ENR;
    }

    public Course() {
        this.CRN = "";
        this.SUBJ = "";
        this.CRSE = "";
        this.XLST_CAP = 0;
        this.ENR = 0;
        this.LINK = "";
        this.XLST_GROUP = "";
        this.OVERALL_CAP = 0;
        this.OVERALL_ENR = 0;
    }

    // Getters and setters
    public String getCRN() { return CRN; }
    public void setCRN(String CRN) { this.CRN = CRN; }

    public String getSUBJ() { return SUBJ; }
    public void setSUBJ(String SUBJ) { this.SUBJ = SUBJ; }

    public String getCRSE() { return CRSE; }
    public void setCRSE(String CRSE) { this.CRSE = CRSE; }

    public int getXLST_CAP() { return XLST_CAP; }
    public void setXLST_CAP(int XLST_CAP) { this.XLST_CAP = XLST_CAP; }

    public int getENR() { return ENR; }
    public void setENR(int ENR) { this.ENR = ENR; }

    public String getLINK() { return LINK; }
    public void setLINK(String LINK) { this.LINK = LINK; }

    public String getXLST_GROUP() { return XLST_GROUP; }
    public void setXLST_GROUP(String XLST_GROUP) { this.XLST_GROUP = XLST_GROUP; }

    public int getOVERALL_CAP() { return OVERALL_CAP; }
    public void setOVERALL_CAP(int OVERALL_CAP) { this.OVERALL_CAP = OVERALL_CAP; }

    public int getOVERALL_ENR() { return OVERALL_ENR; }
    public void setOVERALL_ENR(int OVERALL_ENR) { this.OVERALL_ENR = OVERALL_ENR; }

    // Implement the clone() method
    @Override
    public Course clone() {
        try {
            return (Course) super.clone();  // Shallow copy, since all fields are primitives or immutable (String)
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    // Override the hashCode() method
    @Override
    public int hashCode() {
        return Objects.hash(CRN, SUBJ, CRSE, XLST_CAP, ENR, LINK, XLST_GROUP, OVERALL_CAP, OVERALL_ENR);
    }

    // Override the equals() method (for consistency with hashCode)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return XLST_CAP == course.XLST_CAP &&
                ENR == course.ENR &&
                OVERALL_CAP == course.OVERALL_CAP &&
                OVERALL_ENR == course.OVERALL_ENR &&
                Objects.equals(CRN, course.CRN) &&
                Objects.equals(SUBJ, course.SUBJ) &&
                Objects.equals(CRSE, course.CRSE) &&
                Objects.equals(LINK, course.LINK) &&
                Objects.equals(XLST_GROUP, course.XLST_GROUP);
    }

    // toString() method for easier debugging
    @Override
    public String toString() {
        return "Course{" +
                "CRN='" + CRN + '\'' +
                ", SUBJ='" + SUBJ + '\'' +
                ", CRSE='" + CRSE + '\'' +
                ", XLST_CAP=" + XLST_CAP +
                ", ENR=" + ENR +
                ", LINK='" + LINK + '\'' +
                ", XLST_GROUP='" + XLST_GROUP + '\'' +
                ", OVERALL_CAP=" + OVERALL_CAP +
                ", OVERALL_ENR=" + OVERALL_ENR +
                '}';
    }
}