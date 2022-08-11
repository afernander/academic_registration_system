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
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.perficient.path.practice.academic_registration_system.models.Subject;
import com.perficient.path.practice.academic_registration_system.repositories.SubjectRepository;



public class SubjectServiceImplTest {

    SubjectService subjectService;

    @Mock
    SubjectRepository subjectRepository;

    Subject subjectTest = new Subject();

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

        subjectService = new SubjectServiceImpl(subjectRepository);
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

        when(subjectRepository.findAll()).thenReturn(subjects);

        Set<Subject> subjectsReturned = subjectService.getAllSubjects();

        assertNotNull(subjectsReturned, "The returned subjects should not be null");
        assertEquals(subjects.size(), subjectsReturned.size(), "The returned subjects should be the same as the mocked ones");
        verify(subjectRepository, times(1)).findAll();
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
    public void updateSubjectTestNotFound(){
        Long id= subjectTest.getId();

        when(subjectRepository.findById(id)).thenReturn(Optional.empty());
        
        assertThrows(Exception.class, () -> subjectService.updateSubject(id,subjectTest));
        verify(subjectRepository, times(1)).findById(id);
    }

    @Test
    public void deleteSubjectById(){
        Long id= subjectTest.getId();
    
        when(subjectRepository.findById(id)).thenReturn(Optional.of(subjectTest));
        doNothing().when(subjectRepository).deleteById(id);
        
        subjectService.deleteSubjectById(id);
        verify(subjectRepository, times(1)).findById(id);
        verify(subjectRepository, times(1)).delete(subjectTest);
    }

    @Test
    public void deleteSubjectByIdNotFound(){
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
        
        when(subjectRepository.findByNameContaining(name)).thenReturn(subjects);
        
        Set<Subject> subjectsReturned = subjectService.getSubjectsByName(name);
        
        assertNotNull(subjectsReturned, "The returned subjects should not be null");
        assertEquals(2, subjectsReturned.size(), "The returned subjects should be the same as the mocked ones");
        verify(subjectRepository, times(1)).findByNameContaining(name);
        verify(subjectRepository, never()).findById(anyLong());
    }

    @Test
    void getSubjectsByNameNotFoundTest(){
        String name = "Calculus";
        when(subjectRepository.findByNameContaining(name)).thenReturn(new ArrayList<>());
        assertThrows(Exception.class, () -> subjectService.getSubjectsByName(name));
        verify(subjectRepository, times(1)).findByNameContaining(name);
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
        when(subjectRepository.findByAreaContaining(area)).thenReturn(subjectsByArea);
        
        Set<Subject> subjectsReturned = subjectService.getSubjectsByArea(area);
        
        assertNotNull(subjectsReturned, "The returned subjects should not be null");
        assertEquals(1, subjectsReturned.size(), "The returned subjects should be the same as the mocked ones");
        verify(subjectRepository, times(1)).findByAreaContaining(area);
        verify(subjectRepository, never()).findById(anyLong());
    }

    @Test
    void getSubjectsByAreaNotFoundTest(){
        String area = "Engegnering";
        when(subjectRepository.findByAreaContaining(area)).thenReturn(new ArrayList<>());
        assertThrows(Exception.class, () -> subjectService.getSubjectsByArea(area));
        verify(subjectRepository, times(1)).findByAreaContaining(area);
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
        when(subjectRepository.findByCredits(credits)).thenReturn(subjectsByCredits);
        
        Set<Subject> subjectsReturned = subjectService.getSubjectsByCredits(credits);
        
        assertNotNull(subjectsReturned, "The returned subjects should not be null");
        assertEquals(1, subjectsReturned.size(), "The returned subjects should be the same as the mocked ones");
        verify(subjectRepository, times(1)).findByCredits(credits);
        verify(subjectRepository, never()).findById(anyLong());
    }

    @Test
    void getSubjectsByCreditsNotFoundTest(){
        Integer credits = subjectTest.getCredits();
        when(subjectRepository.findByCredits(credits)).thenReturn(new ArrayList<>());
        assertThrows(Exception.class, () -> subjectService.getSubjectsByCredits(credits));
        verify(subjectRepository, times(1)).findByCredits(credits);
        verify(subjectRepository, never()).findById(anyLong());
    }
}
