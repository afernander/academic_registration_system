package com.perficient.path.practice.academic_registration_system.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.perficient.path.practice.academic_registration_system.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findUsersByCoursesId(Long courseId);
    Page<User> findByFirstNameContaining(Pageable pageable, String name);
    User findByEmail(String email);
}