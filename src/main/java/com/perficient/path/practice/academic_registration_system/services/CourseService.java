package com.perficient.path.practice.academic_registration_system.services;

import java.util.List;
import java.util.Set;

import com.perficient.path.practice.academic_registration_system.models.Course;

public interface CourseService {
    
    Course getCourseById(Long id);
    Set<Course> getAllCourses();
    Course createCourse(Course course);
    Course updateCourse(Long id,Course course);
    void deleteCourseById(Long id);
    Set<Course> getCoursesByName(String name);
    String getCourseDurationByCourseId(Long courseId);
    List<Course> getCoursesByUserId(Long userId);
    Course addSubjectToCourse(Long courseId, Long subjectId);
    List<Course> getCoursesBySubjectId(Long subjectId);
    Course deleteSubjectFromCourse(Long courseId, Long subjectId);
  
    
}