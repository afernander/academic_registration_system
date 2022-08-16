package com.perficient.path.practice.academic_registration_system.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.perficient.path.practice.academic_registration_system.models.User;

public interface UserService {

    User getUserById(Long id);
    Page<User> getAllUsers(int page, int size);
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUserById(Long id);
    User addCourseToUser(Long userId, Long courseId);
    Page<User> getUsersByFirstName(int page, int size,String name);
    List<User> getUsersByCourseId(Long courseId);
    User deleteCourseFromUser(Long userId, Long courseId);
    User addProfessorToUser(Long userId, Long professorId);
    User deleteProfessorFromUser(Long userId, Long professorId);
}