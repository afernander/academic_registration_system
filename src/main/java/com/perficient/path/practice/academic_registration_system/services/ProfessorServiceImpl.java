package com.perficient.path.practice.academic_registration_system.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.perficient.path.practice.academic_registration_system.errors.ProfessorNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.models.Professor;
import com.perficient.path.practice.academic_registration_system.repositories.ProfessorRepository;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorServiceImpl(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @Override
    public Professor getProfessorById(Long id) {
        return professorRepository.findById(id).orElseThrow(()-> new ProfessorNotFoundExeption("Professor with id "+id+" not found"));
    }

    @Override
    public Set<Professor> getAllProfessors() {
        Set<Professor> professorsSet = new HashSet<>();

        professorRepository.findAll().iterator().forEachRemaining(professorsSet::add);
        return professorsSet;
    }

    @Override
    public Professor createProfessor(Professor professor) {
        return professorRepository.save(professor);
    }

    @Override
    public Professor updateProfessor(Long id ,Professor professor) {
        Professor professorToUpdate = professorRepository.findById(professor.getId()).orElseThrow(()-> new ProfessorNotFoundExeption("Professor with id "+professor.getId()+" not found to update"));
        professorToUpdate.setArea(professor.getArea());
        professorToUpdate.setSpecialization(professor.getSpecialization());
        
        return professorRepository.save(professorToUpdate);
    }

    @Override
    public void deleteProfessorById(Long id) {
        Professor professorToDelete = professorRepository.findById(id).orElseThrow(()-> new ProfessorNotFoundExeption("Professor with id "+id+" not found to delete"));
        professorRepository.delete(professorToDelete);
    }

    @Override
    public Set<Professor> getProfessorsByArea(String area) {
        Set<Professor> professorsSet = new HashSet<>();
        professorRepository.findByAreaContaining(area).iterator().forEachRemaining(professorsSet::add);
        if(professorsSet.isEmpty()) {
            throw new ProfessorNotFoundExeption("No professors found with area : "+area);
        }
        return professorsSet;
    }

    @Override
    public Set<Professor> getProfessorsBySpecialization(String specialization) {
        Set<Professor> professorsSet = new HashSet<>();
        professorRepository.findBySpecializationContaining(specialization).iterator().forEachRemaining(professorsSet::add);
        if(professorsSet.isEmpty()) {
            throw new ProfessorNotFoundExeption("No professors found with specialization : "+specialization);
        }
        return professorsSet;
    }
}