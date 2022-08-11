package com.perficient.path.practice.academic_registration_system.services;

import java.util.List;
import java.util.Set;

import com.perficient.path.practice.academic_registration_system.models.User;

public interface UserService {

    User getUserById(Long id);
    Set<User> getAllUsers();
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUserById(Long id);
    User addCourseToUser(Long userId, Long courseId);
    Set<User> getUsersByFirstName(String name);
    List<User> getUsersByCourseId(Long courseId);
    User deleteCourseFromUser(Long userId, Long courseId);
}