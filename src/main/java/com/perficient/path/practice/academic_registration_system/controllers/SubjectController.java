package com.perficient.path.practice.academic_registration_system.controllers;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}