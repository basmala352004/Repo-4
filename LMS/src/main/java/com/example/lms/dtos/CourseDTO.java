package com.example.lms.dtos;

import com.example.lms.models.CourseModel;
import com.example.lms.models.LessonModel;
import com.example.lms.models.StudentModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CourseDTO {
    private Long id;
    private String courseId;
    private String title;
    private String description;
    private int durationHours;
    private List<LessonModel> listLessons = new ArrayList<>();
    private List<String> mediaFiles;
    private List<StudentDTO> students ;
    public CourseDTO(CourseModel courseModel) {
        this.id = courseModel.getId();
        this.courseId = courseModel.getCourseId();
        this.title = courseModel.getTitle();
        this.description = courseModel.getDescription();
        this.durationHours = courseModel.getDurationHours();
        this.mediaFiles = courseModel.getMediaFiles();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(int durationHours) {
        this.durationHours = durationHours;
    }

    public List<LessonModel> getListLessons() {
        return listLessons;
    }

    public void setListLessons(List<LessonModel> listLessons) {
        this.listLessons = listLessons;
    }

    public List<String> getMediaFiles() {
        return mediaFiles;
    }

    public void setMediaFiles(List<String> mediaFiles) {
        this.mediaFiles = mediaFiles;
    }

    public List<StudentDTO> getStudents() {
        return students;
    }

    public void  setStudents(List<StudentModel> students) {
        this.students=students.stream().map(StudentDTO::new).collect(Collectors.toList());
    }
}
