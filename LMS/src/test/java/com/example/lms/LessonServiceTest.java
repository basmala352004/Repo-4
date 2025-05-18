package com.example.lms;

import com.example.lms.models.CourseModel;
import com.example.lms.models.LessonModel;
import com.example.lms.models.NotificationModel;
import com.example.lms.models.StudentModel;
import com.example.lms.repositories.LessonRepository;
import com.example.lms.services.LessonService;
import com.example.lms.services.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)  // Enables Mockito for this test class
 class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private LessonService lessonService;

    private LessonModel lesson;


    @Mock
    private NotificationService notificationService;


    @BeforeEach
    void setUp() {
        // Create students
        StudentModel student1 = new StudentModel("John Doe", "password1");
        StudentModel student2 = new StudentModel("Jane Smith", "password2");

        // Create course and add students
      CourseModel course = new CourseModel("CS101", "Computer Science 101", "Intro to CS", 4, null, null);
        course.setStudents(Arrays.asList(student1, student2));
        // Create a LessonModel for testing with a valid startDate
        lesson = new LessonModel("Lesson 1", Arrays.asList("Topic 1", "Topic 2"), "Lesson Description", "Dr. John",
                LocalDateTime.of(2024, 12, 19, 10, 0), 60, "1234");
        lesson.setCourseModel(course);
        lesson.setId(1L);
    }

    @Test
    void testCreateLesson() {
        // Mock save method
        when(lessonRepository.save(lesson)).thenReturn(lesson);

        lessonService.createLesson(lesson);  // Call the service method

        verify(lessonRepository, times(1)).save(lesson);  // Verify that save was called once
    }

    @Test
    void testDisplayLessons() {
        // Mock the repository to return a list of lessons
        when(lessonRepository.findAll()).thenReturn(Arrays.asList(lesson));

        List<LessonModel> lessons = lessonService.displayLessons();

        assertNotNull(lessons);
        assertEquals(1, lessons.size());
        assertEquals("Lesson 1", lessons.get(0).getTitle());
    }


    @Test
    void testGenerateOTP() {
        // Mock findById to return the lesson
        when(lessonRepository.findById(lesson.getId())).thenReturn(java.util.Optional.of(lesson));

        // Mock save to return the lesson after OTP is set
        when(lessonRepository.save(lesson)).thenReturn(lesson);

        // Mock notification service to simulate sending notifications
        doNothing().when(notificationService).sendNotification(any(NotificationModel.class));

        // Call the service method to generate OTP
        String result = lessonService.generateOTP("123456", lesson.getId());

        // Verify that OTP is set
        assertEquals("123456", lesson.getOTP());

        // Verify that the lesson repository's save method is called once
        verify(lessonRepository, times(1)).save(lesson);

        // Verify that the notification service's sendNotification method is called twice (once for each student)
        verify(notificationService, times(2)).sendNotification(any(NotificationModel.class));

        // Check the result message
        assertEquals("OTP generated succeffully and notification has sent to students.", result);
    }
}
