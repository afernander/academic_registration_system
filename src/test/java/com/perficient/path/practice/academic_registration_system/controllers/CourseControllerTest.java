package com.perficient.path.practice.academic_registration_system.controllers;

import java.math.BigDecimal;
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
import com.perficient.path.practice.academic_registration_system.models.Course;
import com.perficient.path.practice.academic_registration_system.models.Course.DurationType;
import com.perficient.path.practice.academic_registration_system.repositories.CourseRepository;
import com.perficient.path.practice.academic_registration_system.services.CourseService;

public class CourseControllerTest {

    MockMvc mockMvc;

    @Mock
    CourseService courseService;

    @Mock
    CourseRepository courseRepository;

    CourseController courseController;

    Course courseTest = new Course();

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        
        courseTest.setId(1L);
        courseTest.setName("Java");
        courseTest.setDescription("Java is a programming language");
        courseTest.setDuration(Double.valueOf(3));
        courseTest.setDurationType(DurationType.HOURS);
        courseTest.setPrice(BigDecimal.valueOf(1000));

        courseController = new CourseController(courseService);
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @Test
    void  getCourseByIdTest() throws Exception {
        Course course = new Course();
        course.setId(1L);
        when(courseService.getCourseById(1L)).thenReturn(course);
     
        mockMvc.perform(get("/courses/1"))
               .andExpect(status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }      
    
    @Test
    void getAllCoursesTest() throws Exception {

        Set<Course> courses = new HashSet<>();
        Course course = new Course();
        course.setId(1L);
        course.setName("Java");
        courses.add(course);

        Course course2 = new Course();
        course2.setId(2L);
        course2.setName("Python");
        courses.add(course2);
        

        when(courseService.getAllCourses()).thenReturn(courses);
        
        mockMvc.perform(get("/courses/all"))
               .andExpect(status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(2))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Python"))
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(1));
    }

    @Test
    void createCourseTest() throws Exception {
        Course course = courseTest;

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(course);
        
        when(courseService.createCourse(course)).thenReturn(course);
        
        mockMvc.perform(post("/courses/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Java"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Java is a programming language"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(1000));
    }

    @Test
    void updateCourseTest() throws Exception{
        Long courseId = courseTest.getId();

        Course course = courseTest;

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseRepository.save(course)).thenReturn(course);

        course.setName("Python");
        course.setDescription("Python is a programming language");
        course.setDuration(Double.valueOf(5.5));

        when(courseService.updateCourse(courseId,course)).thenReturn(course);
        
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(course);

        mockMvc.perform(post("/courses/"+ courseId+"/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Python"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Python is a programming language"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration").value(5.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(1000));
    }

    @Test
    void deleteCourseByIdTest() throws Exception {
        Long courseId = courseTest.getId();
        Course course = courseTest;
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        
        mockMvc.perform(delete("/courses/"+ courseId+"/delete"))
               .andExpect(status().isOk());

    }

    @Test
    void getCoursesByNameTest() throws Exception {
        String courseName = "Java";
        Set<Course> courses = new HashSet<>();
        Course course = new Course();
        course.setId(1L);
        course.setName(courseName);
        courses.add(course);

        Course course2 = new Course();
        course2.setId(2L);
        course2.setName("java Script");
        courses.add(course2);
        
        Set<Course> coursesByName = courses.stream().filter(c -> c.getName().toLowerCase().contains(courseName.toLowerCase())).collect(Collectors.toSet());
    
        when(courseService.getCoursesByName(courseName)).thenReturn(coursesByName);
           
        mockMvc.perform(get("/courses/"+courseName+"/name"))
               .andExpect(status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(2))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("java Script"))
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(1))
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Java"));
                
    }

    @Test
    void getCourseDurationByCourseIdTest() throws Exception{
        Long courseId = courseTest.getId();
        String durationString = courseTest.getDuration() + " " + courseTest.getDurationType();
        when(courseService.getCourseDurationByCourseId(courseId)).thenReturn(durationString);

        mockMvc.perform(get("/courses/"+courseId+"/duration"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(durationString));
    }
        
}
