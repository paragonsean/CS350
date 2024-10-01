package edu.odu.cs.cs350.enp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TestSnapshot {

    LocalDate date = LocalDateTime.now().toLocalDate();
    Course course1 = new Course("12345", "CS", "350", 100, 90, "A1", "B1", 200, 180);
    Course course2 = new Course("67890", "MATH", "101", 50, 40, "B2", "C3", 100, 80);
    Course course3 = new Course("98765", "PHYS", "201", 75, 60, "C1", "D1", 150, 120);

    ArrayList<Course> courses = new ArrayList<>();
    Snapshot blank = new Snapshot();

    @BeforeEach
    public void setUp() throws Exception {
        courses.clear();
        courses.add(course1);
        courses.add(course2);
        courses.add(course3);
    }

    /**
     * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#Snapshot()}.
     * This tests the default constructor and ensures the initial state is correct.
     */
    @Test
    public void testSnapshot() {
        Snapshot snapshot = new Snapshot();
        assertThat(snapshot.getDate(), is(notNullValue()));  // Snapshot default date should be set
        assertThat(snapshot.getCourseList().isEmpty(), is(true));  // Default snapshot has an empty course list
        assertThat(snapshot.getCourseList().size(), is(0));  // Course list size should be 0
        assertThat(snapshot, equalTo(blank));  // Compare blank snapshot
    }

    /**
     * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#Snapshot(LocalDate, ArrayList)}.
     * This tests the constructor with date and course list parameters.
     */
    @Test
    public void testSnapshotWithDateAndCourseList() {
        Snapshot snapshot = new Snapshot(date, courses);
        assertThat(snapshot.getDate(), equalTo(date));  // Check if the date is set correctly
        assertThat(snapshot.getCourseList(), hasItems(course1, course2, course3));  // Check if all courses are present
        assertThat(snapshot.getCourseList().size(), is(3));  // Ensure the size is correct
    }

    /**
     * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#setDate(LocalDate)}.
     * This tests if the date is correctly set after being changed.
     */
    @Test
    public void testSetDate() {
        Snapshot snapshot = new Snapshot(date, courses);

        LocalDate newDate = LocalDateTime.now().toLocalDate();
        snapshot.setDate(newDate);
        assertThat(snapshot.getDate(), equalTo(newDate));  // Ensure the date is updated correctly
    }

    /**
     * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#addCourse(Course)}.
     * This tests adding courses to a snapshot.
     */
    @Test
    public void testAddCourse() {
        Snapshot snapshot = new Snapshot(date);
        snapshot.addCourse(course1);
        snapshot.addCourse(course2);
        snapshot.addCourse(course3);

        assertThat(snapshot.getCourseList(), hasItems(course1, course2, course3));  // Check if all courses are added
        assertThat(snapshot.getCourseList().size(), is(3));  // Ensure the size is correct

        // Add another course
        Course newCourse = new Course("54321", "HIST", "100", 60, 50, "E1", "F1", 120, 100);
        snapshot.addCourse(newCourse);
        assertThat(snapshot.getCourseList().size(), is(4));  // Size should now be 4
        assertThat(snapshot.getCourseList(), hasItem(newCourse));  // Ensure new course is added
    }

    /**
     * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#getCourse(String)}.
     * This tests retrieving a course by its course number (CRSE).
     */
    @Test
    public void testGetCourse() {
        Snapshot snapshot = new Snapshot(date, courses);
        assertThat(snapshot.getCourse("350"), equalTo(course1));  // Ensure correct course is returned
        assertThat(snapshot.getCourse("101"), equalTo(course2));  // Ensure correct course is returned
        assertThat(snapshot.getCourse("201"), equalTo(course3));  // Ensure correct course is returned
        assertThat(snapshot.getCourse("999"), nullValue());  // Non-existent course should return null
    }

    /**
     * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#toString()}.
     * This tests the string representation of the snapshot.
     */
  
}