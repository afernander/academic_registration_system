package com.perficient.path.practice.academic_registration_system.controllers;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perficient.path.practice.academic_registration_system.models.Course;
import com.perficient.path.practice.academic_registration_system.services.CourseService;

@RestController
@RequestMapping("/courses")
public class CourseController {
    
    private final CourseService courseService;


    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        var course = courseService.getCourseById(id);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @GetMapping(value="/all")
    public ResponseEntity<Set<Course>> getAllCourses() {
        Set<Course> courses = courseService.getAllCourses();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping(value="/create")
    public ResponseEntity<Course> createCourse(@Valid @RequestBody Course course) {
        Course newCourse = courseService.createCourse(course);
        return new ResponseEntity<>(newCourse, HttpStatus.CREATED);
    }

    @PostMapping(value="/{id}/update")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @Valid @RequestBody Course course) {
        Course updatedCourse = courseService.updateCourse(id, course);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }

    @DeleteMapping(value="/{id}/delete")
    public ResponseEntity<Course> deleteCourseById(@PathVariable Long id) {
        courseService.deleteCourseById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value="/{name}/name")
    public ResponseEntity<Set<Course>> getCoursesByName(@PathVariable String name) {
        Set<Course> courses = courseService.getCoursesByName(name);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value="/{id}/duration")
    public ResponseEntity<String> getCourseDurationByCourseId(@PathVariable Long id) {
        String duration = courseService.getCourseDurationByCourseId(id);
        return new ResponseEntity<>(duration, HttpStatus.OK);
    }
}