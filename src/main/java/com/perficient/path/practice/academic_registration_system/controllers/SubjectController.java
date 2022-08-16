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

import com.perficient.path.practice.academic_registration_system.models.Subject;
import com.perficient.path.practice.academic_registration_system.services.SubjectService;

@RestController
@RequestMapping("/subjects")
public class SubjectController {
    
    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Subject> getSubjectById(@PathVariable Long id) {
        var subject = subjectService.getSubjectById(id);
        return new ResponseEntity<>(subject, HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Page<Subject>> getAllSubjects(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "3") int size) {
        Page<Subject> subjectsSet = subjectService.getAllSubjects(page,size);
        return new ResponseEntity<>(subjectsSet, HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Subject> createSubject(@Valid @RequestBody Subject subject) {
        Subject newSubject = subjectService.createSubject(subject);
        return new ResponseEntity<>(newSubject, HttpStatus.CREATED);
    }

    @PostMapping(value = "/{id}/update")
    public ResponseEntity<Subject> updateSubject(@PathVariable Long id, @Valid @RequestBody Subject subject) {
        Subject updatedSubject = subjectService.updateSubject(id, subject);
        return new ResponseEntity<>(updatedSubject, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<Subject> deleteSubjectById(@PathVariable Long id) {
        subjectService.deleteSubjectById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{name}/search/name")
    public ResponseEntity<Page<Subject>> getSubjectsByName(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "3") int size,
                                                         @PathVariable String name) {
        Page<Subject> subjects = subjectService.getSubjectsByName(page,size,name);
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    @GetMapping(value = "/{area}/search/area")
    public ResponseEntity<Page<Subject>> getSubjectsByArea(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "3") int size,
                                                          @PathVariable String area) {
        Page<Subject> subjects = subjectService.getSubjectsByArea(page,size,area);
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    @GetMapping(value = "/{credits}/search/credits")
    public ResponseEntity<Page<Subject>> getSubjectsByCredits(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "3") int size,
                                                             @PathVariable Integer credits) {
        Page<Subject> subjects = subjectService.getSubjectsByCredits(page,size,credits);
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    @GetMapping(value = "/{courseId}/search/byCourseId")
    public ResponseEntity<List<Subject>> getSubjectsByCourseId(@PathVariable Long courseId) {
        List<Subject> subjects = subjectService.getSubjectsByCourseId(courseId);
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    @GetMapping(value = "/{professorId}/search/byProfessorId")
    public ResponseEntity<List<Subject>> getSubjectsByProfessorId(@PathVariable Long professorId) {
        List<Subject> subjects = subjectService.getSubjectsByProfessorId(professorId);
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }
}