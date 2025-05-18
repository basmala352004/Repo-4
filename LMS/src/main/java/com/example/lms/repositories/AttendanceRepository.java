package com.example.lms.repositories;

import com.example.lms.models.AttendanceModel;
import com.example.lms.models.LessonModel;
import com.example.lms.models.StudentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<AttendanceModel, Long> {
    List<AttendanceModel> findByLesson(LessonModel lesson);
    List<AttendanceModel> findByStudent(StudentModel student);
    AttendanceModel findByLessonAndStudent(LessonModel lesson, StudentModel student);
}
