package com.perficient.path.practice.academic_registration_system.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.perficient.path.practice.academic_registration_system.repositories.CourseRepository;
import com.perficient.path.practice.academic_registration_system.repositories.ProfessorRepository;
import com.perficient.path.practice.academic_registration_system.repositories.UserRepository;
import com.perficient.path.practice.academic_registration_system.errors.CourseNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.errors.DuplicatedDataExeption;
import com.perficient.path.practice.academic_registration_system.errors.ProfessorNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.errors.UserNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.models.Course;
import com.perficient.path.practice.academic_registration_system.models.Professor;
import com.perficient.path.practice.academic_registration_system.models.User;

public class UserServiceImplTest {

    UserService userService;

    @Mock
    UserRepository userRepository;
    
    @Mock
    CourseRepository courseRepository;

    @Mock
    ProfessorRepository professorRepository;

    User userTest = new User();

    Course courseTest = new Course();

    Professor professorTest = new Professor();

    int page = 0;
    int size = 3;
    Pageable pageable = PageRequest.of(page, size);

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

        professorTest.setId(1L);
        professorTest.setArea("Computer Science");
        professorTest.setSpecialization("Data Science");

        userService = new UserServiceImpl(userRepository, courseRepository, professorRepository);
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
        Page<User> usersPage = new PageImpl<>(users);
        when(userRepository.findAll(pageable)).thenReturn(usersPage);

        Page<User> allUsers = userService.getAllUsers(page, size);

        assertNotNull(allUsers, "Users should not be null");
        verify(userRepository, times(1)).findAll(pageable);
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    void getAllUsers_NotFoundTest(){
        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(new ArrayList<>()));
        assertThrows(UserNotFoundExeption.class, () -> userService.getAllUsers(page, size));
        verify(userRepository, times(1)).findAll(pageable);
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
    void createUser_DuplicatedDataExeptionTest()throws DuplicatedDataExeption{
        User user = userTest;
        when(userRepository.save(user)).thenThrow(DuplicatedDataExeption.class);
        assertThrows(DuplicatedDataExeption.class, () -> userService.createUser(user));
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
    void deleteCourseFromUserTest() throws Exception{
        Long userId = userTest.getId();
        Long courseId = courseTest.getId();
        User user = userTest;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(courseTest));

        user.getCourses().remove(courseTest);
        userService.deleteCourseFromUser(userId, courseId);
        when(userRepository.save(user)).thenReturn(user);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
        verify(userRepository, never()).findAll();
        verify(courseRepository, never()).findAll();
    }

    @Test
    void deleteCourseFromUser_UserNotFoundTest() throws Exception{
        Long userId = userTest.getId();
        Long courseId = courseTest.getId();
        User user = userTest;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
          
        assertThrows(UserNotFoundExeption.class, () -> userService.deleteCourseFromUser(userId, courseId));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(user);
        verify(userRepository, never()).findAll();
        verify(courseRepository, never()).findAll();
    }

    @Test
    void deleteCourseFromUser_CourseNotFoundTest() throws Exception{
        Long userId = userTest.getId();
        Long courseId = courseTest.getId();
        User user = userTest;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
        assertThrows(CourseNotFoundExeption.class, () -> userService.deleteCourseFromUser(userId, courseId));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(user);
        verify(userRepository, never()).findAll();
        verify(courseRepository, never()).findAll();
    }


    @Test
    void getUsersByCourseId() throws Exception{
        Long courseId = courseTest.getId();
        User user = userTest;
        user.getCourses().add(courseTest);
        List<User> users = new ArrayList<>();
        users.add(user);
        
        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(userRepository.findUsersByCoursesId(courseId)).thenReturn(users);

        List<User> usersByCourseId = userService.getUsersByCourseId(courseId);

        assertNotNull(usersByCourseId, "Users should not be null");
        assertEquals(1, usersByCourseId.size());
        assertEquals(users, usersByCourseId);
        verify(courseRepository, times(1)).existsById(courseId);
        verify(userRepository, times(1)).findUsersByCoursesId(courseId);
        verify(userRepository, never()).findAll();
        verify(courseRepository, never()).findAll();

    }

    @Test
    void getUsersByFirstNameTest(){
        String firstName = userTest.getFirstName();
        User user = new User();
        user.setId(2L);
        user.setFirstName("PEPE");
        List<User> users = new ArrayList<>();
        users.add(userTest);
        users.add(user);

        List<User> usersByFirstName = users.stream().filter(u -> u.getFirstName().toLowerCase().contains(firstName.toLowerCase())).collect(Collectors.toList());
        Page<User> usersPage = new PageImpl<>(usersByFirstName);
        when(userRepository.findByFirstNameContaining(pageable,firstName)).thenReturn(usersPage);

        Page<User> usersReturned = userService.getUsersByFirstName(page,size,firstName);

        assertNotNull(usersReturned, "Users should not be null");
        verify(userRepository, times(1)).findByFirstNameContaining(pageable,firstName);
        verify(userRepository, never()).findAll();
        verify(courseRepository, never()).findAll();
    }

