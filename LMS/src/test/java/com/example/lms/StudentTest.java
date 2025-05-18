package com.example.lms;

import com.example.lms.dtos.StudentDTO;
import com.example.lms.models.CourseModel;
import com.example.lms.models.StudentModel;
import com.example.lms.repositories.CourseRepository;
import com.example.lms.repositories.StudentRepository;
import com.example.lms.services.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // Enables Mockito for this test class
 class StudentTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private StudentService studentService;

    private StudentModel student;
    private CourseModel course;


    @BeforeEach
    void setUp() {
        student = new StudentModel(1, "john.doe@example.com", "John Doe");
        student.setCourses(new ArrayList<>());  // Initialize the courses list

        course = new CourseModel("CS101", "Computer Science 101", "Intro to CS", 40, null, null);
        course.setStudents(new ArrayList<>());  // Initialize the students list
    }





    @Test
    void testGetAllStudents() {
        // Arrange
        List<StudentModel> mockStudents = Collections.singletonList(student);
        when(studentRepository.findAll()).thenReturn(mockStudents);

        // Act
        List<StudentDTO> result = studentService.getAllStudents();

        // Assert
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName()); // Expected value matches the name
        assertEquals("john.doe@example.com", result.get(0).getEmail()); // Expected email matches
    }

    @Test
    void testGetStudentById() {
        StudentModel mockStudent = new StudentModel();
        mockStudent.setId(1);
        mockStudent.setName("Alice");

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        StudentModel result = studentService.getStudentById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Alice", result.getName());

        verify(studentRepository, times(1)).findById(1);
    }




    @Test
    void testEnrollStudent() {
        // Mock the repositories to return student and course
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        studentService.enrollStudent(1, 1L);  // Call the service method

        // Verify that the student was added to the course's students list
        assertTrue(student.getCourses().contains(course));
        assertTrue(course.getStudents().contains(student));

        verify(studentRepository, times(1)).save(student);
        verify(courseRepository, times(1)).save(course);
    }
    @Test
    void testEnrollStudent_CourseAlreadyEnrolled() {
        CourseModel existingCourse = new CourseModel();
        existingCourse.setId(100L);
        existingCourse.setCourseId("Math");

        StudentModel existingStudent = new StudentModel();
        existingStudent.setId(1);
        existingStudent.setName("Alice");
        existingStudent.setCourses(Collections.singletonList(existingCourse));

        when(studentRepository.findById(1)).thenReturn(Optional.of(existingStudent));
        when(courseRepository.findById(100L)).thenReturn(Optional.of(existingCourse));

        StudentModel result = studentService.enrollStudent(1, 100L);

        assertNull(result);

        verify(studentRepository, times(1)).findById(1);
        verify(courseRepository, times(1)).findById(100L);
        verify(studentRepository, never()).save(any());
        verify(courseRepository, never()).save(any());
    }

}
