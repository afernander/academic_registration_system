package com.perficient.path.practice.academic_registration_system.services;

import java.util.ArrayList;
import java.util.HashSet;
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
    

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

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
        List<User> users = new ArrayList();
        users.add(user);
        
        when(userRepository.findAll()).thenReturn(users);

        Set<User> allUsers = userService.getAllUsers();

        assertEquals(1, allUsers.size());
        verify(userRepository, times(1)).findAll();
        verify(userRepository, never()).findById(anyLong());
    }
}
