package com.example.lms.models;

import jakarta.persistence.*;
@Entity
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //id,title , description , deadline , grades , feedback
    private Integer assignmentID;
    private String title;
    private String description;
    private String deadline;
    private double grades;
    private String feedback;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = true) // Foreign key in Quiz table
    private CourseModel course;


    // Getters and Setters

    public Assignment() {
    }

    public Assignment(Integer assignmentID, String title, String description, String deadline, double grades, String feedback, CourseModel course) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.grades = grades;
        this.feedback = feedback;
        this.assignmentID = assignmentID;
        this.course = course;
    }

    public Integer getAssignmentID() {
        return assignmentID;
    }

    public void setAssignmentID(Integer assignmentID) {
        this.assignmentID = assignmentID;
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


    public double getGrades() {
        return grades;
    }

    public void setGrades(double grades) {
        this.grades = grades;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public CourseModel getCourse() {
        return course;
    }

    public void setCourse(CourseModel course) {
        this.course = course;
    }
}