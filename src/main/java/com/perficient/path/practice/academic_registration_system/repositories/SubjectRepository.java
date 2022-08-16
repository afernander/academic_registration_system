package com.perficient.path.practice.academic_registration_system.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.perficient.path.practice.academic_registration_system.models.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Page<Subject> findByNameContaining(Pageable pageable, String name);
    Page<Subject> findByAreaContaining(Pageable pageable,String area);
    Page<Subject> findByCredits(Pageable pageable,Integer credits);
    List<Subject> findSubjectsByCoursesId(Long courseId);
    List<Subject> findSubjectsByProfessorId(Long professorId);
}