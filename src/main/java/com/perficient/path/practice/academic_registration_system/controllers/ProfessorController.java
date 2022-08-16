package com.perficient.path.practice.academic_registration_system.controllers;

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
    public ResponseEntity<Page<Professor>> getAllProfessors(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "3") int size) {
        Page<Professor> professorsSet = professorService.getAllProfessors(page,size);
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
    public ResponseEntity<Page<Professor>> getAllProfessorsByArea(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "3") int size,
                                                                 @PathVariable String area) {
        Page<Professor> professorsSet = professorService.getProfessorsByArea(page,size,area);
        return new ResponseEntity<>(professorsSet, HttpStatus.OK);
    }

    @GetMapping(value = "/{specialization}/search/specialization")
    public ResponseEntity<Page<Professor>> getAllProfessorsBySpecialization(@RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "3") int size,
                                                                            @PathVariable String specialization) {
        Page<Professor> professorsSet = professorService.getProfessorsBySpecialization(page,size,specialization);
        return new ResponseEntity<>(professorsSet, HttpStatus.OK);
    }

    @GetMapping(value = "/{professorId}/add/subject/{subjectId}")
    public ResponseEntity<Professor> addSubjectToProfessor(@PathVariable Long professorId, @PathVariable Long subjectId) {
        Professor professor = professorService.addSubjectToProfessor(professorId, subjectId);
        return new ResponseEntity<>(professor, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{professorId}/delete/subject/{subjectId}")
    public ResponseEntity<Professor> deleteSubjectFromProfessor(@PathVariable Long professorId, @PathVariable Long subjectId) {
        Professor professor = professorService.deleteSubjectFromProfessor(professorId, subjectId);
        return new ResponseEntity<>(professor, HttpStatus.OK);
    }
}