package com.perficient.path.practice.academic_registration_system.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.perficient.path.practice.academic_registration_system.models.Course;

public interface CourseService {
    
    Course getCourseById(Long id);
    Page<Course> getAllCourses(int page, int size);
    Course createCourse(Course course);
    Course updateCourse(Long id,Course course);
    void deleteCourseById(Long id);
    Page<Course> getCoursesByName(int page, int size,String name);
    String getCourseDurationByCourseId(Long courseId);
    List<Course> getCoursesByUserId(Long userId);
    Course addSubjectToCourse(Long courseId, Long subjectId);
    List<Course> getCoursesBySubjectId(Long subjectId);
    Course deleteSubjectFromCourse(Long courseId, Long subjectId);
  
    
}