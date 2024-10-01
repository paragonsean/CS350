package edu.odu.cs.cs350;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TestSnapshot {

  String defaultFileName = "Snapshot File";
  Course course1 = new Course("12345", "CS", "350", 100, 90, "A1", "B1", 200, 180, "2024-10-10");
  Course course2 = new Course("67890", "MATH", "101", 50, 40, "B2", "C3", 100, 80, "2024-10-10");
  Course course3 = new Course("98765", "PHYS", "201", 75, 60, "C1", "D1", 150, 120, "2024-10-10");

  ArrayList<Course> courses = new ArrayList<>();
  Snapshot blank = new Snapshot();

  /**
   * @throws java.lang.Exception
   */
  @BeforeEach
  public void setUp() throws Exception {
      courses.add(course1);
      courses.add(course2);
      courses.add(course3);
  }

  /**
   * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#Snapshot()}.
   */
  @Test
  public void testSnapshot() {
      Snapshot Snapshot = new Snapshot();
      assertThat(Snapshot.getFileName(), equalTo("emptySnapshot"));
      assertThat(Snapshot.getCourseList().isEmpty(), is(true));
      assertThat(Snapshot, equalTo(blank));
  }

  /**
   * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#Snapshot(String, ArrayList)}.
   */
  @Test
  public void testSnapshotStringArrayList() {
      Snapshot Snapshot = new Snapshot(defaultFileName, courses);
      assertThat(Snapshot.getFileName(), equalTo(defaultFileName));
      assertThat(Snapshot.getCourseList(), hasItems(course1, course2, course3));
      assertThat(Snapshot.getCourseList().size(), is(3));
  }

  /**
   * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#setFileName(String)}.
   */
  @Test
  public void testSetFileName() {
      Snapshot Snapshot = new Snapshot(defaultFileName, courses);
      Snapshot SnapshotClone = Snapshot.clone();

      String newFileName = "Updated Snapshot";
      Snapshot.setFileName(newFileName);
      assertThat(Snapshot.getFileName(), equalTo(newFileName));
      assertThat(Snapshot, not(equalTo(SnapshotClone)));  // Snapshot name changed, so they're no longer equal
  }

  /**
   * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#addCourse(Course)}.
   */
  @Test
  public void testAddCourse() {
      Snapshot Snapshot = new Snapshot(defaultFileName, new ArrayList<>());
      Snapshot.addCourse(course1);
      Snapshot.addCourse(course2);
      Snapshot.addCourse(course3);

      Snapshot SnapshotClone = Snapshot.clone();

      assertThat(Snapshot.getCourseList(), hasItems(course1, course2, course3));
      assertThat(Snapshot.getCourseList().size(), is(3));

      Snapshot.addCourse(new Course("54321", "HIST", "100", 60, 50, "E1", "F1", 120, 100, "2024-10-10"));
      assertThat(Snapshot.getCourseList().size(), is(4));
      assertThat(Snapshot, not(equalTo(SnapshotClone)));  // Snapshot changed
  }

  /**
   * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#getCourse(String)}.
   */
  @Test
  public void testGetCourse() {
      Snapshot Snapshot = new Snapshot(defaultFileName, courses);
      assertThat(Snapshot.getCourse("350"), equalTo(course1));
      assertThat(Snapshot.getCourse("101"), equalTo(course2));
      assertThat(Snapshot.getCourse("201"), equalTo(course3));
      assertThat(Snapshot.getCourse("999"), nullValue());  // Non-existent course
  }

  /**
   * Test method for {@link edu.odu.cs.cs350.enp.Snapshot#clone()}.
   */
  @Test
  public void testClone() {
      Snapshot Snapshot = new Snapshot(defaultFileName, courses);
      Snapshot SnapshotClone = Snapshot.clone();

      assertThat(SnapshotClone.getFileName(), equalTo(defaultFileName));
      assertThat(SnapshotClone.getCourseList(), hasItems(course1, course2, course3));
      assertThat(SnapshotClone.getCourseList().size(), is(Snapshot.getCourseList().size()));

      // Verify all fields match
      assertThat(SnapshotClone, equalTo(Snapshot));
  }
}