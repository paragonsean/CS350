package edu.odu.cs.cs350;

import java.util.Objects;

public class Course implements Cloneable {
    private String CRN;
    private String SUBJ;
    private String CRSE;
    private int XLST_CAP;
    private int ENR;
    private String LINK;
    private String XLST_GROUP;
    private int OVERALL_CAP;
    private int OVERALL_ENR;
    private String snapshotDate;

    // Constructor
    public Course(String CRN, String SUBJ, String CRSE, int XLST_CAP, int ENR, String LINK, String XLST_GROUP, int OVERALL_CAP, int OVERALL_ENR, String snapshotDate) {
        this.CRN = CRN;
        this.SUBJ = SUBJ;
        this.CRSE = CRSE;
        this.XLST_CAP = XLST_CAP;
        this.ENR = ENR;
        this.LINK = LINK;
        this.XLST_GROUP = XLST_GROUP;
        this.OVERALL_CAP = OVERALL_CAP;
        this.OVERALL_ENR = OVERALL_ENR;
        this.snapshotDate = snapshotDate;
    }

    // Getters and setters
    public String getCRN() { return CRN; }
    public String getSUBJ() { return SUBJ; }
    public String getCRSE() { return CRSE; }
    public int getXLST_CAP() { return XLST_CAP; }
    public int getENR() { return ENR; }
    public String getLINK() { return LINK; }
    public String getXLST_GROUP() { return XLST_GROUP; }
    public int getOVERALL_CAP() { return OVERALL_CAP; }
    public int getOVERALL_ENR() { return OVERALL_ENR; }
    public String getSnapshotDate() { return snapshotDate; }

    // Implement the clone() method
    @Override
    public Course clone() {
        try {
            return (Course) super.clone();  // Shallow copy, since all fields are primitives or immutable (String)
        } catch (CloneNotSupportedException e) {
            // This shouldn't happen, since we're implementing Cloneable
            throw new AssertionError();
        }
    }

    // Override the hashCode() method
    @Override
    public int hashCode() {
        return Objects.hash(CRN, SUBJ, CRSE, XLST_CAP, ENR, LINK, XLST_GROUP, OVERALL_CAP, OVERALL_ENR, snapshotDate);
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
                Objects.equals(XLST_GROUP, course.XLST_GROUP) &&
                Objects.equals(snapshotDate, course.snapshotDate);
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
                ", snapshotDate='" + snapshotDate + '\'' +
                '}';
    }
}