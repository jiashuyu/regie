package org.uchicago.regie.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PrerequisiteRepositoryTest {

    @Autowired
    private PrerequisiteRepository prerequisiteRepository;

    @Test
    public void testFindPrerequisitesByCourseId() {
        Long courseId = 2L;

        List<Long> prerequisites = prerequisiteRepository.findPrerequisitesByCourseId(courseId);
        assertFalse(prerequisites.isEmpty(), "Should find prerequisites for the given course ID.");
    }

    @Test
    public void testFindCoursesForPrerequisite() {
        Long prerequisiteId = 1L;

        List<Long> courses = prerequisiteRepository.findCoursesForPrerequisite(prerequisiteId);
        assertFalse(courses.isEmpty(), "Should find courses for which the given course is a prerequisite.");
    }

    @Test
    public void testIsPrerequisiteForCourse() {
        Long courseId = 2L;
        Long prerequisiteId = 1L;

        boolean result = prerequisiteRepository.isPrerequisiteForCourse(courseId, prerequisiteId);
        assertTrue(result, "The course should be a prerequisite for another course.");
    }

}
