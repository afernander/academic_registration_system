package com.perficient.path.practice.academic_registration_system.models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
@Table(name = "subjects")
public class Subject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Subject name is mandatory")
    private String name;

    @NotNull(message = "Subject description is required")
    @Lob
    private String description;

    @NotNull(message = "Subject area is required")
    private String area;

    @NotNull(message = "Subject credits is required")
    private Integer credits;

    @Lob
    private String contents;
    
    private String prerequisites;
    private String corequisites;

    @ManyToMany(mappedBy = "subjects")
    private Set<Course> courses;

    @ManyToOne
    private Professor professor;

}