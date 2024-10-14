package edu.odu.cs.cs350.enp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;

public class TestSnapshot {

    String defaultFileName = "Snapshot File";
    Course course1 = new Course("12345", "CS", "350", 100, 90, "A1", "B1", 200, 180, "2024-10-10");
    Course course2 = new Course("67890", "MATH", "101", 50, 40, "B2", "C3", 100, 80, "2024-10-10");
    Course course3 = new Course("98765", "PHYS", "201", 75, 60, "C1", "D1", 150, 120, "2024-10-10");

    ArrayList<Course> courses = new ArrayList<>();
    Snapshot blank = new Snapshot();

    /**
     * Set up method to initialize the courses list before each test.
     * @throws java.lang.Exception
     */
    @BeforeEach
    public void setUp() throws Exception {
        courses.clear();  // Clear the list to prevent duplicate entries
        courses.add(course1);
        courses.add(course2);
        courses.add(course3);
    }

    /**
     * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#Snapshot()}.
     */
    @Test
    public void testSnapshot() {
        Snapshot snapshot = new Snapshot();
        assertThat(snapshot.getFileName(), equalTo("emptySnapshot"));
        assertThat(snapshot.getCourseList().isEmpty(), is(true));
        assertThat(snapshot, equalTo(blank));
    }

    /**
     * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#Snapshot(String, ArrayList)}.
     */
    @Test
    public void testSnapshotStringArrayList() {
        Snapshot snapshot = new Snapshot(defaultFileName, courses);
        assertThat(snapshot.getFileName(), equalTo(defaultFileName));
        assertThat(snapshot.getCourseList(), hasItems(course1, course2, course3));
        assertThat(snapshot.getCourseList().size(), is(3));
    }

    /**
     * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#setFileName(String)}.
     */
    @Test
    public void testSetFileName() {
        Snapshot snapshot = new Snapshot(defaultFileName, courses);
        Snapshot snapshotClone = snapshot.clone();

        String newFileName = "Updated Snapshot";
        snapshot.setFileName(newFileName);
        assertThat(snapshot.getFileName(), equalTo(newFileName));
        assertThat(snapshot, not(equalTo(snapshotClone)));  // Snapshot name changed, so they're no longer equal
    }

    /**
     * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#addCourse(Course)}.
     */
    @Test
    public void testAddCourse() {
        Snapshot snapshot = new Snapshot(defaultFileName, new ArrayList<>());
        snapshot.addCourse(course1);
        snapshot.addCourse(course2);
        snapshot.addCourse(course3);

        Snapshot snapshotClone = snapshot.clone();

        assertThat(snapshot.getCourseList(), hasItems(course1, course2, course3));
        assertThat(snapshot.getCourseList().size(), is(3));

        snapshot.addCourse(new Course("54321", "HIST", "100", 60, 50, "E1", "F1", 120, 100, "2024-10-10"));
        assertThat(snapshot.getCourseList().size(), is(4));
        assertThat(snapshot, not(equalTo(snapshotClone)));  // Snapshot changed
    }

    /**
     * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#getCourse(String)}.
     */
    @Test
    public void testGetCourse() {
        Snapshot snapshot = new Snapshot(defaultFileName, courses);
        assertThat(snapshot.getCourse("350"), equalTo(course1));
        assertThat(snapshot.getCourse("101"), equalTo(course2));
        assertThat(snapshot.getCourse("201"), equalTo(course3));
        assertThat(snapshot.getCourse("999"), nullValue());  // Non-existent course
    }

    /**
     * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#clone()}.
     */
    @Test
    public void testClone() {
        Snapshot snapshot = new Snapshot(defaultFileName, courses);
        Snapshot snapshotClone = snapshot.clone();

        assertThat(snapshotClone.getFileName(), equalTo(defaultFileName));
        assertThat(snapshotClone.getCourseList(), hasItems(course1, course2, course3));
        assertThat(snapshotClone.getCourseList().size(), is(snapshot.getCourseList().size()));

        // Ensure the clone is equal but not the same instance
        assertThat(snapshotClone, equalTo(snapshot));
        assertThat(snapshotClone, not(sameInstance(snapshot)));
    }
}