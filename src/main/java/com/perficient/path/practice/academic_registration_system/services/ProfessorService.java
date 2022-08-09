package com.perficient.path.practice.academic_registration_system.services;

import java.util.Set;

import com.perficient.path.practice.academic_registration_system.models.Professor;
import com.perficient.path.practice.academic_registration_system.models.Subject;

public interface ProfessorService {
    
    Professor getProfessorById(Long id);
    Set<Professor> getAllProfessors();
    Professor createProfessor(Professor professor);
    Professor updateProfessor(Long id,Professor professor);
    void deleteProfessorById(Long id);
    Set<Professor> getProfessorsByArea(String area);
    Set<Professor> getProfessorsBySpecialization(String specialization);
    //Set<Subject> getSubjectsByProfessorId(Long professorId);
    //Long getUserIdByProfessorId(Long professorId);
}