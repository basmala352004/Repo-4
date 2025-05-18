package com.example.lms.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class LessonModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ElementCollection
    @CollectionTable(name = "lesson_topics", joinColumns = @JoinColumn(name = "lesson_id"))
    @Column(name = "topic")
    private List<String> topics;
    private String description;
    private String teacherName;
    private LocalDateTime startDate;
    private int durationMinutes;
    private LocalDateTime endDate;
    private String otp;
    @ManyToOne
    @JoinColumn(name = "courseId")
    @JsonBackReference // Handles the back part of the reference
    private CourseModel courseModel;

    public LessonModel(String title, List<String> topics, String description, String teacherName, LocalDateTime startDate, int durationMinutes, String otp) {
        this.title = title;
        this.topics = topics;
        this.description = description;
        this.teacherName = teacherName;
        this.startDate = startDate;
        this.durationMinutes = durationMinutes;
        this.endDate = startDate.plusMinutes(durationMinutes);
        this.otp = otp;
    }
    public LessonModel() {
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<String> getTopics() {
        return topics;
    }
    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getTeacherName() {
        return teacherName;
    }
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
    public LocalDateTime getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        updateEndDate();
    }
    public int getDurationMinutes() {
        return durationMinutes;
    }
    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
        updateEndDate();
    }
    public LocalDateTime getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    public String getOTP() {
        return otp;
    }
    public void setOTP(String otp) {
        this.otp = otp;
    }
    public CourseModel getCourseModel() {
        return courseModel;
    }
    public void setCourseModel(CourseModel courseModel) {
        this.courseModel = courseModel;
    }
    public void updateEndDate() {
        if (this.startDate != null) {
            this.endDate = this.startDate.plusMinutes(this.durationMinutes);
        }
    }
}
