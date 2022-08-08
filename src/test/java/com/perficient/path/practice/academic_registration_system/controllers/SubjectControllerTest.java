package com.perficient.path.practice.academic_registration_system.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perficient.path.practice.academic_registration_system.models.Subject;
import com.perficient.path.practice.academic_registration_system.repositories.SubjectRepository;
import com.perficient.path.practice.academic_registration_system.services.SubjectService;

public class SubjectControllerTest {

    MockMvc mockMvc;

    @Mock
    SubjectService subjectService;

    @Mock
    SubjectRepository subjectRepository;

    SubjectController subjectController;

    Subject subjectTest = new Subject();

    @BeforeEach
    public void setUp() {
       MockitoAnnotations.openMocks(this);

        subjectTest.setId(1L);
        subjectTest.setName("Calculus");
        subjectTest.setArea("Engineering");
        subjectTest.setDescription("Math is the best");
        subjectTest.setCredits(3);
        subjectTest.setContents("Algebra, Geometry, Trigonometry");
        subjectTest.setPrerequisites("Math");
        subjectTest.setCorequisites("None");

        subjectController = new SubjectController(subjectService);
        mockMvc = MockMvcBuilders.standaloneSetup(subjectController).build();
    }

    @Test
    public void getSubjectByIdTest() throws Exception {
        Subject subject = subjectTest;
        when(subjectService.getSubjectById(1L)).thenReturn(subject);
        mockMvc.perform(get("/subjects/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void getAllSubjectsTest() throws Exception {
        Set<Subject> subjects = new HashSet<>();

        subjects.add(subjectTest);
        Subject subject = new Subject();
        subject.setId(2L);
        subject.setName("Calculus 2");
        subjects.add(subject);

        when(subjectService.getAllSubjects()).thenReturn(subjects);

        mockMvc.perform(get("/subjects/all"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2));

    }

    @Test
    public void createSubjectTest() throws Exception {
        Subject subject = subjectTest;

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(subject);

        when(subjectService.createSubject(subject)).thenReturn(subject);

        mockMvc.perform(post("/subjects/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Calculus"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Math is the best"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.credits").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contents").value("Algebra, Geometry, Trigonometry"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.prerequisites").value("Math"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.corequisites").value("None"));
    }
}
