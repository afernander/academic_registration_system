package com.perficient.path.practice.academic_registration_system.services;

import java.util.Set;

import com.perficient.path.practice.academic_registration_system.models.Course;
import com.perficient.path.practice.academic_registration_system.models.Subject;
import com.perficient.path.practice.academic_registration_system.models.User;

public interface CourseService {
    
    Course getCourseById(Long id);
    Set<Course> getAllCourses();
    Course createCourse(Course course);
    Course updateCourse(Course course);
    void deleteCourseById(Long id);
    Set<Course> getCoursesByName(String name);
    Set<Subject> getSubjectsByCourseId(Long courseId);
    Set<User> getUsersByCourseId(Long courseId);
    String getCourseDurationByCourseId(Long courseId);
    
}