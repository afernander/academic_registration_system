package com.perficient.path.practice.academic_registration_system.models;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Course name is mandatory")
    private String name;

    @NotNull(message = "Course description is required")
    @Lob
    private String description;

    private Double duration;

    @Enumerated(EnumType.STRING)
    private DurationType durationType;

    @Digits(integer = 8, fraction = 2)
    private BigDecimal price;

    @ManyToMany(mappedBy = "courses")
    private Set<User> users;

    @ManyToMany
    @JoinTable(name = "course_subject", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<Subject> subjects;


    public enum DurationType{
        YEARS,
        MONTHS,
        DAYS,
        WEEKS,
        HOURS
    }
}