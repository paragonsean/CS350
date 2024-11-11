package org.example;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Offering {
    private static final Logger logger = LoggerFactory.getLogger(Offering.class);
    private final String offeringKey;
    private final String xlstGroup;
    private final String instructor;
    private final String semesterCode;
    private Map<String, Section> sections = new ConcurrentHashMap<>();
    private int offeringCapacity;
    private int offeringEnrollment;

    /**
     * Constructs an Offering object with the specified parameters.
     *
     * @param offeringKey       the unique key for the offering
     * @param xlstGroup         the group associated with the offering
     * @param offeringCapacity  the capacity of the offering
     * @param offeringEnrollment the current enrollment of the offering
     * @param instructor        the instructor for the offering
     * @param semesterCode      the code for the semester in which the offering is available
     * @throws IllegalArgumentException if any of the parameters are invalid
     */
    public Offering(String offeringKey, String xlstGroup, int offeringCapacity, int offeringEnrollment, String instructor, String semesterCode) {
        try {
            ValidationUtils.validateOfferingParameters(offeringKey, xlstGroup, instructor, semesterCode);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            throw e;
        }
        this.offeringKey = offeringKey;
        this.xlstGroup = xlstGroup;
        this.offeringCapacity = offeringCapacity;
        this.offeringEnrollment = offeringEnrollment;
        this.instructor = instructor;
        this.semesterCode = semesterCode;
    }



    /**
     * Retrieves the key associated with this offering.
     *
     * @return the offering key as a String.
     */
    public String getOfferingKey() {
        return offeringKey;
    }

    
    
    /**
     * Retrieves the sections associated with this offering.
     *
     * @return a map where the keys are section identifiers and the values are Section objects.
     */
    public Map<String, Section> getSections() {
        return sections;
    }

    
    /**
     * Returns the capacity of the offering.
     *
     * @return the offering capacity
     */
    public int getOfferingCapacity() {
        return offeringCapacity;
    }
    
    
    /**
     * Retrieves the value of the xlstGroup.
     *
     * @return the xlstGroup as a String.
     */
    public String getXlstGroup() {
    
        return xlstGroup;
    }
    
    
    
    
    /**
     * Retrieves the instructor of the offering.
     *
     * @return the instructor's name as a String.
     */
    public String getInstructor() {
        return instructor;
    }


    /**
     * Retrieves the semester code for the offering.
     *
     * @return the semester code as a String.
     */
    public String getSemesterCode() {
        return semesterCode;
    }
    
    
    
    
    /**
     * Returns the current enrollment count for the offering.
     *
     * @return the number of students enrolled in the offering
     */
    public int getOfferingEnrollment() {
        return offeringEnrollment;
    }



    /**
     * Adds a new section or updates an existing section with the provided parameters.
     *
     * @param crn               The course reference number of the section.
     * @param subj              The subject code of the course.
     * @param crse              The course number.
     * @param sectionCapacity   The capacity of the section.
     * @param sectionEnrollment The current enrollment of the section.
     * @param link              The link associated with the section.
     * @param xlstGroup         The cross-list group of the section.
     * @throws IllegalArgumentException if the section parameters are invalid.
     */
    public void addOrUpdateSection(String crn, String subj, String crse, int sectionCapacity, int sectionEnrollment, String link, String xlstGroup) {
        try {
            ValidationUtils.validateSectionParameters(crn, subj, crse, semesterCode);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            throw e;
        }
        String sectionKey = crn + "-" + semesterCode;
        sections.compute(sectionKey, (key, existingSection) -> handleAddOrUpdateSection(key, existingSection, subj, crse, sectionCapacity, sectionEnrollment, link, xlstGroup));
    }

    /**
     * Handles the addition or update of a Section object.
     *
     * @param key The unique key for the section.
     * @param existingSection The existing Section object, if it exists; otherwise, null.
     * @param subj The subject code of the section.
     * @param crse The course code of the section.
     * @param sectionCapacity The capacity of the section.
     * @param sectionEnrollment The current enrollment of the section.
     * @param link The link associated with the section.
     * @param xlstGroup The cross-list group of the section.
     * @return The newly created or updated Section object.
     */
    private Section handleAddOrUpdateSection(String key, Section existingSection, String subj, String crse, int sectionCapacity, int sectionEnrollment, String link, String xlstGroup) {
        if (existingSection == null) {
            return new Section(key, subj, crse, sectionCapacity, sectionEnrollment, link, xlstGroup, semesterCode, "additionalParameter");
        } else {
            existingSection.setCapacity(sectionCapacity);
            existingSection.setEnrollment(sectionEnrollment);
            existingSection.setLink(link);
            existingSection.setXlstGroup(xlstGroup);
            return existingSection;
        }
    }

    /**
     * Returns a string representation of the Offering object.
     * The string includes the offering key, cross-list group, sections, 
     * current enrollment, capacity, instructor, and semester code.
     *
     * @return a formatted string representing the Offering object
     */
    @Override
    public String toString() {
        return String.format("Offering{offeringKey='%s', xlstGroup='%s', sections=%s, %d/%d students currently enrolled in offering, instructor='%s', semesterCode='%s'}", offeringKey, xlstGroup, sections.keySet(), offeringEnrollment, offeringCapacity, instructor, semesterCode);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * 
     * @param o the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument; 
     *         {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offering offering = (Offering) o;
        return Objects.equals(offeringKey + "-" + semesterCode, offering.offeringKey + "-" + offering.semesterCode);
    }




    /**
     * Generates a hash code for the Offering object based on the combination
     * of offeringKey and semesterCode.
     *
     * @return an integer hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(offeringKey + "-" + semesterCode);
    }


}
