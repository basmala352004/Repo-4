package com.example.lms.models;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AttendanceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Many-to-One relationship with LessonModel
    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private LessonModel lesson;

    // Many-to-One relationship with StudentModel
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentModel student;
    private boolean isAttended;
    private LocalDateTime timestamp;

    public AttendanceModel(LessonModel lesson, StudentModel student, boolean isAttended, LocalDateTime timestamp) {

        this.lesson = lesson;
        this.student = student;
        this.isAttended = isAttended;
        this.timestamp = timestamp;
    }
    public AttendanceModel() {}
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public StudentModel getStudent() {
        return student;
    }
    public void setStudent(StudentModel student) {
        this.student = student;
    }
    public LessonModel getLesson() {
        return lesson;
    }
    public void setLesson(LessonModel lesson) {
        this.lesson = lesson;
    }
    public boolean isAttended() {
        return isAttended;
    }
    public void setAttended(boolean attended) {
        isAttended = attended;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
