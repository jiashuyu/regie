package org.uchicago.regie.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class StudentEntityTest {

    @Test
    public void testStudentEntityProperties() {
        Long id = 1L;
        String email = "student@example.com";
        boolean enabled = true;
        String password = "securepassword";
        String firstName = "John";
        String lastName = "Doe";

        StudentEntity student = new StudentEntity(id, email, enabled, password, firstName, lastName);

        assertEquals(id, student.id(), "The id should match the value provided to the constructor.");
        assertEquals(email, student.email(), "The email should match the value provided to the constructor.");
        assertEquals(enabled, student.enabled(), "The enabled flag should match the value provided to the constructor.");
        assertEquals(password, student.password(), "The password should match the value provided to the constructor.");
        assertEquals(firstName, student.firstName(), "The firstName should match the value provided to the constructor.");
        assertEquals(lastName, student.lastName(), "The lastName should match the value provided to the constructor.");
    }

    @Test
    public void testEquality() {
        StudentEntity student1 = new StudentEntity(1L, "student@example.com", true, "securepassword", "John", "Doe");
        StudentEntity student2 = new StudentEntity(1L, "student@example.com", true, "securepassword", "John", "Doe");

        assertEquals(student1, student2, "Two StudentEntity objects with the same property values should be considered equal.");
    }

    @Test
    public void testInequalityByDifferentEmail() {
        StudentEntity student1 = new StudentEntity(1L, "student@example.com", true, "securepassword", "John", "Doe");
        StudentEntity student2 = new StudentEntity(1L, "different@example.com", true, "securepassword", "John", "Doe");

        assertNotEquals(student1, student2, "Two StudentEntity objects with different emails should not be considered equal.");
    }
}

