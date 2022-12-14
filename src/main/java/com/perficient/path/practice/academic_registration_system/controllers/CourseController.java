package com.perficient.path.practice.academic_registration_system.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<Page<Course>> getAllCourses(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "3") int size) {
        Page<Course> courses = courseService.getAllCourses(page,size);
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

    @GetMapping(value="/{name}/search/name")
    public ResponseEntity<Page<Course>> getCoursesByName(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "3") int size,
                                                        @PathVariable String name) {
        Page<Course> courses = courseService.getCoursesByName(page,size,name);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value="/{id}/duration")
    public ResponseEntity<String> getCourseDurationByCourseId(@PathVariable Long id) {
        String duration = courseService.getCourseDurationByCourseId(id);
        return new ResponseEntity<>(duration, HttpStatus.OK);
    }

    @GetMapping(value="/{id}/search/byUserId")
    public ResponseEntity<List<Course>> getCoursesByUserId(@PathVariable Long id) {
        List<Course> courses = courseService.getCoursesByUserId(id);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value="/{courseId}/add/subject/{subjectId}")
    public ResponseEntity<Course> addSubjectToCourse(@PathVariable Long courseId, @PathVariable Long subjectId) {
        Course course = courseService.addSubjectToCourse(courseId, subjectId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @GetMapping(value="/{subjectId}/search/bySubjectId")
    public ResponseEntity<List<Course>> getCoursesBySubjectId(@PathVariable Long subjectId) {
        List<Course> courses = courseService.getCoursesBySubjectId(subjectId);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }
    
    @DeleteMapping(value="/{courseId}/delete/subject/{subjectId}")
    public ResponseEntity<Course> deleteSubjectFromCourse(@PathVariable Long courseId, @PathVariable Long subjectId) {
        Course course = courseService.deleteSubjectFromCourse(courseId, subjectId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }
}