package org.example;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code Section} class represents a section of a course in an academic setting.
 * It stores details about the section, including course reference number (CRN),
 * subject, course code, cross-list group, section capacity, enrollment, and other details.
 * 
 * <p>The class includes validation checks to ensure that section parameters such as 
 * capacity and enrollment adhere to logical constraints. Logging is provided for 
 * error handling and validation exceptions.
 * 
 * <p>Note: This class is immutable for CRN, subject, course code, and semester code 
 * after construction to maintain section integrity.
 */
public class Section {

    private static final Logger logger = LoggerFactory.getLogger(Section.class);

    // Fields
    private final String crn;
    private final String subj;
    private final String crse;
    private String xlstGroup;
    private String link;
    private int sectionCapacity;
    private int sectionEnrollment;
    private final String semesterCode;
    private final String campus;
    /**
     * Constructs a {@code Section} object with specified attributes.
     * 
     * @param crn the Course Reference Number of the section
     * @param subj the subject or department code (e.g., "CS" for Computer Science)
     * @param crse the course number (e.g., "350")
     * @param sectionCapacity the maximum capacity of the section
     * @param sectionEnrollment the current enrollment in the section
     * @param link a code representing section links such as lab or recitation links
     * @param xlstGroup the cross-list group identifier
     * @param semesterCode the code representing the semester
     * @param campus the campus at which the section is offered
     * @throws IllegalArgumentException if any parameter validation fails
     */
    public Section(String crn, String subj, String crse, int sectionCapacity, int sectionEnrollment, String link, String xlstGroup, String semesterCode, String campus) {
        try {
            ValidationUtils.validateSectionParameters(crn, subj, crse, semesterCode);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            throw e;
        }
        this.crn = crn;
        this.subj = subj;
        this.crse = crse;
        this.sectionCapacity = sectionCapacity;
        this.sectionEnrollment = sectionEnrollment;
        this.link = link;
        this.xlstGroup = xlstGroup;
        this.campus = campus;
        this.semesterCode = semesterCode;
    }

    /**
     * Sets the capacity of the section.
     * 
     * @param capacity the new capacity to be set
     * @throws IllegalArgumentException if the provided capacity is invalid
     */
    public void setCapacity(int capacity) {
        try {
            ValidationUtils.validateCapacity(capacity);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            throw e;
        }
        this.sectionCapacity = capacity;
    }

    /**
     * Sets the enrollment for the section.
     * 
     * <p>This method validates the provided enrollment value against the section's capacity.
     * If the enrollment value is valid, it updates the section's enrollment.
     * If the enrollment value is invalid, it logs the error and rethrows an IllegalArgumentException.
     *
     * @param enrollment the number of students to enroll in the section
     * @throws IllegalArgumentException if the enrollment value is invalid
     */
    public void setEnrollment(int enrollment) {
        try {
            ValidationUtils.validateEnrollment(enrollment, sectionCapacity);
            this.sectionEnrollment = enrollment;
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
    
    /**
     * Retrieves the capacity of the section.
     *
     * @return the capacity of the section
     */
    public int getSectionCapacity() {
        return sectionCapacity;
    }

    /**
     * Returns the number of students currently enrolled in the section.
     *
     * @return the section enrollment count
     */
    public int getSectionEnrollment() {
        return sectionEnrollment;
    }

    /**
     * Sets the link associated with the section.
     * 
     * @param link the new link value
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Retrieves the unique key for the section, which is the Course Reference Number (CRN).
     * 
     * @return the section key as a String
     */
    public String getSectionKey() {
        return crn;
    }

    /**
     * Sets the cross-list group for the section.
     * 
     * @param xlstGroup the new cross-list group identifier
     */
    public void setXlstGroup(String xlstGroup) {
        this.xlstGroup = xlstGroup;
    }

    /**
     * Retrieves the link associated with the section.
     * 
     * @return the link as a String
     */
    public String getLink() {
        return link;
    }

    /**
     * Retrieves the cross-list group identifier.
     * 
     * @return the cross-list group as a String
     */
    public String getXlstGroup() {
        return xlstGroup;
    }

    /**
     * Retrieves the Course Reference Number (CRN) for the section.
     * 
     * @return the CRN as a String
     */
    public String getCrn() {
        return crn;
    }

    /**
     * Retrieves the subject or department code for the section.
     * 
     * @return the subject as a String
     */
    public String getSubj() {
        return subj;
    }

    /**
     * Retrieves the course number associated with this section.
     * 
     * @return the course number as a String
     */
    public String getCrse() {
        return crse;
    }

    public String getCampus() {
        return campus;
    }
    /**
     * Retrieves the semester code for the section.
     * 
     * @return the semester code as a String
     */
    public String getSemesterCode() {
        return semesterCode;
    }
    
    /**
     * Checks if this {@code Section} object is equal to another object.
     * 
     * <p>The objects are considered equal if they are of the same class and
     * have the same CRN and semester code.
     *
     * @param o the object to be compared
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(crn + "-" + semesterCode, section.crn + "-" + section.semesterCode);
    }

    /**
     * Returns the hash code for this {@code Section} object.
     *
     * @return the hash code based on the CRN and semester code
     */
    @Override
    public int hashCode() {
        return Objects.hash(crn + "-" + semesterCode);
    }

    /**
     * Returns a string representation of the section.
     * 
     * <p>The string contains details about the section, including CRN, subject,
     * course number, enrollment, capacity, link, cross-list group, and semester code.
     * 
     * @return a formatted string representing the section
     */
    @Override
    public String toString() {
        return String.format("Section{crn='%s', subj='%s', crse='%s', %d/%d students currently enrolled in section, link='%s', xlstGroup='%s', semesterCode='%s'}", crn, subj, crse, sectionEnrollment, sectionCapacity, link, xlstGroup, semesterCode);
    }
}
