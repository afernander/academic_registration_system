package com.perficient.path.practice.academic_registration_system.services;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.perficient.path.practice.academic_registration_system.errors.CourseNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.errors.DuplicatedDataExeption;
import com.perficient.path.practice.academic_registration_system.errors.ProfessorNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.errors.SubjectNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.models.Course;
import com.perficient.path.practice.academic_registration_system.models.Professor;
import com.perficient.path.practice.academic_registration_system.models.Subject;
import com.perficient.path.practice.academic_registration_system.repositories.CourseRepository;
import com.perficient.path.practice.academic_registration_system.repositories.ProfessorRepository;
import com.perficient.path.practice.academic_registration_system.repositories.SubjectRepository;



public class SubjectServiceImplTest {

    SubjectService subjectService;

    @Mock
    SubjectRepository subjectRepository;

    @Mock 
    CourseRepository courseRepository;

    @Mock
    ProfessorRepository professorRepository;

    Subject subjectTest = new Subject();

    Course courseTest = new Course();

    Professor professorTest = new Professor();

    int page = 0;
    int size = 3;
    Pageable pageable = PageRequest.of(page, size);

    @BeforeEach
    public void setup() {
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

        subjectService = new SubjectServiceImpl(subjectRepository, courseRepository, professorRepository);
    }

    @Test
    public void getSubjectByIdTest() throws Exception {
        Subject subject = subjectTest;

        Optional<Subject> optionalSubject = Optional.of(subject);

        when(subjectRepository.findById(1L)).thenReturn(optionalSubject);
        
        Subject subjectReturned = subjectService.getSubjectById(1L);

        assertNotNull(subjectReturned, "The returned subject should not be null");
        assertEquals(optionalSubject.get(), subjectReturned, "The returned subject should be the same as the mocked one");
        verify(subjectRepository, times(1)).findById(1L);
    }

    @Test
    public void getSubjectByIdTestNotFound() throws Exception {
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(Exception.class, () -> subjectService.getSubjectById(1L));
        verify(subjectRepository, times(1)).findById(1L);
    }

    @Test
    public void getAllSubjectsTest(){
        List<Subject> subjects = new ArrayList<>();
        subjects.add(subjectTest);
        Page<Subject> pageSubjects = new PageImpl<>(subjects);
        when(subjectRepository.findAll(pageable)).thenReturn(pageSubjects);

        Page<Subject> subjectsReturned = subjectService.getAllSubjects(page,size);

        assertNotNull(subjectsReturned, "The returned subjects should not be null");
        verify(subjectRepository, times(1)).findAll(pageable);
        verify(subjectRepository, never()).findById(anyLong());
    }

    @Test
    public void getAllSubjects_notFoundTest(){
        when(subjectRepository.findAll(pageable)).thenReturn(new PageImpl<>(new ArrayList<>()));

        assertThrows(Exception.class, () -> subjectService.getAllSubjects(page,size));
        verify(subjectRepository, times(1)).findAll(pageable);
        verify(subjectRepository, never()).findById(anyLong());
    }

    @Test
    public void createSubjectTest(){
        Subject subject = subjectTest;

        when(subjectRepository.save(subject)).thenReturn(subject);

        Subject subjectReturned = subjectService.createSubject(subject);

        assertNotNull(subjectReturned, "The returned subject should not be null");
        assertEquals(subject, subjectReturned, "The returned subject should be the same as the mocked one");
        verify(subjectRepository, times(1)).save(subject);
    }

    @Test
    public void createSubject_DuplicatedDataExeptionTest(){
        Subject subject = subjectTest;

        when(subjectRepository.save(subject)).thenThrow(DuplicatedDataExeption.class);

        assertThrows(DuplicatedDataExeption.class, () -> subjectService.createSubject(subject));
    }

