package com.perficient.path.practice.academic_registration_system.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Page<Professor> getAllProfessors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Professor> professorPage = professorRepository.findAll(pageable);
        if(professorPage.isEmpty()){
            throw new ProfessorNotFoundExeption("No professors found");
        }
        return professorPage;
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
    public Page<Professor> getProfessorsByArea(int page, int size,String area) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Professor> professorPage = professorRepository.findByAreaContaining(pageable,area);
        if(professorPage.isEmpty()) {
            throw new ProfessorNotFoundExeption("No professors found with area : "+area);
        }
        return professorPage;
    }

    @Override
    public Page<Professor> getProfessorsBySpecialization(int page, int size,String specialization) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Professor> professorPage = professorRepository.findBySpecializationContaining(pageable,specialization);
        if(professorPage.isEmpty()) {
            throw new ProfessorNotFoundExeption("No professors found with specialization : "+specialization);
        }
        return professorPage;
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