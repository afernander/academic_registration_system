package com.perficient.path.practice.academic_registration_system.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.perficient.path.practice.academic_registration_system.models.User;
import com.perficient.path.practice.academic_registration_system.repositories.UserRepository;
import com.perficient.path.practice.academic_registration_system.services.JwtUserDetailsService;
import com.perficient.path.practice.academic_registration_system.util.JwtTokenUtil;

public class AuthenticationControllerTest {

    MockMvc mockMvc;

    @Mock
    UserRepository userRepository;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtUserDetailsService userDetailsService;

    @Mock
    JwtTokenUtil jwtTokenUtil;

    AuthenticationController authenticationController;

    User userTest = new User();

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        
        userTest.setId(1L);
        userTest.setFirstName("John");
        userTest.setMiddleName("Doe");
        userTest.setFirstSurname("Smith");
        userTest.setEmail("test@gmail.com");
        userTest.setPassword("PASSWORD");


        authenticationController = new AuthenticationController(userRepository, authenticationManager, userDetailsService, jwtTokenUtil);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }


    @Test
    void loginUserTest() {

    }

    @Test
    void SaveUserTest() {

    }
}
