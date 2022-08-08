package com.perficient.path.practice.academic_registration_system.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.perficient.path.practice.academic_registration_system.errors.CourseNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.models.Course;
import com.perficient.path.practice.academic_registration_system.repositories.CourseRepository;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;


    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(()-> new CourseNotFoundExeption("Course with id "+ id+ " not found"));
    }

    @Override
    public Set<Course> getAllCourses() {
        Set<Course> coursesSet = new HashSet<>();
        courseRepository.findAll().iterator().forEachRemaining(coursesSet::add);

        return coursesSet;
    }

    @Override
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Long id, Course course) {
        Course courseToUpdate = courseRepository.findById(id).orElseThrow(()-> new CourseNotFoundExeption("Course with id "+ id+ " not found to update"));
        courseToUpdate.setName(course.getName());
        courseToUpdate.setDescription(course.getDescription());
        courseToUpdate.setDuration(course.getDuration());
        courseToUpdate.setDurationType(course.getDurationType());
        courseToUpdate.setPrice(course.getPrice());
        return courseRepository.save(courseToUpdate);
    }

    @Override
    public void deleteCourseById(Long id) {
        Course courseToDelete = courseRepository.findById(id).orElseThrow(()-> new CourseNotFoundExeption("Course with id "+ id+ " not found to delete"));
        courseRepository.delete(courseToDelete);
    }

    @Override
    public Set<Course> getCoursesByName(String name){
        Set<Course> coursesSet = new HashSet<>();
        courseRepository.findByNameContaining(name).iterator().forEachRemaining(coursesSet::add);
        if(coursesSet.isEmpty()){
            throw new CourseNotFoundExeption("Course with  "+ name+ " in name not found");
        }
        return coursesSet;
    }

    @Override
    public String getCourseDurationByCourseId(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(()-> new CourseNotFoundExeption("Course with id "+ courseId+ " not found to get duration"));
        return course.getDuration() + " " + course.getDurationType();
    }
}