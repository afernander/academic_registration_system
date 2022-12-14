package com.perficient.path.practice.academic_registration_system.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.perficient.path.practice.academic_registration_system.models.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    Page<Professor> findByAreaContaining(Pageable pageable,String area);
    Page<Professor> findBySpecializationContaining(Pageable pageable,String specialization);
}