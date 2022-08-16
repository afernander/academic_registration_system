package com.perficient.path.practice.academic_registration_system.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.perficient.path.practice.academic_registration_system.models.User;
import com.perficient.path.practice.academic_registration_system.services.UserService;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/users")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        var user = userService.getUserById(Long.valueOf(id));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    @GetMapping(value="/all")
    public ResponseEntity<Page<User>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "3") int size) {
        Page<User> usersPage = userService.getAllUsers(page, size);
        return new ResponseEntity<>(usersPage, HttpStatus.OK);
    }

    @PostMapping(value="/create")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User newUser = userService.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping(value="/{id}/update")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping(value="/{id}/delete")
    public ResponseEntity<User> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value="/{name}/search/firstname")
    public ResponseEntity<Page<User>> getUsersByName(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "3") int size,
                                                    @PathVariable String name) {
        Page<User> users = userService.getUsersByFirstName(page,size,name);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    @GetMapping(value="/{userId}/add/course/{courseId}")
    public ResponseEntity<User> addCourseToUser(@PathVariable Long userId, @PathVariable Long courseId) {
        User updatedUser = userService.addCourseToUser(userId, courseId);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping(value="/{courseId}/search/byCourseId")
    public ResponseEntity<List<User>> getUsersByCourseId(@PathVariable Long courseId) {
        List<User> users = userService.getUsersByCourseId(courseId);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @DeleteMapping(value="/{userId}/delete/course/{courseId}")
    public ResponseEntity<User> deleteCourseFromUser(@PathVariable Long userId, @PathVariable Long courseId) {
        User updatedUser = userService.deleteCourseFromUser(userId, courseId);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping(value="/{userId}/add/professor/{professorId}")
    public ResponseEntity<User> addProfessorToUser(@PathVariable Long userId, @PathVariable Long professorId) {
        User updatedUser = userService.addProfessorToUser(userId, professorId);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping(value="/{userId}/delete/professor/{professorId}")
    public ResponseEntity<User> deleteProfessorFromUser(@PathVariable Long userId, @PathVariable Long professorId) {
        User updatedUser = userService.deleteProfessorFromUser(userId, professorId);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}