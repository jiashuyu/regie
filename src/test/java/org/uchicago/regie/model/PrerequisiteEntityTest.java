package org.uchicago.regie.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PrerequisiteEntityTest {

    @Test
    public void testPrerequisiteEntityProperties() {
        Long id = 1L;
        Long courseId = 101L;
        Long prerequisiteId = 100L;

        PrerequisiteEntity prerequisite = new PrerequisiteEntity(id, courseId, prerequisiteId);

        assertEquals(id, prerequisite.id(), "The id should match the value provided to the constructor.");
        assertEquals(courseId, prerequisite.courseId(), "The courseId should match the value provided to the constructor.");
        assertEquals(prerequisiteId, prerequisite.prerequisiteId(), "The prerequisiteId should match the value provided to the constructor.");
    }

    @Test
    public void testEquality() {
        PrerequisiteEntity prerequisite1 = new PrerequisiteEntity(1L, 101L, 100L);
        PrerequisiteEntity prerequisite2 = new PrerequisiteEntity(1L, 101L, 100L);

        assertEquals(prerequisite1, prerequisite2, "Two PrerequisiteEntity objects with the same id, courseId, and prerequisiteId should be equal.");
    }

    @Test
    public void testInequalityByDifferentId() {
        PrerequisiteEntity prerequisite1 = new PrerequisiteEntity(1L, 101L, 100L);
        PrerequisiteEntity prerequisite2 = new PrerequisiteEntity(2L, 101L, 100L);

        assertNotEquals(prerequisite1, prerequisite2, "Two PrerequisiteEntity objects with different ids should not be equal.");
    }
}

