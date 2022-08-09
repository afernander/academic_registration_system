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
    public ResponseEntity<Set<Subject>> getAllSubjects() {
        Set<Subject> subjectsSet = subjectService.getAllSubjects();
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
    public ResponseEntity<Set<Subject>> getSubjectsByName(@PathVariable String name) {
        Set<Subject> subjects = subjectService.getSubjectsByName(name);
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    @GetMapping(value = "/{area}/search/area")
    public ResponseEntity<Set<Subject>> getSubjectsByArea(@PathVariable String area) {
        Set<Subject> subjects = subjectService.getSubjectsByArea(area);
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    @GetMapping(value = "/{credits}/search/credits")
    public ResponseEntity<Set<Subject>> getSubjectsByCredits(@PathVariable Integer credits) {
        Set<Subject> subjects = subjectService.getSubjectsByCredits(credits);
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }
}