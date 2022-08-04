package com.perficient.path.practice.academic_registration_system.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import java.util.HashSet;
import java.util.Set;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perficient.path.practice.academic_registration_system.models.User;
import com.perficient.path.practice.academic_registration_system.services.UserService;

public class UserControllerTest {

    MockMvc mockMvc;

    @Mock
    UserService userService;

    UserController userController;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        
        userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getUserByIdTest() throws Exception {
        User user = new User();
        user.setId(1L);

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
        
    }

    @Test
    void getAllUsersTest() throws Exception {

        Set<User> users = new HashSet<>();
        
        User user = new User();
        user.setId(1L);
        users.add(user);

        User user2 = new User();
        user2.setId(2L);
        users.add(user2);
       
        
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*]",hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(1));  
    }

    @Test
    void createUserTest() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setMiddleName("Doe");
        user.setFirstSurname("Smith");
        user.setEmail("test@gmail.com");
        user.setPassword("PASSWORD");
        user.setRole("student");
        
        ObjectMapper mapper = new ObjectMapper();
        String  content = mapper.writeValueAsString(user);

        when(userService.createUser(user)).thenReturn(user);

    
        mockMvc.perform(post("/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"firstName\":\"John\",\"middleName\":\"Doe\",\"firstSurname\":\"Smith\",\"email\":\"test@gmail.com\",\"password\":\"PASSWORD\",\"role\":\"student\"}"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.middleName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstSurname").value("Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("PASSWORD"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("student"));

    }
}
