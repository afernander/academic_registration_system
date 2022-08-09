package com.perficient.path.practice.academic_registration_system.controllers;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;


import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perficient.path.practice.academic_registration_system.models.Professor;
import com.perficient.path.practice.academic_registration_system.repositories.ProfessorRepository;
import com.perficient.path.practice.academic_registration_system.services.ProfessorService;

public class ProfessorControllerTest {

    MockMvc mockMvc;

    @Mock
    ProfessorService professorService;

    @Mock
    ProfessorRepository professorRepository;

    ProfessorController professorController;

    Professor professorTest= new Professor();

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        professorTest.setId(1L);
        professorTest.setArea("Computer Science");
        professorTest.setSpecialization("Software Engineering");

        professorController = new ProfessorController(professorService);
        mockMvc = MockMvcBuilders.standaloneSetup(professorController).build();
    }

    @Test
    void getProfessorByIdTest() throws Exception {
        Professor professor = professorTest;
        when(professorService.getProfessorById(1L)).thenReturn(professor);

        mockMvc.perform(get("/professors/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.area").value("Computer Science"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.specialization").value("Software Engineering"));
    }

    @Test
    void getAllProfessorsTest() throws Exception {
        Set<Professor> professors = new HashSet<>();
        professors.add(professorTest);
        Professor professor = new Professor();
        professor.setId(2L);
        professor.setArea("Music");
        professor.setSpecialization("Flute");
        professors.add(professor);

        when(professorService.getAllProfessors()).thenReturn(professors);
        
        mockMvc.perform(get("/professors/all"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].area").value("Computer Science"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].specialization").value("Software Engineering"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].area").value("Music"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].specialization").value("Flute"));
    }

    @Test
    void createProfessorTest() throws Exception {
        Professor professor = professorTest;

        ObjectMapper mapper = new ObjectMapper();
        String professorJson = mapper.writeValueAsString(professor);
        when(professorService.createProfessor(professor)).thenReturn(professor);

        
        mockMvc.perform(post("/professors/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(professorJson))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.area").value("Computer Science"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.specialization").value("Software Engineering"));
    }

    @Test
    void updateProfessorTest() throws Exception {
        Professor professor = professorTest;
        Long id = professor.getId();
        
        when(professorRepository.findById(id)).thenReturn(Optional.of(professor));
        when(professorRepository.save(professor)).thenReturn(professor);

        professor.setArea("Music");
        professor.setSpecialization("Flute");

        when(professorService.updateProfessor(id,professor)).thenReturn(professor);

        ObjectMapper mapper = new ObjectMapper();
        String professorJson = mapper.writeValueAsString(professor);
        mockMvc.perform(post("/professors/"+id+"/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(professorJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.area").value("Music"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.specialization").value("Flute"));
    }

    @Test
    void deleteProfessorTest() throws Exception {
        Long id = professorTest.getId();
        when(professorRepository.findById(id)).thenReturn(Optional.of(professorTest));
        mockMvc.perform(delete("/professors/"+id+"/delete"))
                .andExpect(status().isOk());
    }

    @Test
    void getProfessorsByArea() throws Exception{
        String area = "Science";
        Set<Professor> professors = new HashSet<>();
        professors.add(professorTest);
        Professor professor = new Professor();
        professor.setId(2L);
        professor.setArea("Music");
        professors.add(professor);

        Set<Professor> professorsByArea = professors.stream().filter(p -> p.getArea().toLowerCase().contains(area.toLowerCase())).collect(Collectors.toSet());
        when(professorService.getProfessorsByArea(area)).thenReturn(professorsByArea);
       
        mockMvc.perform(get("/professors/"+area+"/search/area"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].area").value("Computer Science"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].specialization").value("Software Engineering"));
    }

    @Test
    void getProfessorsBySpecialization() throws Exception{
        String specialization = "Software";
        Set<Professor> professors = new HashSet<>();
        professors.add(professorTest);
        Professor professor = new Professor();
        professor.setId(2L);
        professor.setSpecialization("Fluid");
        professors.add(professor);

        Set<Professor> professorsBySpecialization = professors.stream().filter(p -> p.getSpecialization().toLowerCase().contains(specialization.toLowerCase())).collect(Collectors.toSet());
        when(professorService.getProfessorsBySpecialization(specialization)).thenReturn(professorsBySpecialization);
       
        mockMvc.perform(get("/professors/"+specialization+"/search/specialization"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].area").value("Computer Science"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].specialization").value("Software Engineering"));
    }
}
