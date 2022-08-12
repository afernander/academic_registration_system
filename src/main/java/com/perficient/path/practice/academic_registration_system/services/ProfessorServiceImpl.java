package com.perficient.path.practice.academic_registration_system.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.perficient.path.practice.academic_registration_system.errors.DuplicatedDataExeption;
import com.perficient.path.practice.academic_registration_system.errors.ProfessorNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.errors.SubjectNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.models.Professor;
import com.perficient.path.practice.academic_registration_system.models.Subject;
import com.perficient.path.practice.academic_registration_system.repositories.ProfessorRepository;
import com.perficient.path.practice.academic_registration_system.repositories.SubjectRepository;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;

    private final SubjectRepository subjectRepository;

    public ProfessorServiceImpl(ProfessorRepository professorRepository, SubjectRepository subjectRepository) {
        this.professorRepository = professorRepository;
        this.subjectRepository = subjectRepository;
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
        try{
            professorRepository.save(professor);
        }catch(Exception e){
            throw new DuplicatedDataExeption("Professor with code "+ professor.getCode()+ " already exists");
        }
        return professor;
    }

    @Override
    public Professor updateProfessor(Long id ,Professor professor) {
        Professor professorToUpdate = professorRepository.findById(id).orElseThrow(()-> new ProfessorNotFoundExeption("Professor with id "+professor.getId()+" not found to update"));
        professorToUpdate.setCode(professor.getCode());
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

    @Override
    public Professor addSubjectToProfessor(Long professorId, Long subjectId) {
        Professor professor = professorRepository.findById(professorId).orElseThrow(()-> new ProfessorNotFoundExeption("Professor with id "+professorId+" not found to add subject"));
        Subject subject =subjectRepository.findById(subjectId).orElseThrow(()-> new SubjectNotFoundExeption("Subject with id "+subjectId+" not found to add to professor"));
        professor.getSubjects().add(subject);
        subject.setProfessor(professor);
        subjectRepository.save(subject);
        return professorRepository.save(professor);
    }

    @Override
    public Professor deleteSubjectFromProfessor(Long professorId, Long subjectId) {
        Professor professor = professorRepository.findById(professorId).orElseThrow(()-> new ProfessorNotFoundExeption("Professor with id "+professorId+" not found to delete subject"));
        Subject subject =subjectRepository.findById(subjectId).orElseThrow(()-> new SubjectNotFoundExeption("Subject with id "+subjectId+" not found to delete from professor"));
        professor.getSubjects().remove(subject);
        subject.setProfessor(null);
        subjectRepository.save(subject);
        return professorRepository.save(professor);
    }
}