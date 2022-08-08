package com.perficient.path.practice.academic_registration_system.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {
    User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    public void getIdTest(){
        Long idValue = 1L;
        user.setId(idValue);
        assertEquals(idValue, user.getId());
    }

    @Test
    public void getFirstNameTest(){
        String firstNameValue = "John";
        user.setFirstName(firstNameValue);
        assertEquals(firstNameValue, user.getFirstName());
    }

    @Test
    public void userEqualsTrueTest(){
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("John");
        User user2 = new User();
        user2.setId(1L);
        user2.setFirstName("John");
        assertEquals(user1, user2);
        assertTrue(user1.equals(user2));
    }

    @Test
    public void userEqualsFlaseTest(){
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("John");
        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("John");
        assertFalse(user1.equals(user2));
    }

    
}