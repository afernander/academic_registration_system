package com.perficient.path.practice.academic_registration_system.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.perficient.path.practice.academic_registration_system.errors.UserNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.models.User;
import com.perficient.path.practice.academic_registration_system.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundExeption("User with id "+ id+ " not found"));
    }

    @Override
    public Set<User> getAllUsers() {
        Set<User> usersSet = new HashSet<>();

        userRepository.findAll().iterator().forEachRemaining(usersSet::add);
        return usersSet;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id,User user) {
        User userToUpdate = userRepository.findById(id).orElseThrow(() -> new UserNotFoundExeption("User with id "+ id+ " not found to update"));
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setMiddleName(user.getMiddleName());
        userToUpdate.setFirstSurname(user.getFirstSurname());
        userToUpdate.setSecondSurname(user.getSecondSurname());
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setBornDate(user.getBornDate());
        userToUpdate.setRole(user.getRole());
        return userRepository.save(userToUpdate);
    }

    @Override
    public void deleteUserById(Long id) {
        User userToDelete = userRepository.findById(id).orElseThrow(() -> new UserNotFoundExeption("User with id "+ id+ " not found to delete"));
        userRepository.delete(userToDelete);
    }

}
    