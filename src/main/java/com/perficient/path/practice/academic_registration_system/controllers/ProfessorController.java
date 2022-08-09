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

import com.perficient.path.practice.academic_registration_system.models.Professor;
import com.perficient.path.practice.academic_registration_system.services.ProfessorService;

@RestController
@RequestMapping("/professors")
public class ProfessorController {
    
    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Professor> getProfessorById(@PathVariable Long id) {
        var professor = professorService.getProfessorById(id);
        return new ResponseEntity<>(professor, HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Set<Professor>> getAllProfessors() {
        Set<Professor> professorsSet = professorService.getAllProfessors();
        return new ResponseEntity<>(professorsSet, HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Professor> createProfessor(@Valid @RequestBody Professor professor) {
        Professor newProfessor = professorService.createProfessor(professor);
        return new ResponseEntity<>(newProfessor, HttpStatus.CREATED);
    }

    @PostMapping(value = "/{id}/update")
    public ResponseEntity<Professor> updateProfessor(@PathVariable Long id, @Valid @RequestBody Professor professor) {
        Professor updatedProfessor = professorService.updateProfessor(id, professor);
        return new ResponseEntity<>(updatedProfessor, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<Professor> deleteProfessorById(@PathVariable Long id) {
        professorService.deleteProfessorById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{area}/search/area")
    public ResponseEntity<Set<Professor>> getAllProfessorsByArea(@PathVariable String area) {
        Set<Professor> professorsSet = professorService.getProfessorsByArea(area);
        return new ResponseEntity<>(professorsSet, HttpStatus.OK);
    }

    @GetMapping(value = "/{specialization}/search/specialization")
    public ResponseEntity<Set<Professor>> getAllProfessorsBySpecialization(@PathVariable String specialization) {
        Set<Professor> professorsSet = professorService.getProfessorsBySpecialization(specialization);
        return new ResponseEntity<>(professorsSet, HttpStatus.OK);
    }
}