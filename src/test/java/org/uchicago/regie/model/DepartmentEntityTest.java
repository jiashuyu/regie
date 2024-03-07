package org.uchicago.regie.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DepartmentEntityTest {

    @Test
    public void testDepartmentEntityProperties() {
        Long id = 10L;
        String name = "Computer Science";
        String chairEmail = "chair@cs.department.university.edu";

        DepartmentEntity department = new DepartmentEntity(id, name, chairEmail);

        assertEquals(id, department.id(), "The id should match the value provided to the constructor.");
        assertEquals(name, department.name(), "The name should match the value provided to the constructor.");
        assertEquals(chairEmail, department.chairEmail(), "The chairEmail should match the value provided to the constructor.");
    }

    // Example of a hypothetical test if there were custom logic to validate the chair's email format
    @Test
    public void testChairEmailFormat() {
        String chairEmail = "chair@cs.department.university.edu";
        DepartmentEntity department = new DepartmentEntity(1L, "Computer Science", chairEmail);

        assertTrue(department.chairEmail().contains("@"), "The chair's email should contain '@' character.");
        assertTrue(department.chairEmail().contains("."), "The chair's email should contain '.' character.");
    }
}

