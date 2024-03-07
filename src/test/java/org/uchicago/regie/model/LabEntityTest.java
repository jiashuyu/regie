package org.uchicago.regie.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class LabEntityTest {

    @Test
    public void testRecordEquality() {
        LabEntity lab1 = new LabEntity(1L, 1L, "Lab 101");
        LabEntity lab2 = new LabEntity(1L, 1L, "Lab 101");

        assertEquals(lab1, lab2, "LabEntity records with the same data should be equal.");
    }

    @Test
    public void testRecordInequalityByDifferentId() {
        LabEntity lab1 = new LabEntity(1L, 1L, "Lab 101");
        LabEntity lab2 = new LabEntity(2L, 1L, "Lab 101");

        assertNotEquals(lab1, lab2, "LabEntity records with different IDs should not be equal.");
    }

    @Test
    public void testRecordInequalityByDifferentCourseId() {
        LabEntity lab1 = new LabEntity(1L, 1L, "Lab 101");
        LabEntity lab2 = new LabEntity(1L, 2L, "Lab 101");

        assertNotEquals(lab1, lab2, "LabEntity records with different course IDs should not be equal.");
    }

    @Test
    public void testToStringContainsLabNumber() {
        LabEntity lab = new LabEntity(1L, 1L, "Lab 101");
        String labString = lab.toString();

        assert(labString.contains("Lab 101"));
    }
}
