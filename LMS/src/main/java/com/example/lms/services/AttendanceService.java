package com.example.lms.services;

import com.example.lms.models.AttendanceModel;
import com.example.lms.models.CourseModel;
import com.example.lms.models.LessonModel;
import com.example.lms.models.StudentModel;
import com.example.lms.repositories.AttendanceRepository;
import com.example.lms.repositories.LessonRepository;
import com.example.lms.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttendanceService {
    private static final String LESSON_NOT_FOUND_MSG = "Lesson not found";
    private static final String STUDENT_NOT_FOUND_MSG = "Student not found";

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;

    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository,StudentRepository studentRepository,LessonRepository lessonRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.lessonRepository = lessonRepository;
    }

    public List<AttendanceModel> displayAllAttendance() {
        return attendanceRepository.findAll();
    }
    public List<AttendanceModel> displayLessonAttendance(long lessonId) {
        LessonModel lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException(LESSON_NOT_FOUND_MSG));
        return attendanceRepository.findByLesson(lesson);

    }
    public String attendLesson(int studentId, long lessonId, String otp) {
        StudentModel student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException(STUDENT_NOT_FOUND_MSG));
        LessonModel lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException(LESSON_NOT_FOUND_MSG));
        String returnMessage;
        LocalDateTime currentTime = LocalDateTime.now();
        boolean isLessonOngoing = currentTime.isAfter(lesson.getStartDate()) && currentTime.isBefore(lesson.getEndDate());
        boolean isCorrectOTP = lesson.getOTP().equals(otp);
        if (!isLessonOngoing) {
            returnMessage = "Lesson is not ongoing.";
        }
        else if (!isCorrectOTP) {
            returnMessage = "OTP is not correct.";
        }
        else {
            List<CourseModel> courses = student.getCourses();
            if (courses.contains(lesson.getCourseModel())) {
                boolean isAttended = true;
                AttendanceModel attendance = new AttendanceModel(lesson, student, isAttended, currentTime);
                attendanceRepository.save(attendance);
                returnMessage = "You are successfully attend.";
            }
            else {
                returnMessage = "You are not enrolled in the course!";
            }
        }
        return returnMessage;

    }
}
