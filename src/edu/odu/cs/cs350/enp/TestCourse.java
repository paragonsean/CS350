package edu.odu.cs.cs350.enp;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TestCourse {

    private Course course;
    private Section section1;
    private Section section2;
    private Section sectionZeroEnrollment;
    private Section sectionZeroCapacity;

    @Before
    public void setUp() {
        course = new Course("Software Engineering", "CS", 350, "CS350");
        section1 = new Section(1, 30, 25);  // section 1 with capacity 30 and enrolled 25
        section2 = new Section(2, 40, 35);  // section 2 with capacity 40 and enrolled 35
        sectionZeroEnrollment = new Section(3, 50, 0);  // section with 0 enrolled students
        sectionZeroCapacity = new Section(4, 0, 0);  // section with 0 capacity
    }

    @Test
    public void testConstructor() {
        assertThat(course.getTitle(), is("Software Engineering"));
        assertThat(course.getSubject(), is("CS"));
        assertThat(course.getCourseNumber(), is(350));
        assertThat(course.getCrossListGroup(), is("CS350"));
        assertThat(course.getOverallEnrollment(), is(0));
        assertThat(course.getOverallCapacity(), is(0));
        assertThat(course.getNumSections(), is(0));
    }

    @Test
    public void testAddSection() {
        course.addSection(section1);
        course.addSection(section2);

        assertThat(course.getNumSections(), is(2));
        assertThat(course.getOverallEnrollment(), is(60));  // 25 + 35
        assertThat(course.getOverallCapacity(), is(70));    // 30 + 40
    }

    @Test
    public void testGetSections() {
        course.addSection(section1);
        course.addSection(section2);

        List<Section> sections = course.getSections();
        assertThat(sections.size(), is(2));
        assertThat(sections.get(0), is(section1));
        assertThat(sections.get(1), is(section2));

        // Ensure that modifying the returned list does not affect the course's internal list
        sections.clear();
        assertThat(course.getNumSections(), is(2));
    }

    @Test
    public void testToString() {
        course.addSection(section1);
        course.addSection(section2);

        String expectedString = "CS 350: Software Engineering (Enrollment: 60, Capacity: 70, Sections: 2)";
        assertThat(course.toString(), is(expectedString));
    }

    @Test
    public void testAddSectionWithZeroEnrollment() {
        course.addSection(sectionZeroEnrollment);

        assertThat(course.getNumSections(), is(1));
        assertThat(course.getOverallEnrollment(), is(0));  // No students enrolled
        assertThat(course.getOverallCapacity(), is(50));   // Capacity of 50
    }

    @Test
    public void testAddSectionWithZeroCapacity() {
        course.addSection(sectionZeroCapacity);

        assertThat(course.getNumSections(), is(1));
        assertThat(course.getOverallEnrollment(), is(0));  // No students enrolled
        assertThat(course.getOverallCapacity(), is(0));    // Capacity of 0
    }

    @Test
    public void testAddMultipleSectionsIncludingZeroCapacity() {
        course.addSection(section1);
        course.addSection(sectionZeroCapacity);
        course.addSection(section2);

        assertThat(course.getNumSections(), is(3));
        assertThat(course.getOverallEnrollment(), is(60));  // 25 + 35 (from valid sections)
        assertThat(course.getOverallCapacity(), is(70));    // 30 + 40 (ignoring zero-capacity section)
    }

    @Test
    public void testIterator() {
        course.addSection(section1);
        course.addSection(section2);

        Iterator<Section> iterator = course.iterator();
        assertTrue(iterator.hasNext());
        assertThat(iterator.next(), is(section1));
        assertTrue(iterator.hasNext());
        assertThat(iterator.next(), is(section2));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testCrossListGroup() {
        assertThat(course.getCrossListGroup(), is("CS350"));

        Course crossListedCourse = new Course("Data Structures", "CS", 361, "CS361");
        assertThat(crossListedCourse.getCrossListGroup(), is("CS361"));
    }
}
