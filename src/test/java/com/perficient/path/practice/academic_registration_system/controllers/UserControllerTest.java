package com.perficient.path.practice.academic_registration_system.controllers;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perficient.path.practice.academic_registration_system.models.Course;
import com.perficient.path.practice.academic_registration_system.models.User;
import com.perficient.path.practice.academic_registration_system.repositories.CourseRepository;
import com.perficient.path.practice.academic_registration_system.repositories.UserRepository;
import com.perficient.path.practice.academic_registration_system.services.UserService;

public class UserControllerTest {

    MockMvc mockMvc;

    @Mock
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    CourseRepository courseRepository;

    UserController userController;

    User userTest = new User();

    Course courseTest = new Course();

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        
        userTest.setId(1L);
        userTest.setFirstName("John");
        userTest.setMiddleName("Doe");
        userTest.setFirstSurname("Smith");
        userTest.setEmail("test@gmail.com");
        userTest.setPassword("PASSWORD");
        userTest.setRole("student");

        courseTest.setId(1L);
        courseTest.setName("Java");
        courseTest.setDescription("Java is a programming language");

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
        User user = userTest;
        
        ObjectMapper mapper = new ObjectMapper();
        String  content = mapper.writeValueAsString(user);

        when(userService.createUser(user)).thenReturn(user);

    
        mockMvc.perform(post("/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.middleName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstSurname").value("Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("PASSWORD"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("student"));
    }

    @Test
    void updateUserTest() throws Exception {
        Long userId = userTest.getId();

        User user = userTest;

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        user.setFirstName("Alejandro");
        user.setMiddleName("Fernandez");

        when(userService.updateUser(userId,user)).thenReturn(user);

        ObjectMapper mapper = new ObjectMapper();
        String  content = mapper.writeValueAsString(user);

        mockMvc.perform(post("/users/"+userId+"/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Alejandro"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.middleName").value("Fernandez"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstSurname").value("Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("PASSWORD"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("student"));
    }

    @Test
    void deleteUserTest() throws Exception {
        Long userId = userTest.getId();
        User user = userTest;

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(delete(("/users/"+userId+"/delete")))
                .andExpect(status().isOk());
    }

    @Test
    void addCourseToUserTest() throws Exception {
        Long userId = userTest.getId();
        Long courseId = courseTest.getId();
        User user = userTest;

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseTest));

        user.getCourses().add(courseTest);
        when(userService.addCourseToUser(userId,courseId)).thenReturn(user);
        
        mockMvc.perform(get("/users/"+userId+"/add/course/"+courseId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"));
    }

    @Test
    void getUsersByFistNameTest() throws Exception{
        String firstName = userTest.getFirstName();
        Set<User> users = new HashSet<>();
        users.add(userTest);
        User user = new User();
        user.setId(2L);
        user.setFirstName("PAOLO");
        users.add(user);

        Set<User> usersByFirstName = users.stream().filter(u -> u.getFirstName().toLowerCase().contains(firstName.toLowerCase())).collect(Collectors.toSet());
        when(userService.getUsersByFirstName(firstName)).thenReturn(usersByFirstName);
        
        mockMvc.perform(get("/users/"+firstName+"/search/firstname"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value(firstName));
    }

   @Test
   void getUsersByCourseIdTest() throws Exception{
        Long courseId = courseTest.getId();
        User user = userTest;
        List<User> users = new ArrayList<>();
        user.getCourses().add(courseTest);
        users.add(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseTest));
        when(userService.getUsersByCourseId(courseId)).thenReturn(users);

        mockMvc.perform(get("/users/"+courseId+"/search/byCourseId"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("John"));
   }    
   
    @Test
    void deleteCourseFromUserTest() throws Exception{
        Long userId = userTest.getId();
        Long courseId = courseTest.getId();
        User user = userTest;
        user.getCourses().add(courseTest);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseTest));
        when(userService.deleteCourseFromUser(userId,courseId)).thenReturn(user);
        
        mockMvc.perform(delete("/users/"+userId+"/delete/course/"+courseId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"));
    }
}
