package com.example.lms.dtos;

import com.example.lms.models.CourseModel;
import com.example.lms.models.StudentModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentDTO {
    int id;
    String name;
    String email;
    private List<CourseDTO> courses;

    public StudentDTO(StudentModel student) {
        this.id = student.getId();
        this.name = student.getName();
        this.email = student.getEmail();
        this.courses = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<CourseDTO> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseModel> courses) {
        this.courses = courses.stream()
                .map(CourseDTO::new)
                .collect(Collectors.toList());
    }
}
