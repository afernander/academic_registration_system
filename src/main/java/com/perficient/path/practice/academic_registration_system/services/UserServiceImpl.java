package com.perficient.path.practice.academic_registration_system.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.perficient.path.practice.academic_registration_system.errors.CourseNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.errors.DuplicatedDataExeption;
import com.perficient.path.practice.academic_registration_system.errors.ProfessorNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.errors.UserNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.models.Course;
import com.perficient.path.practice.academic_registration_system.models.Professor;
import com.perficient.path.practice.academic_registration_system.models.User;
import com.perficient.path.practice.academic_registration_system.repositories.CourseRepository;
import com.perficient.path.practice.academic_registration_system.repositories.ProfessorRepository;
import com.perficient.path.practice.academic_registration_system.repositories.UserRepository;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final CourseRepository courseRepository;

    private final ProfessorRepository professorRepository;


    public UserServiceImpl(UserRepository userRepository, CourseRepository courseRepository, ProfessorRepository professorRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.professorRepository = professorRepository;
    }
    
    @Override
    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundExeption("User with id "+ id+ " not found"));
    }

    @Override
    public Page<User> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageable);
        if(userPage.isEmpty()){
            throw new UserNotFoundExeption("No users found");
        }
        return userPage;
    }

    @Override
    public User createUser(User user) {
        try{
            userRepository.save(user);
        }catch(Exception e){
            throw new DuplicatedDataExeption("User with name "+ user.getFirstName()+ " already exists");
        }
        return user;
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

    @Override
    public User addCourseToUser(Long userId, Long courseId) {
        User userToUpdate = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundExeption("User with id "+ userId+ " not found to update"));
        Course courseToAdd = courseRepository.findById(courseId).orElseThrow(() -> new CourseNotFoundExeption("Course with id "+ courseId+ " not found to add"));
        userToUpdate.getCourses().add(courseToAdd);
        return userRepository.save(userToUpdate);
    }

    @Override
    public Page<User> getUsersByFirstName(int page, int size,String name) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findByFirstNameContaining(pageable,name);
        if(userPage.isEmpty()) {
            throw new UserNotFoundExeption("User with name "+ name+ " not found");
        }
        return userPage;
    }

    @Override
    public List<User> getUsersByCourseId(Long courseId) {
        if(!courseRepository.existsById(courseId)){
            throw new CourseNotFoundExeption("Course with id "+ courseId+ " not found to get users");
        }
        List<User> users = userRepository.findUsersByCoursesId(courseId);
        if(users.isEmpty()){
            throw new UserNotFoundExeption("Course with id "+ courseId+ " has no users");
        }
        return users;
    }

    @Override
    public User deleteCourseFromUser(Long userId, Long courseId) {
        User userToUpdate = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundExeption("User with id "+ userId+ " not found to update"));
        Course courseToDelete = courseRepository.findById(courseId).orElseThrow(() -> new CourseNotFoundExeption("Course with id "+ courseId+ " not found to delete"));
        userToUpdate.getCourses().remove(courseToDelete);
        return userRepository.save(userToUpdate);
    }

    @Override
    public User addProfessorToUser(Long userId, Long professorId) {
        User userToUpdate = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundExeption("User with id "+ userId+ " not found to update"));
        Professor professorToAdd = professorRepository.findById(professorId).orElseThrow(() -> new ProfessorNotFoundExeption("Professor with id "+ professorId+ " not found to add"));
        userToUpdate.setProfessor(professorToAdd);
        professorToAdd.setUser(userToUpdate);
        professorRepository.save(professorToAdd);
        return userRepository.save(userToUpdate);
    }

    @Override
    public User deleteProfessorFromUser(Long userId, Long professorId) {
        User userToUpdate = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundExeption("User with id "+ userId+ " not found to update"));
        Professor professorToDelete = professorRepository.findById(professorId).orElseThrow(() -> new ProfessorNotFoundExeption("Professor with id "+ professorId+ " not found to delete"));
        userToUpdate.setProfessor(null);
        professorToDelete.setUser(null);
        professorRepository.save(professorToDelete);
        return userRepository.save(userToUpdate);
    }
}
    