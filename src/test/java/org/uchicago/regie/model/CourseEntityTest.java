package org.uchicago.regie.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CourseEntityTest {

    @Test
    public void testRecordEquality() {
        CourseEntity course1 = new CourseEntity(1L, 1L, "101", "Introduction to Testing", 30);
        CourseEntity course2 = new CourseEntity(1L, 1L, "101", "Introduction to Testing", 30);

        assertEquals(course1, course2, "CourseEntity records with the same data should be equal.");
    }

    @Test
    public void testRecordInequalityByDifferentId() {
        CourseEntity course1 = new CourseEntity(1L, 1L, "101", "Introduction to Testing", 30);
        CourseEntity course2 = new CourseEntity(2L, 1L, "101", "Introduction to Testing", 30);

        assertNotEquals(course1, course2, "CourseEntity records with different IDs should not be equal.");
    }

    @Test
    public void testToStringContainsName() {
        CourseEntity course = new CourseEntity(1L, 1L, "101", "Introduction to Testing", 30);
        String courseString = course.toString();

        assert(courseString.contains("Introduction to Testing"));
    }

}
