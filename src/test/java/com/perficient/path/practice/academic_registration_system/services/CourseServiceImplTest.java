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
import com.perficient.path.practice.academic_registration_system.errors.SubjectNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.errors.UserNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.models.Course;
import com.perficient.path.practice.academic_registration_system.models.Subject;
import com.perficient.path.practice.academic_registration_system.models.User;
import com.perficient.path.practice.academic_registration_system.models.Course.DurationType;
import com.perficient.path.practice.academic_registration_system.repositories.CourseRepository;
import com.perficient.path.practice.academic_registration_system.repositories.SubjectRepository;
import com.perficient.path.practice.academic_registration_system.repositories.UserRepository;



public class CourseServiceImplTest {

   CourseService courseService;

   @Mock
    CourseRepository courseRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    SubjectRepository subjectRepository;

    Course courseTest = new Course();

    User userTest = new User();

    Subject subjectTest = new Subject();

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

        subjectTest.setId(1L);
        subjectTest.setName("OOP");
        subjectTest.setDescription("Object Oriented Programming");

        courseService = new CourseServiceImpl(courseRepository, userRepository, subjectRepository);
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

    @Test
    void addSubjectToCourseTest(){
        Long courseId = courseTest.getId();
        Long subjectId = subjectTest.getId();
        Course course = courseTest;

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subjectTest));

        course.getSubjects().add(subjectTest);
        when(courseRepository.save(course)).thenReturn(course);
        Course courseReturned = courseService.addSubjectToCourse(courseId, subjectId);

        assertNotNull(courseReturned, "The course is not null");
        assertEquals(course, courseReturned, "The courses are equal");
        verify(courseRepository, times(1)).findById(courseId);
        verify(subjectRepository, times(1)).findById(subjectId);
        verify(courseRepository, times(1)).save(course);
        verify(courseRepository, never()).findAll();
        verify(subjectRepository, never()).findAll();
    }

    @Test
    void addSubjectToCourseTest_CourseNotFoundTest() throws Exception{
        Long courseId = courseTest.getId();
        Long subjectId = subjectTest.getId();
        
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subjectTest));
        
        assertThrows(CourseNotFoundExeption.class , () -> courseService.addSubjectToCourse(courseId, subjectId));
    }

    @Test
    void addSubjectToCourseTest_SubjectNotFoundTest() throws Exception{
        Long courseId = courseTest.getId();
        Long subjectId = subjectTest.getId();
        Course course = courseTest;

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.empty());
        
        assertThrows(SubjectNotFoundExeption.class , () -> courseService.addSubjectToCourse(courseId, subjectId));
    }

    @Test
    void getCoursesBySubjectIdTest() throws Exception{
        Long subjectId = subjectTest.getId();
        Course course = courseTest;
        course.getSubjects().add(subjectTest);
        List<Course> courses = new ArrayList<>();
        courses.add(course);

        when(subjectRepository.existsById(subjectId)).thenReturn(true);
        when(courseRepository.findCoursesBySubjectsId(subjectId)).thenReturn(courses);

        List<Course> coursesReturned = courseService.getCoursesBySubjectId(subjectId);

        assertNotNull(coursesReturned, "The courses are not null");
        assertEquals(1, coursesReturned.size(), "The courses size is 1");
        assertEquals(courses, coursesReturned, "The courses are equal");
        verify(subjectRepository, times(1)).existsById(subjectId);
        verify(courseRepository, times(1)).findCoursesBySubjectsId(subjectId);
        verify(courseRepository, never()).findAll();
        verify(subjectRepository, never()).findAll();
    }

    @Test
    void getCoursesBySubjectId_SubjectNotFoundTest() throws Exception{
        Long subjectId = subjectTest.getId();
        Course course = courseTest;
        course.getSubjects().add(subjectTest);
        List<Course> courses = new ArrayList<>();
        courses.add(course);

        when(subjectRepository.existsById(subjectId)).thenReturn(false);
        when(courseRepository.findCoursesBySubjectsId(subjectId)).thenReturn(courses);

        assertThrows(SubjectNotFoundExeption.class , () -> courseService.getCoursesBySubjectId(subjectId));
    }

    @Test
    void getCoursesBySubjectId_CourseNotFoundTest() throws Exception{
        Long subjectId = subjectTest.getId();
        Course course = courseTest;
        course.getSubjects().add(subjectTest);
        List<Course> courses = new ArrayList<>();
       

        when(subjectRepository.existsById(subjectId)).thenReturn(true);
        when(courseRepository.findCoursesBySubjectsId(subjectId)).thenReturn(courses);

        assertThrows(CourseNotFoundExeption.class , () -> courseService.getCoursesBySubjectId(subjectId));
    }

    @Test
    void deleteSubjectFromCourseTest() throws Exception{
        Long courseId = courseTest.getId();
        Long subjectId = subjectTest.getId();
        Course course = courseTest;

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subjectTest));

        course.getSubjects().remove(subjectTest);
        courseService.deleteSubjectFromCourse(courseId, subjectId);
        when(courseRepository.save(course)).thenReturn(course);

        verify(courseRepository, times(1)).findById(courseId);
        verify(courseRepository, times(1)).save(course);
        verify(courseRepository, never()).findAll();
        verify(subjectRepository, never()).findAll();
    }

    @Test
    void deleteSubjectFromCourse_CourseNotFoundTest() throws Exception{
        Long courseId = courseTest.getId();
        Long subjectId = subjectTest.getId();
        Course course = courseTest;

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundExeption.class , () -> courseService.deleteSubjectFromCourse(courseId, subjectId));
        verify(courseRepository, times(1)).findById(courseId);
        verify(courseRepository, never()).save(course);
        verify(courseRepository, never()).findAll();
        verify(subjectRepository, never()).findAll();
    }

    @Test
    void deleteSubjectFromCourse_SubjectNotFoundTest() throws Exception{
        Long courseId = courseTest.getId();
        Long subjectId = subjectTest.getId();
        Course course = courseTest;

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.empty());

        assertThrows(SubjectNotFoundExeption.class , () -> courseService.deleteSubjectFromCourse(courseId, subjectId));
        verify(courseRepository, times(1)).findById(courseId);
        verify(courseRepository, never()).save(course);
        verify(courseRepository, never()).findAll();
        verify(subjectRepository, never()).findAll();
    }
   
}
