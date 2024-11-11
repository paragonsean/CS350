package org.example;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Course implements Comparable<Course> {

    private static final Logger logger = Logger.getLogger(Course.class.getName());


    private static final String INVALID_CAPACITY_OR_ENROLLMENT_VALUES = "Invalid capacity or enrollment values";
    private static final String COURSE_SECTIONS_TO_STRING = "Course{subject='%s', courseNumber=%d, offerings=%s, date=%s}";
    private static final String SECTION_SUMMARY_FORMAT = "Section{key='%s', capacity=%d, enrollment=%d}";

    private String subject;
    private int courseNumber;
    protected String courseKey;
    private Map<String, Offering> offerings = new HashMap<>();
    private LocalDate date;
    private String semesterCode;

    /**
     * Constructs a new Course object with the specified subject, course number, and date.
     *
     * @param subject the subject of the course
     * @param courseNumber the course number
     * @param date the date associated with the course
     * @throws IllegalArgumentException if the subject is null or empty, or if the date is null
     */
    public Course(String subject, int courseNumber, String crn, String xlstGroup, int sectionCapacity, int sectionEnrollment, int offeringCapacity, int offeringEnrollment, String link, String instructor, LocalDate date) {
        // LoggerConfig.configureFileLogging(logger);  // Set up file logging for this logger

        initializeCourse(subject, courseNumber, date);

        initializeOfferingsAndSections(crn, xlstGroup, sectionCapacity, sectionEnrollment, offeringCapacity, offeringEnrollment, link, instructor, date);
    }

    /**
     * Constructs a new Course object with the specified subject, course number, and date.
     *
     * @param subject the subject of the course
     * @param courseNumber the course number
     * @param date the date associated with the course
     * @throws IllegalArgumentException if the subject is null or empty, or if the date is null
     */
    private void initializeOfferingsAndSections(String crn, String xlstGroup, int sectionCapacity, int sectionEnrollment, int offeringCapacity, int offeringEnrollment, String link, String instructor, LocalDate date) {
        this.semesterCode = ValidationUtils.generateSemesterCode(date);
        addOfferingsAndSections(crn, xlstGroup, sectionCapacity, sectionEnrollment, offeringCapacity, offeringEnrollment, link, instructor, semesterCode);
    }

    /**
     * Constructs a new Course object with the specified subject, course number, and date.
     *
     * @param subject the subject of the course
     * @param courseNumber the course number
     * @param date the date associated with the course
     * @throws IllegalArgumentException if the subject is null or empty, or if the date is null
     */
    private void initializeCourse(String subject, int courseNumber, LocalDate date) {
        ValidationUtils.validateCourseParameters(subject, date);
        setCourseProperties(subject, courseNumber, date);
    }

    /**
     * Sets the properties of the course.
     *
     * @param subject the subject of the course
     * @param courseNumber the course number
     * @param date the date associated with the course
     */
    private void setCourseProperties(String subject, int courseNumber, LocalDate date) {
        this.subject = subject;
        this.courseNumber = courseNumber;
        this.courseKey = generateCourseKey();
        this.date = date;
    }
        /**
     * Adds offerings and sections to a course.
     *
     * @param crn The course reference number.
     * @param xlstGroup The cross-list group identifier.
     * @param sectionCapacity The capacity of the section.
     * @param sectionEnrollment The current enrollment of the section.
     * @param offeringCapacity The capacity of the offering.
     * @param offeringEnrollment The current enrollment of the offering.
     * @param link The link associated with the section.
     * @param instructor The instructor of the offering.
     * @param semesterCode The code representing the semester.
     */
    public void addOfferingsAndSections(String crn, String xlstGroup, int sectionCapacity, int sectionEnrollment, int offeringCapacity, int offeringEnrollment, String link, String instructor, String semesterCode) {
        String sanitizedLink = ValidationUtils.sanitizeLink(link);
        String determinedXlstGroup = ValidationUtils.determineXlstGroup(xlstGroup, crn);
        ValidationUtils.validateCapacity(sectionCapacity);
        ValidationUtils.validateEnrollment(sectionEnrollment, sectionCapacity);
        Offering offering = getOrCreateOffering(determinedXlstGroup, offeringCapacity, offeringEnrollment, instructor, semesterCode);
        addSectionToOffering(offering, crn, sectionCapacity, sectionEnrollment, sanitizedLink, determinedXlstGroup, semesterCode);
    }

    /**
     * Retrieves the map of course offerings.
     *
     * @return a map where the keys are offering identifiers and the values are Offering objects.
     */
    public Map<String, Offering> getOfferings() {
        return offerings;
    }
    // Method to merge another course into this one
public void mergeCourse(Course otherCourse) {
    if (!this.courseKey.equals(otherCourse.getCourseKey())) {
        throw new IllegalArgumentException("Cannot merge courses with different keys");
    }

    // Iterate through offerings in the other course
    for (Offering otherOffering : otherCourse.getOfferings().values()) {
        for (Section otherSection : otherOffering.getSections().values()) {
            // Use addOfferingsAndSections to add each section to this course
            addOfferingsAndSections(
                otherSection.getCrn(), 
                otherSection.getXlstGroup(),
                otherSection.getSectionCapacity(),
                otherSection.getSectionEnrollment(),
                otherOffering.getOfferingCapacity(),
                otherOffering.getOfferingEnrollment(),
                otherSection.getLink(),
                otherOffering.getInstructor(),
                this.semesterCode
            );
        }
    }
}

    /**
     * Retrieves the set of keys from the offerings map.
     *
     * @return a set of strings representing the keys in the offerings map.
     */
    public Set<String> getKeySet() {
        return offerings.keySet();
    }

    /**
     * Retrieves the key associated with the course.
     *
     * @return the course key as a String
     */
    protected String getCourseKey() {
        return courseKey;
    }

    /**
     * Returns the date associated with this course.
     *
     * @return the date of the course
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Generates a unique key for the course by concatenating the subject and course number.
     *
     * @return A string representing the unique course key.
     */
    private String generateCourseKey() {
        return subject + courseNumber;
    }

    /**
     * Generates a unique offering key by concatenating the provided xlstGroup with the courseKey.
     *
     * @param xlstGroup the group identifier to be concatenated with the course key
     * @return a string representing the unique offering key
     */
    private String generateOfferingKey(String xlstGroup) {
        return xlstGroup + courseKey;
    }

    /**
     * Adds a section to the course.
     *
     * @param crn               The Course Reference Number (CRN) of the section.
     * @param xlstGroup         The cross-list group identifier.
     * @param sectionCapacity   The capacity of the section.
     * @param sectionEnrollment The current enrollment of the section.
     * @param link              The link associated with the section.
     * @param semesterCode      The code of the semester in which the section is offered.
     * @throws IllegalArgumentException if any of the validation checks fail.
     */
    public void addSection(String crn, String xlstGroup, int sectionCapacity, int sectionEnrollment, String link, String semesterCode) {
        ValidationUtils.validateSectionParameters(crn, subject, Integer.toString(courseNumber), link);
        ValidationUtils.validateCapacity(sectionCapacity);
        ValidationUtils.validateEnrollment(sectionEnrollment, sectionCapacity);
        Offering offering = getOrCreateOffering(xlstGroup, 0, 0, "STAFF", semesterCode);
        addSectionToOffering(offering, crn, sectionCapacity, sectionEnrollment, link, xlstGroup, semesterCode);
    }

    /**
     * Retrieves an existing offering or creates a new one if it does not exist.
     *
     * @param xlstGroup The group identifier for the offering.
     * @param offeringCapacity The maximum capacity of the offering.
     * @param offeringEnrollment The current enrollment count of the offering.
     * @param instructor The instructor assigned to the offering.
     * @param semesterCode The code representing the semester for the offering.
     * @return The existing or newly created Offering object.
     */
    private Offering getOrCreateOffering(String xlstGroup, int offeringCapacity, int offeringEnrollment, String instructor, String semesterCode) {
        String offeringKey = generateOfferingKey(xlstGroup);
        return offerings.computeIfAbsent(offeringKey, key -> createNewOffering(key, offeringCapacity, offeringEnrollment, instructor, semesterCode));
    }

    /**
     * Adds a section to the given offering.
     *
     * @param offering the offering to which the section will be added
     * @param crn the course reference number of the section
     * @param sectionCapacity the capacity of the section
     * @param sectionEnrollment the current enrollment of the section
     * @param link the link associated with the section
     * @param xlstGroup the cross-list group of the section
     * @param semesterCode the semester code for the section
     */
    private void addSectionToOffering(Offering offering, String crn, int sectionCapacity, int sectionEnrollment, String link, String xlstGroup, String semesterCode) {
        logger.info(String.format("Adding section with CRN: %s to Offering: %s", crn, offering.getOfferingKey()));
        offering.addOrUpdateSection(crn, subject, courseKey, sectionCapacity, sectionEnrollment, link, xlstGroup);
    }

    /**
     * Creates a new Offering instance with the specified parameters.
     *
     * @param offeringKey the unique key for the offering
     * @param capacity the maximum number of students that can enroll in the offering
     * @param enrollment the current number of students enrolled in the offering
     * @param instructor the name of the instructor for the offering
     * @param semesterCode the code representing the semester for the offering
     * @return a new Offering instance with the provided details
     */
    private Offering createNewOffering(String offeringKey, int capacity, int enrollment, String instructor, String semesterCode) {
        logger.info(String.format("Creating new Offering with key: %s", offeringKey));
        return new Offering(offeringKey, offeringKey.replace(courseKey, ""), capacity, enrollment, instructor, semesterCode);
    }

    /**
     * Calculates the total enrollment across all course offerings.
     *
     * @return the sum of enrollments for all offerings.
     */
    public int getTotalOfferingEnrollment() {
        return offerings.values().stream().mapToInt(Offering::getOfferingEnrollment).sum();
    }

    /**
     * Calculates the total enrollment across all sections of all offerings.
     *
     * @return the total number of students enrolled in all sections
     */
    public int getTotalSectionEnrollment() {
        return offerings.values().stream()
                .flatMap(offering -> offering.getSections().values().stream())
                .mapToInt(Section::getSectionEnrollment)
                .sum();
    }

    /**
     * Calculates the total capacity of all course offerings.
     *
     * @return the sum of the capacities of all offerings.
     */
    public int getTotalOfferingCapacity() {
        return offerings.values().stream()
                .mapToInt(Offering::getOfferingCapacity)
                .sum();
    }

    /**
     * Calculates the total capacity of all sections across all offerings.
     *
     * @return the sum of the capacities of all sections.
     */
    public int getTotalSectionCapacity() {
        return offerings.values().stream()
                .flatMap(offering -> offering.getSections().values().stream())
                .mapToInt(Section::getSectionCapacity)
                .sum();
    }
    /**
     * Retrieves the enrollment number for a specific section identified by the given CRN and cross-list group.
     *
     * @param crn the Course Reference Number (CRN) of the section
     * @param xlstGroup the cross-list group identifier
     * @return the enrollment number of the specified section
     * @throws IllegalArgumentException if the section is not found for the given CRN and cross-list group
     */
    public int getSectionEnrollment(String crn, String xlstGroup) {
        ValidationUtils.validateSectionParameters(crn, subject, Integer.toString(courseNumber), semesterCode);
        String offeringKey = generateOfferingKey(xlstGroup);
        Offering offering = offerings.get(offeringKey);
        if (offering != null) {
            Section section = offering.getSections().get(crn+"-"+semesterCode);
            if (section != null) {
                return section.getSectionEnrollment();
            }
        }
        String errorMessage = String.format("Section not found for CRN: %s in XLST Group: %s", crn, xlstGroup);
        logger.log(Level.SEVERE, errorMessage);
        throw new IllegalArgumentException(errorMessage);
    }

    /**
     * Returns a string representation of the Course object.
     * The string representation includes the subject, course number,
     * number of offerings, date, and details of each offering and its sections.
     *
     * @return a string representation of the Course object
     */
    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "Course{", "}");
        joiner.add("subject='" + subject + "'")
              .add("courseNumber=" + courseNumber)
              .add("offerings=" + offerings.size())
              .add("date=" + date);
        for (Offering offering : offerings.values()) {
            joiner.add(offering.toString());
            for (Section section : offering.getSections().values()) {
                joiner.add(section.toString());
            }
        }
        return joiner.toString();
    }
    /**
     * Compares this Course object with the specified object for equality.
     * Returns true if the specified object is also a Course object and the two objects
     * have the same course key and date.
     *
     * @param o the object to be compared for equality with this Course object
     * @return true if the specified object is equal to this Course object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course that = (Course) o;
        return Objects.equals(courseKey, that.courseKey) && Objects.equals(date, that.date);
    }

    /**
     * Returns the hash code value for this Course object.
     * The hash code is generated based on the course key and date.
     *
     * @return the hash code value for this Course object
     */
    @Override
    public int hashCode() {
        return Objects.hash(courseKey, date);
    }

    /**
     * Compares this Course object with the specified Course object for order.
     * Returns a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     *
     * @param other the Course object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     *         is less than, equal to, or greater than the specified object.
     *         If the specified object is null, this object is considered greater.
     */
    @Override
    public int compareTo(Course other) {
        if (other == null) {
            return 1; // Default behavior: non-null object is greater than null.
        }
        int nameComparison = this.subject.compareTo(other.subject);
        if (nameComparison != 0) {
            return nameComparison;
        }
        return this.date.compareTo(other.date);
    }

    /**
     * Logs all offerings and their respective sections.
     * 
     * This method iterates through all offerings and logs their keys. For each offering,
     * it further iterates through its sections and logs the section key, capacity, and enrollment.
     * 
     * Logging is performed at the INFO level, and the method checks if INFO level logging is enabled
     * before proceeding with the logging operations.
     * 
     * The format for logging section details is defined by the constant SECTION_SUMMARY_FORMAT.
     * 
     * Preconditions:
     * - The logger must be properly initialized.
     * - The offerings map must be populated with valid Offering objects.
     * - Each Offering object must have a populated sections map with valid Section objects.
     * 
     * Postconditions:
     * - Information about all offerings and their sections will be logged if INFO level logging is enabled.
     */
    public void printAllOfferingsAndSections() {
        if (logger.isLoggable(Level.INFO)) {
            for (Map.Entry<String, Offering> offeringEntry : offerings.entrySet()) {
                Offering offering = offeringEntry.getValue();
                logger.info(String.format("Offering: %s", offering.getOfferingKey()));

                for (Map.Entry<String, Section> sectionEntry : offering.getSections().entrySet()) {
                    Section section = sectionEntry.getValue();
                    logger.info(String.format(SECTION_SUMMARY_FORMAT, section.getSectionKey(), section.getSectionCapacity(), section.getSectionEnrollment()));
                }
            }
        }
    }

}