    @Test
    public void updateSubjectTest(){
        Subject subject = subjectTest;
        Long id= subjectTest.getId();

        Subject subjectUpdated = subjectTest;
        subjectUpdated.setName("Updated");
        subjectUpdated.setArea("Updated");
        subjectUpdated.setDescription("Updated");
        subjectUpdated.setCredits(1);
        subjectUpdated.setContents("Updated");
        subjectUpdated.setPrerequisites("Updated");
        subjectUpdated.setCorequisites("Updated");

        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));
        when(subjectRepository.save(subjectUpdated)).thenReturn(subjectUpdated);

        Subject subjectReturned = subjectService.updateSubject(id,subject);

        assertNotNull(subjectReturned, "The returned subject should not be null");
        assertEquals(subjectUpdated, subjectReturned, "The returned subject should be the same as the mocked one");
        verify(subjectRepository, times(1)).save(subject);
    }

    @Test
    public void updateSubject_NotFoundTest(){
        Long id= subjectTest.getId();

        when(subjectRepository.findById(id)).thenReturn(Optional.empty());
        
        assertThrows(Exception.class, () -> subjectService.updateSubject(id,subjectTest));
        verify(subjectRepository, times(1)).findById(id);
    }

    @Test
    public void deleteSubjectByIdTest(){
        Long id= subjectTest.getId();
    
        when(subjectRepository.findById(id)).thenReturn(Optional.of(subjectTest));
        doNothing().when(subjectRepository).deleteById(id);
        
        subjectService.deleteSubjectById(id);
        verify(subjectRepository, times(1)).findById(id);
        verify(subjectRepository, times(1)).delete(subjectTest);
    }

    @Test
    public void deleteSubjectByIdNotFoundTest(){
        Long id= subjectTest.getId();
    
        when(subjectRepository.findById(id)).thenReturn(Optional.empty());
        
        assertThrows(Exception.class, () -> subjectService.deleteSubjectById(id));
        verify(subjectRepository, times(1)).findById(id);
        verify(subjectRepository, never()).delete(subjectTest);
    }

    @Test
    public void getSubjectByNameTest(){
        String name = "Calculus";
        Subject subject2 = new Subject();
        subject2.setId(2L);
        subject2.setName("Calculus 2");

        List<Subject> subjects = new ArrayList<>();
        subjects.add(subjectTest);
        subjects.add(subject2);
        Page<Subject> subjectPage = new PageImpl<>(subjects);
        when(subjectRepository.findByNameContaining(pageable,name)).thenReturn(subjectPage);
        
        Page<Subject> subjectsReturned = subjectService.getSubjectsByName(page,size,name);
        
        assertNotNull(subjectsReturned, "The returned subjects should not be null");
        verify(subjectRepository, times(1)).findByNameContaining(pageable,name);
        verify(subjectRepository, never()).findById(anyLong());
    }

    @Test
    void getSubjectsByNameNotFoundTest(){
        String name = "Calculus";
        when(subjectRepository.findByNameContaining(pageable,name)).thenReturn(new PageImpl<>(new ArrayList<>()));
        assertThrows(Exception.class, () -> subjectService.getSubjectsByName(page,size,name));
        verify(subjectRepository, times(1)).findByNameContaining(pageable,name);
        verify(subjectRepository, never()).findById(anyLong());
    }

    @Test
    public void getSubjectByAreaTest(){
        String area = subjectTest.getArea();
        Subject subject2 = new Subject();
        subject2.setId(2L);
        subject2.setArea("history");

        List<Subject> subjects = new ArrayList<>();
        subjects.add(subjectTest);
        subjects.add(subject2);
        List<Subject> subjectsByArea = subjects.stream().filter(s -> s.getArea().toLowerCase().contains(area.toLowerCase())).collect(Collectors.toList());
        Page<Subject> subjectPage = new PageImpl<>(subjectsByArea);
        when(subjectRepository.findByAreaContaining(pageable,area)).thenReturn(subjectPage);
        
        Page<Subject> subjectsReturned = subjectService.getSubjectsByArea(page,size,area);
        
        assertNotNull(subjectsReturned, "The returned subjects should not be null");
        verify(subjectRepository, times(1)).findByAreaContaining(pageable,area);
        verify(subjectRepository, never()).findById(anyLong());
    }

    @Test
    void getSubjectsByAreaNotFoundTest(){
        String area = "Engegnering";
        when(subjectRepository.findByAreaContaining(pageable,area)).thenReturn(new PageImpl<>(new ArrayList<>()));
        assertThrows(Exception.class, () -> subjectService.getSubjectsByArea(page,size,area));
        verify(subjectRepository, times(1)).findByAreaContaining(pageable,area);
        verify(subjectRepository, never()).findById(anyLong());
    }

    @Test
    public void getSubjectByCreditsTest(){
        Integer credits = subjectTest.getCredits();
        Subject subject2 = new Subject();
        subject2.setId(2L);
        subject2.setCredits(5);;

        List<Subject> subjects = new ArrayList<>();
        subjects.add(subjectTest);
        subjects.add(subject2);
        List<Subject> subjectsByCredits = subjects.stream().filter(s -> s.getCredits().equals(credits)).collect(Collectors.toList());
        Page<Subject> subjectPage = new PageImpl<>(subjectsByCredits);
        when(subjectRepository.findByCredits(pageable,credits)).thenReturn(subjectPage);
        
        Page<Subject> subjectsReturned = subjectService.getSubjectsByCredits(page,size,credits);
        
        assertNotNull(subjectsReturned, "The returned subjects should not be null");
        verify(subjectRepository, times(1)).findByCredits(pageable,credits);
        verify(subjectRepository, never()).findById(anyLong());
    }

    @Test
    void getSubjectsByCreditsNotFoundTest(){
        Integer credits = subjectTest.getCredits();
        when(subjectRepository.findByCredits(pageable,credits)).thenReturn(new PageImpl<>(new ArrayList<>()));
        assertThrows(Exception.class, () -> subjectService.getSubjectsByCredits(page,size,credits));
        verify(subjectRepository, times(1)).findByCredits(pageable,credits);
        verify(subjectRepository, never()).findById(anyLong());
    }

    @Test
    void getSubjectsByCourseIdTest() throws Exception{
        Long courseId = courseTest.getId();
        Subject subject = subjectTest;
        subject.getCourses().add(courseTest);
        List<Subject> subjects = new ArrayList<>();
        subjects.add(subject);

        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(subjectRepository.findSubjectsByCoursesId(courseId)).thenReturn(subjects);

        List<Subject> subjectsReturned = subjectService.getSubjectsByCourseId(courseId);

        assertNotNull(subjectsReturned, "The returned subjects should not be null");
        assertEquals(1, subjectsReturned.size(), "The returned subjects should be the same as the mocked ones");
        assertEquals(subjects, subjectsReturned, "The returned subjects should be the same as the mocked ones");
        verify(courseRepository, times(1)).existsById(courseId);
        verify(subjectRepository, times(1)).findSubjectsByCoursesId(courseId);
        verify(subjectRepository, never()).findAll();
        verify(courseRepository, never()).findAll();
    }

    @Test
    void getSubjectsByCourseIdTest_CourseNotFoundTest() throws Exception{
        Long courseId = courseTest.getId();
        Subject subject = subjectTest;
        subject.getCourses().add(courseTest);
        List<Subject> subjects = new ArrayList<>();
        subjects.add(subject);

        when(courseRepository.existsById(courseId)).thenReturn(false);
        when(subjectRepository.findSubjectsByCoursesId(courseId)).thenReturn(subjects);

        assertThrows(CourseNotFoundExeption.class, () -> subjectService.getSubjectsByCourseId(courseId));
    }

    @Test
    void getSubjectsByCourseIdTest_SubjectNotFoundTest() throws Exception{
        Long courseId = courseTest.getId();
        Subject subject = subjectTest;
        subject.getCourses().add(courseTest);
        List<Subject> subjects = new ArrayList<>();

        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(subjectRepository.findSubjectsByCoursesId(courseId)).thenReturn(subjects);

        assertThrows(SubjectNotFoundExeption.class, () -> subjectService.getSubjectsByCourseId(courseId));
    }

    @Test
    void getSubjectsByProfessorIdTest() throws Exception{
        Long professorId = professorTest.getId();
        Subject subject = subjectTest;
        subject.setProfessor(professorTest);
        List<Subject> subjects = new ArrayList<>();
        subjects.add(subject);

        when(professorRepository.existsById(professorId)).thenReturn(true);
        when(subjectRepository.findSubjectsByProfessorId(professorId)).thenReturn(subjects);

        List<Subject> subjectsReturned = subjectService.getSubjectsByProfessorId(professorId);

        assertNotNull(subjectsReturned, "The returned subjects should not be null");
        assertEquals(1, subjectsReturned.size(), "The returned subjects should be the same as the mocked ones");
        assertEquals(subjects, subjectsReturned, "The returned subjects should be the same as the mocked ones");
        verify(professorRepository, times(1)).existsById(professorId);
        verify(subjectRepository, times(1)).findSubjectsByProfessorId(professorId);
        verify(subjectRepository, never()).findAll();
        verify(professorRepository, never()).findAll();
    }

    @Test
    void getSubjectsByProfessorId_ProfessorNotFoundTest() throws Exception{
        Long professorId = professorTest.getId();
        Subject subject = subjectTest;
        subject.setProfessor(professorTest);
        List<Subject> subjects = new ArrayList<>();
        subjects.add(subject);

        when(professorRepository.existsById(professorId)).thenReturn(false);
        when(subjectRepository.findSubjectsByProfessorId(professorId)).thenReturn(subjects);
        
        assertThrows(ProfessorNotFoundExeption.class, () -> subjectService.getSubjectsByProfessorId(professorId));

        verify(professorRepository, times(1)).existsById(professorId);
        verify(subjectRepository, never()).findSubjectsByProfessorId(professorId);
        verify(subjectRepository, never()).findAll();
        verify(professorRepository, never()).findAll();
    }

    @Test
    void getSubjectsByProfessorId_SubjectNotFoundTest() throws Exception{
        Long professorId = professorTest.getId();
        Subject subject = subjectTest;
        subject.setProfessor(professorTest);
        List<Subject> subjects = new ArrayList<>();

        when(professorRepository.existsById(professorId)).thenReturn(true);
        when(subjectRepository.findSubjectsByProfessorId(professorId)).thenReturn(subjects);
        
        assertThrows(SubjectNotFoundExeption.class, () -> subjectService.getSubjectsByProfessorId(professorId));

        verify(professorRepository, times(1)).existsById(professorId);
        verify(subjectRepository, times(1)).findSubjectsByProfessorId(professorId);
        verify(subjectRepository, never()).findAll();
        verify(professorRepository, never()).findAll();
    }
}
