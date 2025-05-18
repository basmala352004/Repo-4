package com.example.lms.repositories;

import com.example.lms.models.CourseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;



public interface CourseRepository extends JpaRepository<CourseModel, Long> {
    CourseModel findByCourseId(String courseId);
     Optional<CourseModel> findByTitle(String title);

    @Query("SELECT c FROM CourseModel c JOIN c.students s WHERE s.id = :studentId")
    List<CourseModel> findByStudentId(@Param("studentId") Long studentId);

}
