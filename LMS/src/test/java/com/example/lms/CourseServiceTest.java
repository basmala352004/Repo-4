package com.example.lms;

import com.example.lms.dtos.CourseDTO;
import com.example.lms.dtos.StudentDTO;
import com.example.lms.models.CourseModel;
import com.example.lms.models.LessonModel;
import com.example.lms.models.StudentModel;
import com.example.lms.repositories.CourseRepository;
import com.example.lms.services.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)  // Enables Mockito for this test class
 class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private CourseModel course;
    private LessonModel lesson;

    @BeforeEach
    void setUp() {
        // Initialize students and lessons
        StudentModel student1 = new StudentModel("John Doe", "pass1");
        StudentModel student2 = new StudentModel("Jane Smith", "pass2");

        // Create a CourseModel and LessonModel for testing
        course = new CourseModel("CS101", "Computer Science 101", "Intro to CS", 4, null, null);
        course.setStudents(Arrays.asList(student1, student2));  // Adding students to the course
        course.setListLessons(new ArrayList<>());  // Initialize the lessons list

        lesson = new LessonModel("Lesson 1", Arrays.asList("Topic 1", "Topic 2"), "Lesson Description", "Dr. John",
                LocalDateTime.of(2024, 12, 19, 10, 0), 60, "1234");  // Setting a valid startDate
    }

    @Test
    void testCreateCourse() {
        // Mock save method
        when(courseRepository.save(course)).thenReturn(course);

        courseService.createCourse(course);  // Call the service method

        verify(courseRepository, times(1)).save(course);  // Verify that save was called once
    }

    @Test
    void testDisplayCourses() {
        // Mock students
        StudentModel student1 = new StudentModel();
        student1.setId(1);
        student1.setName("John Doe");

        StudentModel student2 = new StudentModel();
        student2.setId(2);
        student2.setName("Jane Smith");

        // Mock course with students
        CourseModel mockCourse = new CourseModel();
        mockCourse.setCourseId("CS101");
        mockCourse.setStudents(Arrays.asList(student1, student2));

        // Mock the repository to return the course
        when(courseRepository.findAll()).thenReturn(Arrays.asList(mockCourse));

        // Call the displayCourses function
        List<CourseDTO> courses = courseService.displayCourses();

        // Verify the results
        assertNotNull(courses);
        assertEquals(1, courses.size());
        assertEquals("CS101", courses.get(0).getCourseId());

        // Verify students in the DTO
        List<StudentDTO> returnedStudents = courses.get(0).getStudents();
        assertNotNull(returnedStudents);
        assertEquals(2, returnedStudents.size());
        assertEquals("John Doe", returnedStudents.get(0).getName());
        assertEquals("Jane Smith", returnedStudents.get(1).getName());
    }


    @Test
    void testAddLessonToCourse() {
        // Mock findById to return a course
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

        courseService.addLessonToCourse(course.getId(), lesson);  // Call the service method

        assertEquals(1, course.getListLessons().size());  // Verify that the lesson was added
        assertEquals("Lesson 1", course.getListLessons().get(0).getTitle());
    }

    @Test
    void testAddMediaFile() {
        // Mock findByCourseId to return a course
        when(courseRepository.findByCourseId(course.getCourseId())).thenReturn(course);

        courseService.addMediaFile(course.getCourseId(), "C:/uploads/lesson1.mp4");  // Call the service method

        assertEquals(1, course.getMediaFiles().size());  // Verify that the media file was added
        assertEquals("C:/uploads/lesson1.mp4", course.getMediaFiles().get(0));
    }

    @Test
    void testUpdateCourseDetails() {
        // Mock findById to return a course
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Updated course details
        CourseModel updatedCourse = new CourseModel();
        updatedCourse.setTitle("Updated Title");
        updatedCourse.setDescription("Updated Description");
        updatedCourse.setDurationHours(50);

        courseService.updateCourseDetails(1L, updatedCourse); // Call the service method

        // Verify the updates
        assertEquals("Updated Title", course.getTitle());
        assertEquals("Updated Description", course.getDescription());
        assertEquals(50, course.getDurationHours());
        verify(courseRepository, times(1)).save(course); // Verify save method is called
    }
    @Test
    void testGetStudentsByCourseId() {
        // Mock findById to return a course
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

        // Call the service method to get the students
        List<StudentModel> students = courseService.getStudentsByCourseId(course.getId());

        // Verify that the correct students are returned
        assertNotNull(students);
        assertEquals(2, students.size());
        assertEquals("John Doe", students.get(0).getName());
        assertEquals("Jane Smith", students.get(1).getName());
    }

    @Test
    void testGetMediaFilesByCourseId() {
        // Mock findById to return a course
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

        // Add a media file
        course.getMediaFiles().add("C:/uploads/lesson1.mp4");

        // Call the service method to get media files
        List<String> mediaFiles = courseService.getMediaFilesByCourseId(course.getId());

        // Verify that the correct media file is returned
        assertNotNull(mediaFiles);
        assertEquals(1, mediaFiles.size());
        assertEquals("C:/uploads/lesson1.mp4", mediaFiles.get(0));
    }


}
