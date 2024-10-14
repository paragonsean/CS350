package edu.odu.cs.cs350.enp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestHelper {

    
    public static Course createRandomCourse() {
        Random random = new Random();
        String crn = String.valueOf(10000 + random.nextInt(90000));
        String[] subjects = {"CS", "MATH", "PHYS", "CHEM", "BIO"};
        String subj = subjects[random.nextInt(subjects.length)];
        String crse = String.valueOf(100 + random.nextInt(900));
        int xlstCap = 50 + random.nextInt(150);
        int enr = random.nextInt(xlstCap);
        String link = "L" + random.nextInt(10);
        String xlstGroup = "G" + random.nextInt(10);
        int overallCap = xlstCap + random.nextInt(100);
        int overallEnr = enr + random.nextInt(overallCap - xlstCap + 1);
        String snapshotDate = "2024-" + (10 + random.nextInt(3)) + "-" + (10 + random.nextInt(21));
        
        return new Course(crn, subj, crse, xlstCap, enr, link, xlstGroup, overallCap, overallEnr, snapshotDate);
    }

    public static Snapshot createSnapshot(String fileName, List<Course> courses) {
        return new Snapshot(fileName, new ArrayList<>(courses)); // Clone the list to avoid mutation issues
    }

    public static ArrayList<Course> setupDefaultCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(createCourse("12345", "CS", "350", 100, 90, "A1", "B1", 200, 180, "2024-10-10"));
        courses.add(createCourse("67890", "MATH", "101", 50, 40, "B2", "C3", 100, 80, "2024-10-10"));
        courses.add(createCourse("98765", "PHYS", "201", 75, 60, "C1", "D1", 150, 120, "2024-10-10"));
        return courses;
    }

    // Verifies all fields in a Course object
    public static void assertCourseFields(Course course, String crn, String subj, String crse, int xlstCap, int enr, String link, String xlstGroup, int overallCap, int overallEnr, String snapshotDate) {
        assertThat(course.getCRN(), equalTo(crn));
        assertThat(course.getSUBJ(), equalTo(subj));
        assertThat(course.getCRSE(), equalTo(crse));
        assertThat(course.getXLST_CAP(), is(xlstCap));
        assertThat(course.getENR(), is(enr));
        assertThat(course.getLINK(), equalTo(link));
        assertThat(course.getXLST_GROUP(), equalTo(xlstGroup));
        assertThat(course.getOVERALL_CAP(), is(overallCap));
        assertThat(course.getOVERALL_ENR(), is(overallEnr));
        assertThat(course.getSnapshotDate(), equalTo(snapshotDate));
    }

    // Verifies that two Snapshots or Courses are deeply equal
    public static void assertSnapshotEquality(Snapshot snapshot1, Snapshot snapshot2) {
        assertThat(snapshot1.getFileName(), equalTo(snapshot2.getFileName()));
        assertThat(snapshot1.getCourseList().size(), equalTo(snapshot2.getCourseList().size()));
        assertThat(snapshot1.getCourseList(), containsInAnyOrder(snapshot2.getCourseList().toArray()));
    }

    public static void assertCoursesEqual(Course course1, Course course2) {
        assertThat(course1, equalTo(course2));
        assertThat(course1.hashCode(), equalTo(course2.hashCode()));
    }

    public static void assertSnapshotCourses(List<Course> courses, Course... expectedCourses) {
        assertThat(courses, hasItems(expectedCourses));
        assertThat(courses.size(), is(expectedCourses.length));
    }
}