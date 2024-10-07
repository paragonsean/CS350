package edu.odu.cs.cs350.enp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.time.LocalDate;
import java.time.LocalDate;
import java.util.ArrayList;

public class TestSnapshot {

    LocalDate date = LocalDate.now();
    Course course1 = new Course("12345", "CS", "350", 100, 90, "A1", "B1", 200, 180);
    Course course2 = new Course("67890", "MATH", "101", 50, 40, "B2", "C3", 100, 80);
    Course course3 = new Course("98765", "PHYS", "201", 75, 60, "C1", "D1", 150, 120);

    ArrayList<Course> courses;
    Snapshot blankSnapshot;

    @BeforeEach
    public void setUp() {
        courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);
        courses.add(course3);

        blankSnapshot = new Snapshot();
    }

    /**
     * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#Snapshot()}.
     */
    @Test
    public void testSnapshot() {
        Snapshot snapshot = new Snapshot();
        assertThat(snapshot.getDate(), is(notNullValue()));  // Default date should be set
        assertThat(snapshot.getCourseList().isEmpty(), is(true));  // Default course list should be empty
        assertThat(snapshot.getCourseList().size(), is(0));  // Ensure course list size is 0
        assertThat(snapshot, equalTo(blankSnapshot));  // Blank snapshots should be equal
    }

    /**
     * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#Snapshot(LocalDate, ArrayList)}.
     */
    @Test
    public void testSnapshotWithDateAndCourseList() {
        Snapshot snapshot = new Snapshot(date, courses);
        assertThat(snapshot.getDate(), equalTo(date));  // Ensure date is set correctly
        assertThat(snapshot.getCourseList(), hasItems(course1, course2, course3));  // All courses should be present
        assertThat(snapshot.getCourseList().size(), is(3));  // Ensure size is 3
    }

    /**
     * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#setDate(LocalDate)}.
     */
    @Test
    public void testSetDate() {
        Snapshot snapshot = new Snapshot(date, courses);
        LocalDate newDate = LocalDate.of(2024, 1, 1);
        snapshot.setDate(newDate);
        assertThat(snapshot.getDate(), equalTo(newDate));  // Ensure date is updated
    }

    /**
     * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#addCourse(Course)}.
     */
    @Test
    public void testAddCourse() {
        Snapshot snapshot = new Snapshot(date,courses);
        snapshot.addCourse(course1);
        snapshot.addCourse(course2);

        assertThat(snapshot.getCourseList(), hasItems(course1, course2));  // Added courses should be present
        assertThat(snapshot.getCourseList().size(), is(2));  // Ensure the size is correct

        // Add another course
        Course newCourse = new Course("54321", "HIST", "100", 60, 50, "E1", "F1", 120, 100);
        snapshot.addCourse(newCourse);
        assertThat(snapshot.getCourseList().size(), is(3));  // Size should now be 3
        assertThat(snapshot.getCourseList(), hasItem(newCourse));  // Ensure new course is added
    }

    /**
     * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#getCourse(String)}.
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
     */
    @Test
    public void testToString() {
        Snapshot snapshot = new Snapshot(date, courses);
        String expectedString = "Snapshot Date: " + date.toString() + "\nCourses:\n" +
                "   " + course1.toString() + "\n" +
                "   " + course2.toString() + "\n" +
                "   " + course3.toString() + "\n";

        assertThat(snapshot.toString(), equalTo(expectedString));  // Ensure toString() matches expected output
    }
}