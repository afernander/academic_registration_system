package com.perficient.path.practice.academic_registration_system.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.perficient.path.practice.academic_registration_system.errors.CourseNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.errors.UserNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.models.Course;
import com.perficient.path.practice.academic_registration_system.models.User;
import com.perficient.path.practice.academic_registration_system.models.Course.DurationType;
import com.perficient.path.practice.academic_registration_system.repositories.CourseRepository;
import com.perficient.path.practice.academic_registration_system.repositories.UserRepository;



public class CourseServiceImplTest {

   CourseService courseService;

   @Mock
    CourseRepository courseRepository;

    @Mock
    UserRepository userRepository;

    Course courseTest = new Course();

    User userTest = new User();

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        courseTest.setId(1L);
        courseTest.setName("Java");
        courseTest.setDescription("Java is a programming language");
        courseTest.setDuration(Double.valueOf(3));
        courseTest.setPrice(BigDecimal.valueOf(1000));
        
        userTest.setId(1L);
        userTest.setFirstName("John");

        courseService = new CourseServiceImpl(courseRepository, userRepository);
    }

    @Test
    void getCourseByIdTest() throws Exception {
        Course course = new Course();
        course.setId(1L);

        Optional<Course> courseOptional = Optional.of(course);
        when(courseRepository.findById(1L)).thenReturn(courseOptional);

        Course courseReturned = courseService.getCourseById(1L);
        assertNotNull(courseReturned, "The course is not null");
        assertEquals(courseOptional.get(), courseReturned, "The course ids are equal");
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void getCourseByIdNotFoundTest(){
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> courseService.getCourseById(1L));
    }


    @Test
    void getAllCoursesTest(){
        Course course = new Course();
        course.setId(1L);
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        when(courseRepository.findAll()).thenReturn(courses);

        Set<Course> allCourses = courseService.getAllCourses();

        assertEquals(1, allCourses.size());
        verify(courseRepository, times(1)).findAll();
        verify(courseRepository, never()).findById(anyLong());
    }

    @Test
    void createCourseTest(){
        Course course = courseTest;

        when(courseRepository.save(course)).thenReturn(course);

        Course courseReturned = courseService.createCourse(course);

        assertNotNull(courseReturned,"The course is not null");
        assertEquals(course, courseReturned, "The courses are equal");
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void updateCourseTest(){
        Course course = courseTest;
        Course courseUpdated = new Course();
        courseUpdated.setId(1L);
        courseUpdated.setName("Updated Java");
        courseUpdated.setDescription("Updated");
        courseUpdated.setDuration(Double.valueOf(5));
        courseTest.setDurationType(DurationType.HOURS);
        courseUpdated.setPrice(BigDecimal.valueOf(2000));

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(courseUpdated)).thenReturn(courseUpdated);

        Course courseReturned = courseService.updateCourse(1L, courseUpdated);

        assertNotNull(courseReturned,"The course is not null");
        assertEquals(courseUpdated, courseReturned, "The courses are equal");
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(courseUpdated);
    }

    @Test
    void deleteCourseById(){
        Course course = courseTest;
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).deleteById(1L);

        courseService.deleteCourseById(1L);
        
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).delete(course);
    }

    @Test
    void updateCourseNotFoundTest(){
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
        Course course = courseTest;
        assertThrows(Exception.class, () -> courseService.updateCourse(1L, course));
    }

    @Test
    void deleteCourseNotFoundTest(){
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> courseService.deleteCourseById(1L));
    }

    @Test
    void getCoursesByName(){
        String courseName = "Java";
        Course course = courseTest;
        Course course2 = new Course();
        course2.setId(2L);
        course2.setName("java Script");

        List<Course> courses = new ArrayList<>();
        courses.add(course);
        courses.add(course2);
        List<Course> coursesByName = courses.stream().filter(c -> c.getName().toLowerCase().contains(courseName.toLowerCase())).collect(Collectors.toList());

        when(courseRepository.findByNameContaining(courseName)).thenReturn(coursesByName);

        Set<Course> coursesReturned = courseService.getCoursesByName(courseName);

        assertEquals(2, coursesReturned.size());
        verify(courseRepository, times(1)).findByNameContaining(courseName);
        verify(courseRepository, never()).findById(anyLong());
        verify(courseRepository, never()).findAll();
    }

    @Test
    void getCoursesByNameNotFoundTest(){
        String courseName = "Java";
        when(courseRepository.findByNameContaining(courseName)).thenReturn(new ArrayList<>());
        assertThrows(Exception.class, () -> courseService.getCoursesByName(courseName));
    }

    @Test
    void getCoursesDurationByCourseIdTest(){
        Course course = courseTest;
        String durationString = courseTest.getDuration() + " " + courseTest.getDurationType();
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        String durationRetruned = courseService.getCourseDurationByCourseId(1L);
        assertEquals(durationString, durationRetruned);
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, never()).findAll();
    }

    @Test
    void getCoursesDurationByCourseIdNotFoundTest(){
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> courseService.getCourseDurationByCourseId(1L));
    }

    @Test
    void getCoursesByUserIdTest() throws Exception{
        Long userId = userTest.getId();
        Course course = courseTest;
        course.getUsers().add(userTest);
        List<Course> courses = new ArrayList<>();
        courses.add(course);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(courseRepository.findCoursesByUsersId(userId)).thenReturn(courses);

        List<Course> coursesReturned = courseService.getCoursesByUserId(userId);

        assertNotNull(coursesReturned, "The courses are not null");
        assertEquals(1, coursesReturned.size(), "The courses size is 1");
        assertEquals(courses, coursesReturned, "The courses are equal");
        verify(userRepository, times(1)).existsById(userId);
        verify(courseRepository, times(1)).findCoursesByUsersId(userId);
        verify(courseRepository, never()).findAll();
        verify(userRepository, never()).findAll();
    }

    @Test
    void getCoursesByUserId_UserNotFoundTest(){
       Long userId = userTest.getId();
       Course course = courseTest;
        course.getUsers().add(userTest);
        List<Course> courses = new ArrayList<>();
        courses.add(course);

       when(userRepository.existsById(userId)).thenReturn(false);
       when(courseRepository.findCoursesByUsersId(userId)).thenReturn(courses);

       assertThrows(UserNotFoundExeption.class , () -> courseService.getCoursesByUserId(userId));
    }

    @Test
    void getCoursesByUserId_CourseNotFoundTest(){
        Long userId = userTest.getId();
        Course course = courseTest;
        course.getUsers().add(userTest);
        List<Course> courses = new ArrayList<>();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(courseRepository.findCoursesByUsersId(userId)).thenReturn(courses);

        assertThrows(CourseNotFoundExeption.class , () -> courseService.getCoursesByUserId(userId));
    }
   
}
