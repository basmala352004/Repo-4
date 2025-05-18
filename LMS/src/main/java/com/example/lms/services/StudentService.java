package com.example.lms.services;

import com.example.lms.dtos.StudentDTO;
import com.example.lms.models.CourseModel;
import com.example.lms.models.StudentModel;
import com.example.lms.repositories.CourseRepository;
import com.example.lms.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(s -> {
                    StudentDTO dto = new StudentDTO(s);
                    dto.setCourses(s.getCourses());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    public StudentModel getStudentById(int id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));
    }

    public StudentModel enrollStudent( int studentId, long courseId) {
        // اوزين نجيب ال طالب العنده ال
        StudentModel student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        CourseModel course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));

        if (student.getCourses().contains(course)) {
            throw new IllegalStateException("Student is already enrolled in this course");
        }

        student.getCourses().add(course);
        course.getStudents().add(student);

        studentRepository.save(student);
        courseRepository.save(course);

        return student;
    }
}
