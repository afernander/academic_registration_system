package com.perficient.path.practice.academic_registration_system.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.perficient.path.practice.academic_registration_system.models.Course;



@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByNameContaining(String name);
    List<Course> findCoursesByUsersId(Long userId);
    List<Course> findCoursesBySubjectsId(Long subjectId);

}
    
