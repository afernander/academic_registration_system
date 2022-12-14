package com.perficient.path.practice.academic_registration_system.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perficient.path.practice.academic_registration_system.models.Course;
import com.perficient.path.practice.academic_registration_system.models.Professor;
import com.perficient.path.practice.academic_registration_system.models.Subject;
import com.perficient.path.practice.academic_registration_system.repositories.CourseRepository;
import com.perficient.path.practice.academic_registration_system.repositories.ProfessorRepository;
import com.perficient.path.practice.academic_registration_system.repositories.SubjectRepository;
import com.perficient.path.practice.academic_registration_system.services.SubjectService;

public class SubjectControllerTest {

    MockMvc mockMvc;

    @Mock
    SubjectService subjectService;

    @Mock
    SubjectRepository subjectRepository;

    @Mock
    CourseRepository courseRepository;

    @Mock
    ProfessorRepository professorRepository;

    SubjectController subjectController;

    Subject subjectTest = new Subject();

    Course courseTest = new Course();

    Professor professorTest = new Professor();

    int page = 0;
    int size = 3;
    Pageable pageable = PageRequest.of(page, size);

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

        courseTest.setId(1L);
        courseTest.setName("Java");

        professorTest.setId(1L);
        professorTest.setArea("Engineering");

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
        List<Subject> subjects = new ArrayList<>();

        subjects.add(subjectTest);
        Subject subject = new Subject();
        subject.setId(2L);
        subject.setName("Calculus 2");
        subjects.add(subject);

        Page<Subject> subjectPage = new PageImpl<>(subjects);
        when(subjectService.getAllSubjects(page,size)).thenReturn(subjectPage);

        mockMvc.perform(get("/subjects/all"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].id").value(2));

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

    @Test
    public void updateSubjectTest() throws Exception {
        Subject subject = subjectTest;
        Long id = 1L;

        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));
        when(subjectRepository.save(subject)).thenReturn(subject);
  
        subject.setName("Calculus Updated");
        subject.setDescription("Math is the best Updated");
        when(subjectService.updateSubject(id, subject)).thenReturn(subject);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(subject);
        mockMvc.perform(post("/subjects/"+id+"/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Calculus Updated"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Math is the best Updated"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.credits").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contents").value("Algebra, Geometry, Trigonometry"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.prerequisites").value("Math"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.corequisites").value("None"));
    }

    @Test
    public void delteSubjectTest() throws Exception{
        Long id = subjectTest.getId();
        Subject subject = subjectTest;
        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));
        
        mockMvc.perform(delete("/subjects/"+id+"/delete"))
                .andExpect(status().isOk());
    }

    @Test
    public void getSubjectByNameTest() throws Exception {
        String name = "Calculus";
        Subject subject = subjectTest;
        List<Subject> subjects = new ArrayList<>();
        Subject subject2 = new Subject();
        subject2.setId(2L);
        subject2.setName("Calculus 2");
        subjects.add(subject);
        subjects.add(subject2);

        List<Subject> subjectByName = subjects.stream().filter(s -> s.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
        Page<Subject> subjectPage = new PageImpl<>(subjectByName);
        when(subjectService.getSubjectsByName(page,size, name)).thenReturn(subjectPage);
       
        mockMvc.perform(get("/subjects/"+name+"/search/name"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].name").value("Calculus"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].name").value("Calculus 2"));
    }

    @Test
    public void getSubjectByAreaTest() throws Exception {
        String area = subjectTest.getArea();
        Subject subject = subjectTest;
        List<Subject> subjects = new ArrayList<>();
        Subject subject2 = new Subject();
        subject2.setId(2L);
        subject2.setArea("English");
        subjects.add(subject);
        subjects.add(subject2);

        List<Subject> subjectByArea = subjects.stream().filter(s -> s.getArea().toLowerCase().contains(area.toLowerCase())).collect(Collectors.toList());
        Page<Subject> subjectPage = new PageImpl<>(subjectByArea);
        when(subjectService.getSubjectsByArea(page,size,area)).thenReturn(subjectPage);
       
        mockMvc.perform(get("/subjects/"+area+"/search/area"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].name").value("Calculus"));
    }

    @Test
    public void getSubjectByCreditsTest() throws Exception {
        Integer credits = subjectTest.getCredits();
        Subject subject = subjectTest;
        List<Subject> subjects = new ArrayList<>();
        Subject subject2 = new Subject();
        subject2.setId(2L);
        subject2.setCredits(5);
        subjects.add(subject);
        subjects.add(subject2);

        List<Subject> subjectByCredits = subjects.stream().filter(s -> s.getCredits().equals(credits)).collect(Collectors.toList());
        Page<Subject> subjectPage = new PageImpl<>(subjectByCredits);
        when(subjectService.getSubjectsByCredits(page,size,credits)).thenReturn(subjectPage);
       
        mockMvc.perform(get("/subjects/"+credits+"/search/credits"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].credits").value(3));
    }

    @Test
    public void getSubjectsByCourseIdTest() throws Exception{
        Long courseId = courseTest.getId();
        Subject subject = subjectTest;
        List<Subject> subjects = new ArrayList<>();
        subject.getCourses().add(courseTest);
        subjects.add(subject);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(courseTest));
        when(subjectRepository.findById(subject.getId())).thenReturn(Optional.of(subject));
        when(subjectService.getSubjectsByCourseId(courseId)).thenReturn(subjects);
      
        mockMvc.perform(get("/subjects/"+courseId+"/search/byCourseId"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Calculus"));
    }

    @Test
    public void getSubjectsByProfessorIdTest() throws Exception{
        Long professorId = professorTest.getId();
        Subject subject = subjectTest;
        List<Subject> subjects = new ArrayList<>();
        subject.setProfessor(professorTest);
        Subject subject2 = new Subject();
        Professor professor2 = new Professor();
        professor2.setId(2L);
        subject2.setId(2L);
        subject2.setProfessor(professor2);
        subjects.add(subject2);
        subjects.add(subject);

        when(subjectRepository.findById(subject.getId())).thenReturn(Optional.of(subject));
        when(professorRepository.findById(professorId)).thenReturn(Optional.of(professorTest));
        List<Subject> subjectByProfessorId = subjects.stream().filter(s -> s.getProfessor().getId().equals(professorId)).collect(Collectors.toList());
        when(subjectService.getSubjectsByProfessorId(professorId)).thenReturn(subjectByProfessorId);
       

        mockMvc.perform(get("/subjects/"+professorId+"/search/byProfessorId"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Calculus"));

    }
}
