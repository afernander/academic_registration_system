package com.perficient.path.practice.academic_registration_system.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.perficient.path.practice.academic_registration_system.errors.SubjectNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.models.Subject;
import com.perficient.path.practice.academic_registration_system.repositories.SubjectRepository;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id).orElseThrow(()->new SubjectNotFoundExeption("Subject with id "+id+" not found"));
    }

    @Override
    public Set<Subject> getAllSubjects() {
        Set<Subject> subjectsSet = new HashSet<>();

        subjectRepository.findAll().iterator().forEachRemaining(subjectsSet::add);
        return subjectsSet;
    }

    @Override
    public Subject createSubject(Subject subject) {
        return subjectRepository.save(subject);
    }
    
}