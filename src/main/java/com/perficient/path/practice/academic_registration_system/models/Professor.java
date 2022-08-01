package com.perficient.path.practice.academic_registration_system.models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
@Table(name = "professors")
public class Professor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Professor area is mandatory")
    private String area;
    private String specialization;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "professor")
    private Set<Subject> subjects;

    @OneToOne
    private User user;

}