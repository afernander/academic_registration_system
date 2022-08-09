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
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.perficient.path.practice.academic_registration_system.repositories.CourseRepository;
import com.perficient.path.practice.academic_registration_system.repositories.UserRepository;
import com.perficient.path.practice.academic_registration_system.errors.CourseNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.errors.UserNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.models.Course;
import com.perficient.path.practice.academic_registration_system.models.User;

public class UserServiceImplTest {

    UserService userService;

    @Mock
    UserRepository userRepository;
    
    @Mock
    CourseRepository courseRepository;

    User userTest = new User();

    Course courseTest = new Course();

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

        courseTest.setId(1L);
        courseTest.setName("Java");
        courseTest.setDescription("Java is a programming language");

        userService = new UserServiceImpl(userRepository, courseRepository);
    }


    @Test
    void getUserByIdTest() {
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

    @Test
    void getUserByIdUserNotFoundTest(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(UserNotFoundExeption.class, () -> userService.getUserById(1L));
    }

    @Test
    void updateUserUserNotFoundTest(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        User user = userTest;
        
        assertThrows(UserNotFoundExeption.class, () -> userService.updateUser(1L,user));
    }

    @Test
    void deleteUserByIdNotFoundTest(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(UserNotFoundExeption.class, () -> userService.deleteUserById(1L));
    }

    @Test
    void addCourseToUserTest() throws Exception{
        Long userId = userTest.getId();
        Long courseId = courseTest.getId();
        User user = userTest;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(courseTest));

        user.getCourses().add(courseTest);
        when(userRepository.save(user)).thenReturn(user);
        User userRetruned=userService.addCourseToUser(userId, courseId);

        assertNotNull(userRetruned, "User should not be null");
        assertEquals(user, userRetruned);
        verify(userRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
        verify(userRepository, never()).findAll();
        verify(courseRepository, never()).findAll();
    }

    @Test
    void addCourseToUser_UserNotFoundTest() throws Exception{
        Long userId = userTest.getId();
        Long courseId = courseTest.getId();
        User user = userTest;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(courseTest));

        assertThrows(UserNotFoundExeption.class, () -> userService.addCourseToUser(userId, courseId));
    }

    @Test
    void addCourseToUser_CourseNotFoundTest() throws Exception{
        Long userId = userTest.getId();
        Long courseId = courseTest.getId();
        User user = userTest;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundExeption.class, () -> userService.addCourseToUser(userId, courseId));
    }

    @Test
    void getCoursesByUserId() throws Exception{
        Long userId = userTest.getId();
        User user = userTest;
        user.getCourses().add(courseTest);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Set<Course> courses = userService.getCoursesByUserId(userId);
        assertNotNull(courses, "Courses should not be null");
        assertEquals(1, courses.size());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).findAll();
        verify(courseRepository, never()).findAll();
    }

    @Test
    void getCoursesByUserId_UserNotFoundTest() throws Exception{
        Long userId = userTest.getId();
        User user = userTest;
        user.getCourses().add(courseTest);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundExeption.class, () -> userService.getCoursesByUserId(userId));
    }

    @Test
    void getCoursesByUserId_CourseNotFoundTest() throws Exception{
        Long userId = userTest.getId();
        User user = userTest;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        assertThrows(CourseNotFoundExeption.class, () -> userService.getCoursesByUserId(userId));
    }
}
