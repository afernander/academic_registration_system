package com.perficient.path.practice.academic_registration_system.models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Table(name= "users")
@Data
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    private String middleName;
    private String firstSurname;
    private String secondSurname;

    @NotNull(message = "Email is mandatory")
    private String email;

    @NotNull(message = "Password is mandatory")
    private String password;

    private String bornDate;

    @NotNull(message = "Role is required")
    private String role;

    @ManyToMany
    @JoinTable(name = "user_course", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses;

    @OneToOne(cascade = CascadeType.ALL)
    private Professor professor;
}