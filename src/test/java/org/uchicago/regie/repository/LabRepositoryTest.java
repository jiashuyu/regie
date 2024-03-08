package org.uchicago.regie.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.uchicago.regie.model.LabEntity;
import org.uchicago.regie.model.CourseEntity; // Make sure to import your CourseEntity
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LabRepositoryTest {

    @Autowired
    private LabRepository labRepository;

    @Autowired
    private CourseRepository courseRepository;

    private CourseEntity testCourse;

    @BeforeEach
    void setUp() {
        // Set up a test course to use with labs
        testCourse = new CourseEntity(null, 1L, "CS101", "Introduction to Computer Science", 100);
        testCourse = courseRepository.save(testCourse);
    }

    @Test
    public void testFindByCourseId() {
        LabEntity lab = new LabEntity(null, testCourse.id(), "Lab 1");
        lab = labRepository.save(lab);

        Optional<LabEntity> foundLab = labRepository.findByCourseId(testCourse.id());
        assertTrue(foundLab.isPresent(), "Should find a lab by course ID.");
        assertEquals("Lab 1", foundLab.get().labNumber(), "The lab number should match.");
    }

    @Test
    public void testFindAllByDepartmentId() {
        List<LabEntity> labs = labRepository.findAllByDepartmentId(testCourse.departmentId());
        assertNotNull(labs, "Should not return null.");
    }

    @Test
    public void testFindByLabNumber() {
        LabEntity lab = new LabEntity(null, testCourse.id(), "Lab 1");
        labRepository.save(lab);

        List<LabEntity> labs = labRepository.findByLabNumber("Lab 1");
        assertFalse(labs.isEmpty(), "Should find labs by lab number.");
        assertEquals(1, labs.size(), "Should find exactly one lab.");
        assertEquals(testCourse.id(), labs.get(0).courseId(), "The course ID should match.");
    }

    @Test
    public void testCountByCourseId() {
        LabEntity lab = new LabEntity(null, testCourse.id(), "Lab 1");
        labRepository.save(lab);

        int count = labRepository.countByCourseId(testCourse.id());
        assertEquals(1, count, "Should count the number of labs associated with a course.");
    }

    @Test
    public void testFindAllByOrderByLabNumberAsc() {
        LabEntity lab1 = new LabEntity(null, testCourse.id(), "Lab 1");
        LabEntity lab2 = new LabEntity(null, testCourse.id(), "Lab 2");
        labRepository.save(lab1);
        labRepository.save(lab2);

        List<LabEntity> labs = labRepository.findAllByOrderByLabNumberAsc();
        assertNotNull(labs, "Should not return null.");
        assertTrue(labs.size() >= 2, "Should find at least two labs.");
        assertTrue(labs.get(0).labNumber().compareTo(labs.get(1).labNumber()) < 0, "Labs should be ordered by lab number ascending.");
    }
}
