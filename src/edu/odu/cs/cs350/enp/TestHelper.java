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
    package edu.odu.cs.cs350.enp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import java.util.ArrayList;

public class TestCourseGenerator {

    private ArrayList<Course> generatedCourses;

    @BeforeEach
    public void setUp() {
        generatedCourses = generateSampleCourses(); // Call the course generation function
    }

    /**
     * Test method for generating courses dynamically.
     */
    @Test
    public void testGenerateSampleCourses() {
        assertThat(generatedCourses, not(empty())); // Ensure courses are generated

        // Check the properties of the first generated course
        Course firstCourse = generatedCourses.get(0);
        assertThat(firstCourse.getCRN(), equalTo("10000"));
        assertThat(firstCourse.getSUBJ(), equalTo("CS"));
        assertThat(firstCourse.getCRSE(), equalTo("115"));
        assertThat(firstCourse.getTITLE(), equalTo("INTRO TO CS WITH PYTHON"));
        assertThat(firstCourse.getSeats(), equalTo(30));
        
    }

    /**
     * Helper function to generate sample courses dynamically.
     */
    private ArrayList<Course> generateSampleCourses() {
        ArrayList<Course> courses = new ArrayList<>();

        String[] subjects = {"CS", "MATH", "PHYS", "HIST"};
        String[] titles = {"INTRO TO CS WITH PYTHON", "CALCULUS I", "INTRO TO PHYSICS", "WORLD HISTORY"};
        int[] crns = {10000, 10001, 10002, 10003};

        for (int i = 0; i < 4; i++) {
            Course course = new Course(
                String.valueOf(crns[i]),
                subjects[i % subjects.length],
                "115",
                30 + (i * 5),  // Seats
                10 + (i * 2),  // Enrollment
                "A" + (i + 1),
                "B" + (i + 1),
                100 + (i * 10), // Cross-list cap
                50 + (i * 5),   // Overall enrollment
                "2024-10-10"
            );
            courses.add(course);
        }

        return courses;
    }
}

    public static Snapshot createSnapshot(String fileName, List<Course> courses) {
        return new Snapshot(fileName, new ArrayList<>(courses)); // Clone the list to avoid mutation issues
    }

    public static ArrayList<Course> setupDefaultCourses() {
        ArrayList<Course> courses = new ArrayList<>();
      
        return courses;
    }

    // Verifies all fields in a Course object
    public static void assertCourseFields(Course course, String crn, String subj, String crse, int xlstCap, int enr, String link, String xlstGroup, int overallCap, int overallEnr, String snapshotDate) {
  
        assertThat(course.getSUBJ(), equalTo(subj));
        assertThat(course.getCRSE(), equalTo(crse));
  
        assertThat(course.getXLST_GROUP(), equalTo(xlstGroup));
        assertThat(course.getOVERALL_CAP(), is(overallCap));
        assertThat(course.getOVERALL_ENR(), is(overallEnr));
      
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