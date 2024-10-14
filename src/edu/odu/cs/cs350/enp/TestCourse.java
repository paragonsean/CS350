package edu.odu.cs.cs350.enp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
public class TestCourse {

    Course course1;
    Course course2;
    Course course3;

    @BeforeEach
    public void setUp() {
        course1 = new Course("12345", "CS", "350", 100, 90, "A1", "B1", 200, 180, "2024-10-10");
        course2 = new Course("67890", "MATH", "101", 50, 40, "B2", "C3", 100, 80, "2024-10-10");
        course3 = new Course("12345", "CS", "350", 100, 90, "A1", "B1", 200, 180, "2024-10-10");  // Same as course1
    }

    /**
     * Test method for {@link edu.edu.odu.cs.cs350.enp.Course#Course(String, String, String, int, int, String, String, int, int, String)}.
     */
    @Test
    public void testCourseConstructor() {
        assertThat(course1.getCRN(), equalTo("12345"));
        assertThat(course1.getSUBJ(), equalTo("CS"));
        assertThat(course1.getCRSE(), equalTo("350"));
        assertThat(course1.getXLST_CAP(), is(100));
        assertThat(course1.getENR(), is(90));
        assertThat(course1.getLINK(), equalTo("A1"));
        assertThat(course1.getXLST_GROUP(), equalTo("B1"));
        assertThat(course1.getOVERALL_CAP(), is(200));
        assertThat(course1.getOVERALL_ENR(), is(180));
        assertThat(course1.getSnapshotDate(), equalTo("2024-10-10"));
    }

    /**
     * Test method for {@link edu.edu.odu.cs.cs350.enp.Course#equals(Object)}.
     */
    @Test
    public void testEquals() {
        // course1 and course3 should be equal since they have the same data
        assertThat(course1, equalTo(course3));
        // course1 and course2 should not be equal since they have different data
        assertThat(course1, not(equalTo(course2)));
    }

    /**
     * Test method for {@link edu.edu.odu.cs.cs350.enp.Course#hashCode()}.
     */
    @Test
    public void testHashCode() {
        // If two objects are equal, their hash codes should also be equal
        assertThat(course1.hashCode(), equalTo(course3.hashCode()));
        // If two objects are not equal, their hash codes should ideally not be equal
        assertThat(course1.hashCode(), not(equalTo(course2.hashCode())));
    }

    /**
     * Test method for {@link edu.edu.odu.cs.cs350.enp.Course#clone()}.
     */
    @Test
    public void testClone() {
        Course clonedCourse = course1.clone();
        // The clone should be equal to the original
        assertThat(clonedCourse, equalTo(course1));
        // But the clone should not be the same object reference
        assertNotSame(course1, clonedCourse);

        // The fields of the cloned object should match those of the original
        assertThat(clonedCourse.getCRN(), equalTo(course1.getCRN()));
        assertThat(clonedCourse.getSUBJ(), equalTo(course1.getSUBJ()));
        assertThat(clonedCourse.getCRSE(), equalTo(course1.getCRSE()));
        assertThat(clonedCourse.getXLST_CAP(), is(course1.getXLST_CAP()));
        assertThat(clonedCourse.getENR(), is(course1.getENR()));
        assertThat(clonedCourse.getLINK(), equalTo(course1.getLINK()));
        assertThat(clonedCourse.getXLST_GROUP(), equalTo(course1.getXLST_GROUP()));
        assertThat(clonedCourse.getOVERALL_CAP(), is(course1.getOVERALL_CAP()));
        assertThat(clonedCourse.getOVERALL_ENR(), is(course1.getOVERALL_ENR()));
        assertThat(clonedCourse.getSnapshotDate(), equalTo(course1.getSnapshotDate()));
    }

    /**
     * Test method for {@link edu.edu.odu.cs.cs350.enp.Course#toString()}.
     */
    @Test
    public void testToString() {
        String expectedString = "Course{CRN='12345', SUBJ='CS', CRSE='350', XLST_CAP=100, ENR=90, LINK='A1', XLST_GROUP='B1', OVERALL_CAP=200, OVERALL_ENR=180, snapshotDate='2024-10-10'}";
        assertThat(course1.toString(), equalTo(expectedString));
    }
}