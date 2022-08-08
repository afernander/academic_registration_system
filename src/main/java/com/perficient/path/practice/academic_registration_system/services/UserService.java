package com.perficient.path.practice.academic_registration_system.services;

import java.util.Set;

//import com.perficient.path.practice.academic_registration_system.models.Course;
import com.perficient.path.practice.academic_registration_system.models.User;

public interface UserService {

    User getUserById(Long id);
    Set<User> getAllUsers();
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUserById(Long id);
    // Todo Set<Course> getCoursesByUserId(Long userId);
}