    @Test
    void getUsersByFirstNameNotFoundTest(){
        String userName = "Jonh";
        when(userRepository.findByFirstNameContaining(pageable,userName)).thenReturn(new PageImpl<>(new ArrayList<>()));
        assertThrows(Exception.class, () -> userService.getUsersByFirstName(page,size,userName));
    }

    @Test
    void getUsersByCourseId_CourseNotFoundTest() throws Exception{
        Long courseId = courseTest.getId();
        User user = userTest;
        user.getCourses().add(courseTest);
        List<User> users = new ArrayList<>();
        users.add(user);
        
        when(courseRepository.existsById(courseId)).thenReturn(false);
        when(userRepository.findUsersByCoursesId(courseId)).thenReturn(users);

        assertThrows(CourseNotFoundExeption.class, () -> userService.getUsersByCourseId(courseId));
    }

    @Test
    void getUsersByCourseId_UserNotFoundTest() throws Exception{
        Long courseId = courseTest.getId();
        User user = userTest;
        user.getCourses().add(courseTest);
        List<User> users = new ArrayList<>();
        
        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(userRepository.findUsersByCoursesId(courseId)).thenReturn(users);

        assertThrows(UserNotFoundExeption.class, () -> userService.getUsersByCourseId(courseId));
    }

    @Test
    void addProfessorToUserTest() throws Exception{
        Long userId = userTest.getId();
        Long professorId = professorTest.getId();
        User user = userTest;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(professorRepository.findById(professorId)).thenReturn(Optional.of(professorTest));
        userService.addProfessorToUser(userId, professorId);
        user.setProfessor(professorTest);
        when(userRepository.save(user)).thenReturn(user);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
        verify(professorRepository, never()).findAll();
        verify(professorRepository, times(1)).findById(professorId);
    }

    @Test
    void addProfessorToUser_UserNotFoundTest() throws Exception{
        Long userId = userTest.getId();
        Long professorId = professorTest.getId();
        User user = userTest;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundExeption.class, () -> userService.addProfessorToUser(userId, professorId));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(user);
        verify(professorRepository, never()).findAll();
        verify(professorRepository, never()).findById(professorId);
    }

    @Test
    void addProfessorToUser_ProfessorNotFoundTest() throws Exception{
        Long userId = userTest.getId();
        Long professorId = professorTest.getId();
        User user = userTest;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(professorRepository.findById(professorId)).thenReturn(Optional.empty());
        assertThrows(ProfessorNotFoundExeption.class, () -> userService.addProfessorToUser(userId, professorId));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(user);
        verify(professorRepository, never()).findAll();
        verify(professorRepository, times(1)).findById(professorId);
    }

    @Test
    void deleteProfessorFromUserTest() throws Exception{
        Long userId = userTest.getId();
        Long professorId = professorTest.getId();
        User user = userTest;
        Professor professor = professorTest;
        user.setProfessor(professor);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        userService.deleteProfessorFromUser(userId, professorId);
        user.setProfessor(null);
        professor.setUser(null);
        when(userRepository.save(user)).thenReturn(user);
        when(professorRepository.save(professor)).thenReturn(professor);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
        verify(professorRepository,times(1)).save(professor);
        verify(professorRepository, never()).findAll();
        verify(professorRepository, times(1)).findById(professorId);
    }

    @Test
    void deleteProfessorFromUser_UserNotFoundTest() throws Exception{
        Long userId = userTest.getId();
        Long professorId = professorTest.getId();
        User user = userTest;
        Professor professor = professorTest;
        user.setProfessor(professor);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundExeption.class, () -> userService.deleteProfessorFromUser(userId, professorId));
       verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(user);
        verify(professorRepository,never()).save(professor);
        verify(professorRepository, never()).findAll();
        verify(professorRepository, never()).findById(professorId);
    }

    @Test
    void deleteProfessorFromUser_ProfessorNotFoundTest() throws Exception{
        Long userId = userTest.getId();
        Long professorId = professorTest.getId();
        User user = userTest;
        Professor professor = professorTest;
        user.setProfessor(professor);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(professorRepository.findById(professorId)).thenReturn(Optional.empty());
        
        assertThrows(ProfessorNotFoundExeption.class, () -> userService.deleteProfessorFromUser(userId, professorId));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(user);
        verify(professorRepository,never()).save(professor);
        verify(professorRepository, never()).findAll();
        verify(professorRepository, times(1)).findById(professorId);
    }
}
