package com.example.lms.controllers;

import com.example.lms.dtos.CourseDTO;
import com.example.lms.models.CourseModel;
import com.example.lms.models.StudentModel;
import com.example.lms.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {


    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    private static final String UPLOAD_DIRECTORY = "C:/uploads/";

    @PostMapping("/createCourse")
    public ResponseEntity<String> createCourse(@RequestBody CourseModel course) {
        if (course.getListLessons() == null) {
            course.setListLessons(new ArrayList<>());
        }
        if (course.getMediaFiles() == null) {
            course.setMediaFiles(new ArrayList<>());
        }

        courseService.createCourse(course);  // Create the course using the service
        return ResponseEntity.ok("Course created successfully");
    }

    @PostMapping("/{courseId}/upload-media")
    public ResponseEntity<String> uploadMedia(@PathVariable String courseId, @RequestParam("file") MultipartFile file) {
        try {

            File uploadDir = new File(UPLOAD_DIRECTORY);
            if (!uploadDir.exists() && !uploadDir.mkdirs()) {
                    return ResponseEntity.status(500).body("Failed to create upload directory.");
            }

            // Save the file to the Path
            String filePath = UPLOAD_DIRECTORY + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            // Add the file path
            courseService.addMediaFile(courseId, filePath);

            return ResponseEntity.ok("Media file uploaded successfully: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("File upload failed: " + e.getMessage());
        }
    }


    @GetMapping("/displayCourses")
    public ResponseEntity<List<CourseDTO>> displayCourses() {
        List<CourseDTO> courses = courseService.displayCourses();
        return ResponseEntity.ok(courses);
    }
    @DeleteMapping("/{courseId}/deleteStudent/{studentId}")
    public ResponseEntity<String> deleteEnrollStudent(@PathVariable Long courseId, @PathVariable Integer studentId) {
        courseService.deleteStudentFromCourse(courseId, studentId);
        return ResponseEntity.ok("Student deleted successfully");
    }

    @DeleteMapping("/{courseId}/deleteAllStudents")
    public ResponseEntity<String> deleteAllStudents(@PathVariable Long courseId) {
        courseService.deleteAllStudentsFromCourse(courseId);
        return ResponseEntity.ok("All students deleted successfully");
    }
    @PutMapping("/{courseId}/update")
    public ResponseEntity<String> updateCourse(@PathVariable Long courseId, @RequestBody CourseModel updatedCourse) {
        courseService.updateCourseDetails(courseId, updatedCourse);
        return ResponseEntity.ok("Course updated successfully");
    }

    @GetMapping("/{courseId}/students")
    public ResponseEntity<List<StudentModel>> getEnrolledStudents(@PathVariable Long courseId) {
        List<StudentModel> students = courseService.getStudentsByCourseId(courseId);

        if (students == null) {
            return ResponseEntity.status(404).body(null); // Return 404 if course doesn't exist
        } else if (students.isEmpty()) {
            return ResponseEntity.ok(new ArrayList<>()); // ✅ Return 200 with empty list if course exists but no students
        }

        return ResponseEntity.ok(students); // ✅ Return list of students if enrolled
    }

    @GetMapping("/{courseId}/materials")
    public ResponseEntity<List<String>> getCourseMaterials(@PathVariable Long courseId) {
        List<String> mediaFiles = courseService.getMediaFilesByCourseId(courseId);
        if (mediaFiles.isEmpty()) {
            return ResponseEntity.status(404).body(null); // Return 404 if no materials are found
        }
        return ResponseEntity.ok(mediaFiles); // Return the list of media files for the course
    }



}
