package com.perficient.path.practice.academic_registration_system.services;

import java.util.Set;

import com.perficient.path.practice.academic_registration_system.models.Course;
import com.perficient.path.practice.academic_registration_system.models.Subject;

public interface SubjectService {
    
    Subject getSubjectById(Long id);
    Set<Subject> getAllSubjects();
    Subject createSubject(Subject subject);
    Subject updateSubject(Long id,Subject subject);
    void deleteSubjectById(Long id);
    Set<Subject> getSubjectsByName(String name);
    Set<Subject> getSubjectsByArea(String area);
    Set<Subject> getSubjectsByCredits(Integer credits);
    //Set<Subject> getSubjectsByProfessorId(Long professorId);
    //Set<Course> getCoursesBySubjectId(Long subjectId);
}