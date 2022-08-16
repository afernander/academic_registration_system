package com.perficient.path.practice.academic_registration_system.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.perficient.path.practice.academic_registration_system.models.Subject;

public interface SubjectService {
    
    Subject getSubjectById(Long id);
    Page<Subject> getAllSubjects(int page, int size);
    Subject createSubject(Subject subject);
    Subject updateSubject(Long id,Subject subject);
    void deleteSubjectById(Long id);
    Page<Subject> getSubjectsByName(int page, int size,String name);
    Page<Subject> getSubjectsByArea(int page, int size,String area);
    Page<Subject> getSubjectsByCredits(int page, int size,Integer credits);
    List<Subject> getSubjectsByCourseId(Long courseId);
    List<Subject> getSubjectsByProfessorId(Long professorId);
}