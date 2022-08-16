package com.perficient.path.practice.academic_registration_system.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.perficient.path.practice.academic_registration_system.models.Subject;
import com.perficient.path.practice.academic_registration_system.repositories.ProfessorRepository;
import com.perficient.path.practice.academic_registration_system.repositories.SubjectRepository;
import com.perficient.path.practice.academic_registration_system.services.ProfessorService;

public class ProfessorControllerTest {

    MockMvc mockMvc;

    @Mock
    ProfessorService professorService;

    @Mock
    ProfessorRepository professorRepository;

    @Mock
    SubjectRepository subjectRepository;

    ProfessorController professorController;

    Professor professorTest= new Professor();

    Subject subjectTest= new Subject();

    int page = 0;
    int size = 3;
    Pageable pageable = PageRequest.of(page, size);

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        professorTest.setId(1L);
        professorTest.setCode("PROF001");
        professorTest.setArea("Computer Science");
        professorTest.setSpecialization("Software Engineering");

        subjectTest.setId(1L);
        subjectTest.setName("Software Engineering");

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
        List<Professor> professors = new ArrayList<>();
        professors.add(professorTest);
        Professor professor = new Professor();
        professor.setId(2L);
        professor.setArea("Music");
        professor.setSpecialization("Flute");
        professors.add(professor);

        Page<Professor> professorPage = new PageImpl<>(professors);
        when(professorService.getAllProfessors(page, size)).thenReturn(professorPage);
       
        
        mockMvc.perform(get("/professors/all"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].area").value("Computer Science"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].specialization").value("Software Engineering"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].area").value("Music"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].specialization").value("Flute"));
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
        List<Professor> professors = new ArrayList<>();
        professors.add(professorTest);
        Professor professor = new Professor();
        professor.setId(2L);
        professor.setArea("Music");
        professors.add(professor);

        List<Professor> professorsByArea = professors.stream().filter(p -> p.getArea().toLowerCase().contains(area.toLowerCase())).collect(Collectors.toList());
        Page<Professor> professorPage = new PageImpl<>(professorsByArea);
        when(professorService.getProfessorsByArea(page, size,area)).thenReturn(professorPage);
       
        mockMvc.perform(get("/professors/"+area+"/search/area"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].area").value("Computer Science"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].specialization").value("Software Engineering"));
    }

    @Test
    void getProfessorsBySpecialization() throws Exception{
        String specialization = "Software";
        List<Professor> professors = new ArrayList<>();
        professors.add(professorTest);
        Professor professor = new Professor();
        professor.setId(2L);
        professor.setSpecialization("Fluid");
        professors.add(professor);

        List<Professor> professorsBySpecialization = professors.stream().filter(p -> p.getSpecialization().toLowerCase().contains(specialization.toLowerCase())).collect(Collectors.toList());
        Page<Professor> professorPage = new PageImpl<>(professorsBySpecialization);
        when(professorService.getProfessorsBySpecialization(page, size,specialization)).thenReturn(professorPage);

       
        mockMvc.perform(get("/professors/"+specialization+"/search/specialization"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].area").value("Computer Science"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].specialization").value("Software Engineering"));
    }

    @Test
    void addSubjectToProfessor() throws Exception{
        Long profesorid = professorTest.getId();
        Long subjectid = subjectTest.getId();
        Professor professor = professorTest;
        Subject subject = subjectTest;

        when(professorRepository.findById(profesorid)).thenReturn(Optional.of(professor));
        when(subjectRepository.findById(subjectid)).thenReturn(Optional.of(subject));
        professor.getSubjects().add(subject);
        subject.setProfessor(professor);
        when(subjectRepository.save(subject)).thenReturn(subject);
        when(professorRepository.save(professor)).thenReturn(professor);
        when(professorService.addSubjectToProfessor(profesorid,subjectid)).thenReturn(professor);
   
        mockMvc.perform(get("/professors/"+profesorid+"/add/subject/"+subjectid))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.area").value("Computer Science"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.specialization").value("Software Engineering"));
    }

    @Test
    void deleteSubjectFromProfessor() throws Exception{
        Long profesorid = professorTest.getId();
        Long subjectid = subjectTest.getId();
        Professor professor = professorTest;
        Subject subject = subjectTest;

        professor.getSubjects().add(subject);
        subject.setProfessor(professor);
        when(professorRepository.findById(profesorid)).thenReturn(Optional.of(professor));
        when(subjectRepository.findById(subjectid)).thenReturn(Optional.of(subject));
        professor.getSubjects().remove(subject);
        subject.setProfessor(null);
        when(subjectRepository.save(subject)).thenReturn(subject);
        when(professorRepository.save(professor)).thenReturn(professor);
        when(professorService.deleteSubjectFromProfessor(profesorid,subjectid)).thenReturn(professor);
    
        mockMvc.perform(delete("/professors/"+profesorid+"/delete/subject/"+subjectid))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.area").value("Computer Science"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.specialization").value("Software Engineering"));
    }
}
