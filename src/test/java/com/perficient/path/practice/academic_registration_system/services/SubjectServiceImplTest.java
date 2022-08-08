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
}
