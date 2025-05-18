package com.example.lms.services;

import com.example.lms.models.*;
import com.example.lms.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TrackPerformanceService {

    private static final String COURSE_ID_KEY = "courseId";
    private static final String DATE_KEY = "submissionDate";


    private final AttendanceRepository attendanceRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final AssignmentRepository assignmentRepository;
    private final QuizRepository quizRepository;

    @Autowired
    public TrackPerformanceService(AttendanceRepository attendanceRepository,  CourseRepository courseRepository,StudentRepository studentRepository, AssignmentRepository assignmentRepository, QuizRepository quizRepository) {
        this.attendanceRepository = attendanceRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.assignmentRepository = assignmentRepository;
        this.quizRepository = quizRepository;
    }

    // Fetch performance for courses based on attendance
    public List<Map<String, Object>> getPerformanceForCourses(List<String> courseNames, String lessonName) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (String courseName : courseNames) {
            Optional<CourseModel> courseOpt = courseRepository.findByTitle(courseName);
            if (courseOpt.isPresent()) {
                CourseModel course = courseOpt.get();

                Optional<LessonModel> lessonOpt = course.getListLessons().stream()
                        .filter(lesson -> lesson.getTitle().equals(lessonName))
                        .findFirst();

                if (lessonOpt.isPresent()) {
                    LessonModel lesson = lessonOpt.get();
                    List<AttendanceModel> attendanceRecords = attendanceRepository.findByLesson(lesson);

                    Map<String, Object> coursePerformance = new HashMap<>();
                    coursePerformance.put("Course", course.getTitle());

                    List<Map<String, Object>> performanceDetails = new ArrayList<>();

                    for (AttendanceModel studentRecord : attendanceRecords) {
                        Map<String, Object> performanceDetail = new HashMap<>();
                        performanceDetail.put("Student ID", studentRecord.getStudent().getId());
                        performanceDetail.put("Attendance Status", studentRecord.isAttended() ? "Attended" : "Not Attended");
                        performanceDetail.put("Lesson", lesson.getTitle());
                        performanceDetail.put("Date", studentRecord.getTimestamp().toLocalDate());

                        performanceDetails.add(performanceDetail);
                    }

                    coursePerformance.put("Performance", performanceDetails);
                    result.add(coursePerformance);
                }
            }
        }
        return result;
    }

    // Fetch assignment grades and feedback
    public Map<String, Object> getAssignmentGrades(Integer assignmentId) {
        Optional<Assignment> assignmentOpt = assignmentRepository.findById(assignmentId);

        if (assignmentOpt.isPresent()) {
            Assignment assignment = assignmentOpt.get();

            // Creating the response map
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("assignmentId", assignment.getAssignmentID());
            result.put("assignmentTitle", assignment.getTitle());

            // Fetching students who submitted the assignment
            List<StudentModel> students = studentRepository.findAll(); // Adjust if there's a direct relation with Assignment
            List<Map<String, Object>> studentsGrades = new ArrayList<>();

            for (StudentModel student : students) {
                Map<String, Object> studentDetails = new HashMap<>();
                studentDetails.put("studentId", student.getId());
                studentDetails.put("studentName", student.getName());
                studentDetails.put("grade", assignment.getGrades());
                studentDetails.put("feedback", assignment.getFeedback());
                studentDetails.put(DATE_KEY, new Date().toString()); // Replace with actual submission date if available
                List<CourseModel> courses = courseRepository.findByStudentId(Long.valueOf(student.getId())); // Ensure Long type
                if (!courses.isEmpty()) {
                    // Assuming the first course is the one to include; modify as necessary for your use case
                    studentDetails.put(COURSE_ID_KEY, courses.get(0).getId());  // Add the course ID to the student details
                } else {
                    // Handle case when no courses are found for the student
                    studentDetails.put(COURSE_ID_KEY, "No course found");
                }
                studentsGrades.add(studentDetails);
            }

            result.put("students", studentsGrades);
            return result;
        }

        throw new NoSuchElementException("Assignment not found with ID: " + assignmentId);
    }

    // Fetch quiz grades and feedback
    public Map<String, Object> getQuizGrades(long quizId) {
        Optional<QuizModel> quizOpt = quizRepository.findById(quizId);

        if (quizOpt.isPresent()) {
            QuizModel quiz = quizOpt.get();

            // Create the response map
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("quizId", quiz.getId());
            result.put("quizTitle", quiz.getQuizTitle());

            // Fetching students who took the quiz
            List<StudentModel> students = studentRepository.findAll(); // Adjust if there's a direct relation with Quiz
            List<Map<String, Object>> studentsGrades = new ArrayList<>();

            for (StudentModel student : students) {
                Map<String, Object> studentDetails = new HashMap<>();
                studentDetails.put("studentId", student.getId());
                studentDetails.put("studentName", student.getName());
                studentDetails.put("grade", quiz.getGrade());
                studentDetails.put(DATE_KEY, new Date().toString()); // Updated key // Replace with actual submission date if available

                List<CourseModel> courses = courseRepository.findByStudentId(Long.valueOf(student.getId())); // Ensure Long type
                if (!courses.isEmpty()) {
                    // Assuming the first course is the one to include; modify as necessary
                    studentDetails.put(COURSE_ID_KEY, courses.get(0).getId());
                } else {
                    // Handle case when no courses are found for the student
                    studentDetails.put(COURSE_ID_KEY, "No course found");
                }
                studentsGrades.add(studentDetails);
            }

            result.put("students", studentsGrades);
            return result;
        }

        throw new NoSuchElementException("Quiz not found with ID: " + quizId);
    }
}
