package org.uchicago.regie.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CourseLoadEntityTest {

    @Test
    public void testRecordProperties() {
        Long studentId = 1L;
        int currentLoad = 3;
        int maxLoad = 5;

        CourseLoadEntity courseLoad = new CourseLoadEntity(studentId, currentLoad, maxLoad);

        assertEquals(studentId, courseLoad.studentId(), "Student ID should match the constructor argument.");
        assertEquals(currentLoad, courseLoad.currentLoad(), "Current load should match the constructor argument.");
        assertEquals(maxLoad, courseLoad.maxLoad(), "Max load should match the constructor argument.");
    }

    @Test
    public void testLoadLimits() {
        CourseLoadEntity courseLoad = new CourseLoadEntity(1L, 3, 5);

        assertTrue(courseLoad.currentLoad() <= courseLoad.maxLoad(), "Current load should not exceed max load.");
    }

}
