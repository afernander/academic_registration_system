package com.perficient.path.practice.academic_registration_system.services;

import org.springframework.data.domain.Page;

import com.perficient.path.practice.academic_registration_system.models.Professor;

public interface ProfessorService {
    
    Professor getProfessorById(Long id);
    Page<Professor> getAllProfessors(int page, int size);
    Professor createProfessor(Professor professor);
    Professor updateProfessor(Long id,Professor professor);
    void deleteProfessorById(Long id);
    Page<Professor> getProfessorsByArea(int page, int size,String area);
    Page<Professor> getProfessorsBySpecialization(int page, int size,String specialization);
    Professor addSubjectToProfessor(Long professorId, Long subjectId);
    Professor deleteSubjectFromProfessor(Long professorId, Long subjectId);
}