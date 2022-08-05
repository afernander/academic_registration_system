package com.perficient.path.practice.academic_registration_system.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.perficient.path.practice.academic_registration_system.repositories.UserRepository;
import com.perficient.path.practice.academic_registration_system.models.User;

public class UserServiceImplTest {

    UserService userService;

    @Mock
    UserRepository userRepository;
    

    User userTest = new User();

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        userTest.setId(1L);
        userTest.setFirstName("John");
        userTest.setMiddleName("Doe");
        userTest.setFirstSurname("Smith");
        userTest.setEmail("test@gmail.com");
        userTest.setPassword("PASSWORD");
        userTest.setRole("student");

        userService = new UserServiceImpl(userRepository);
    }


    @Test
    void testGetUserById() {
        User user = new User();
        user.setId(1L);

        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findById(1L)).thenReturn(userOptional);
        
        User userById = userService.getUserById(1L);

        assertNotNull(userById, "User should not be null");
        assertEquals(userOptional.get(), userById);
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void getAllUsersTest(){
        User user = new User();
        user.setId(1L);
        List<User> users = new ArrayList<>();
        users.add(user);
        
        when(userRepository.findAll()).thenReturn(users);

        Set<User> allUsers = userService.getAllUsers();

        assertEquals(1, allUsers.size());
        verify(userRepository, times(1)).findAll();
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    void createUserTest(){
        User user = userTest;
    
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser,"User should not be null");
        assertEquals(user, createdUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUserTest(){
        User user = userTest;
        User updatedUser = userTest;
        updatedUser.setFirstName("Updated");
        updatedUser.setMiddleName("Updated");
        updatedUser.setFirstSurname("Updated");
        updatedUser.setEmail("Updated@gmail.com");
        updatedUser.setPassword("Updated123");
        updatedUser.setRole("Updated");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        
        User updatedUserById = userService.updateUser(1L, updatedUser);
        
        assertNotNull(updatedUserById, "User should not be null");
        assertEquals(updatedUser, updatedUserById);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    void deleteUserByIdTest(){
        User user = userTest;
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(1L);
        
        userService.deleteUserById(1L);
        
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(user);
    }
}
