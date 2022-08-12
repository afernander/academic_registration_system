package com.perficient.path.practice.academic_registration_system.services;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.perficient.path.practice.academic_registration_system.errors.DuplicatedDataExeption;
import com.perficient.path.practice.academic_registration_system.errors.ProfessorNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.errors.SubjectNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.models.Professor;
import com.perficient.path.practice.academic_registration_system.models.Subject;
import com.perficient.path.practice.academic_registration_system.repositories.ProfessorRepository;
import com.perficient.path.practice.academic_registration_system.repositories.SubjectRepository;

public class ProfessorServiceImplTest{

    ProfessorService professorService;

    @Mock
    ProfessorRepository professorRepository;

    @Mock 
    SubjectRepository subjectRepository;

    Professor professorTest= new Professor();

    Subject subjectTest= new Subject();

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        professorTest.setId(1L);
        professorTest.setCode("PROF001");
        professorTest.setArea("Computer Science");
        professorTest.setSpecialization("Software Engineering");

        subjectTest.setId(1L);
        subjectTest.setName("Software Engineering"); 

        professorService = new ProfessorServiceImpl(professorRepository, subjectRepository);
    }

    @Test
    void getProfessorByIdTest() throws Exception {
        Professor professor = professorTest;
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        Professor professorReturned = professorService.getProfessorById(1L);

        assertNotNull(professorReturned, "The professor is not null");
        assertEquals(professor, professorReturned,"The professor is the same");
        verify(professorRepository, times(1)).findById(anyLong());
    }

    @Test
    void getProfessorByIdNotFoundTest() throws Exception {
        when(professorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> professorService.getProfessorById(1L));
    }

    @Test
    void getAllProfessorsTest(){
        Professor professor = new Professor();
        professor.setId(2L);
        professor.setCode("PROF002");
        List<Professor> professors = new ArrayList<>();
        professors.add(professorTest);
        professors.add(professor);
        when(professorRepository.findAll()).thenReturn(professors);

        Set<Professor> professorsReturned = professorService.getAllProfessors();

        assertEquals(2, professorsReturned.size(), "The size of the set is 2");
        verify(professorRepository, times(1)).findAll();
        verify(professorRepository, never()).findById(anyLong());
    }

    @Test
    void createProfessorTest(){
        when(professorRepository.save(professorTest)).thenReturn(professorTest);
        Professor professorReturned = professorService.createProfessor(professorTest);

        assertNotNull(professorReturned, "The professor is not null");
        assertEquals(professorTest, professorReturned, "The professor is the same");
        verify(professorRepository, times(1)).save(professorTest);
    }

    @Test
    void createProfessor_DuplicatedDataExeptionTest(){
        when(professorRepository.save(professorTest)).thenThrow(DuplicatedDataExeption.class);
        assertThrows(DuplicatedDataExeption.class, () -> professorService.createProfessor(professorTest));
    }

    @Test
    void updateProfessorTest(){
        Professor professor = professorTest;
        Professor professorUpdated = new Professor();
        professorUpdated.setId(1L);
        professorUpdated.setCode("PROF001");
        professorUpdated.setArea("Updated area");
        professorUpdated.setSpecialization("Updated specialization");
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(professorRepository.save(professorUpdated)).thenReturn(professorUpdated);

        Professor professorReturned = professorService.updateProfessor(1L, professorUpdated);

        assertNotNull(professorReturned,"The professor is not null");
        assertEquals(professorUpdated, professorReturned, "The professors are equal");
        verify(professorRepository, times(1)).findById(1L);
        verify(professorRepository, times(1)).save(professorUpdated); 
    }

    @Test
    void updateProfessorTestNotFoundException(){
        Professor professor = professorTest;
        when(professorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> professorService.updateProfessor(1L, professor));
    }

    @Test
    void deleteProfessorByIdTest(){
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professorTest));
        professorService.deleteProfessorById(1L);
        verify(professorRepository, times(1)).findById(1L);
        verify(professorRepository, times(1)).delete(professorTest);
    }

    @Test
    void deleteProfessorByIdNotFoundTest(){
        when(professorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> professorService.deleteProfessorById(1L));
    }

    @Test
    void getProfessorsByAreaTest(){
        String area = "Science";
        Professor professor = new Professor();
        professor.setId(2L);
        professor.setCode("PROF002");
        professor.setArea("Science");

        List<Professor> professors = new ArrayList<>();
        professors.add(professorTest);
        professors.add(professor);

        List<Professor> professorsByArea= professors.stream().filter(p -> p.getArea().toLowerCase().contains(area.toLowerCase())).collect(Collectors.toList());
        when(professorRepository.findByAreaContaining(area)).thenReturn(professorsByArea);

        Set<Professor> professorsReturned = professorService.getProfessorsByArea(area);

        assertEquals(2, professorsReturned.size(), "The size of the set is 2");
        verify(professorRepository, times(1)).findByAreaContaining(area);
        verify(professorRepository, never()).findById(anyLong());
        verify(professorRepository, never()).findAll();
    }

    @Test
    void getProfessorsByAreaNotFoundException(){
        String area = "Science";
        when(professorRepository.findByAreaContaining(area)).thenReturn(new ArrayList<>());
        assertThrows(Exception.class, () -> professorService.getProfessorsByArea(area));
    }

    @Test
    void getProfessorsBySpecializationTest(){
        String specialization = "Software";
        Professor professor = new Professor();
        professor.setId(2L);
        professor.setCode("PROF002");
        professor.setSpecialization("Science");

        List<Professor> professors = new ArrayList<>();
        professors.add(professorTest);
        professors.add(professor);

        List<Professor> professorsBySpecialization= professors.stream().filter(p -> p.getSpecialization().toLowerCase().contains(specialization.toLowerCase())).collect(Collectors.toList());
        when(professorRepository.findBySpecializationContaining(specialization)).thenReturn(professorsBySpecialization);

        Set<Professor> professorsReturned = professorService.getProfessorsBySpecialization(specialization);

        assertEquals(1, professorsReturned.size(), "The size of the set is 1");
        verify(professorRepository, times(1)).findBySpecializationContaining(specialization);
        verify(professorRepository, never()).findById(anyLong());
        verify(professorRepository, never()).findAll();
    }

    @Test
    void getProfessorsBySpecializationNotFoundException(){
        String specialization = "Software";
        when(professorRepository.findBySpecializationContaining(specialization)).thenReturn(new ArrayList<>());
        assertThrows(Exception.class, () -> professorService.getProfessorsBySpecialization(specialization));
    }

    @Test
    void addSubjectToProfessorTest(){
        Professor professor = professorTest;
        Subject subject = subjectTest;

        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        professorService.addSubjectToProfessor(1L, 1L);
        when(professorRepository.save(professor)).thenReturn(professor);
        when(subjectRepository.save(subject)).thenReturn(subject);

        assertNotNull(professor, "The professor is not null");
        assertNotNull(subject, "The subject is not null");
        assertEquals(1, professor.getSubjects().size(), "The size of the set is 1");
        verify(professorRepository, times(1)).findById(1L);
        verify(subjectRepository, times(1)).findById(1L);
        verify(professorRepository, times(1)).save(professor);
        verify(subjectRepository, times(1)).save(subject);
    }

    @Test
    void addSubjectToProfessor_ProfessorNotFoundTest(){
        Professor professor = professorTest;
        Subject subject = subjectTest;

        when(professorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProfessorNotFoundExeption.class, () -> professorService.addSubjectToProfessor(1L, 1L));
        verify(professorRepository, times(1)).findById(1L);
        verify(subjectRepository, never()).findById(1L);
        verify(professorRepository, never()).save(professor);
        verify(subjectRepository, never()).save(subject);
    }

    @Test
    void addSubjectToProfessor_SubjectNotFoundTest(){
        Professor professor = professorTest;
        Subject subject = subjectTest;

        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(SubjectNotFoundExeption.class, () -> professorService.addSubjectToProfessor(1L, 1L));
        verify(professorRepository, times(1)).findById(1L);
        verify(subjectRepository, times(1)).findById(1L);
        verify(professorRepository, never()).save(professor);
        verify(subjectRepository, never()).save(subject);
    }

    @Test
    void deleteSubjectFromProfessorTest(){
        Professor professor = professorTest;
        Subject subject = subjectTest;
        professor.getSubjects().add(subject);
        subject.setProfessor(professor);

        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        professorService.deleteSubjectFromProfessor(1L, 1L);
        when(professorRepository.save(professor)).thenReturn(professor);
        when(subjectRepository.save(subject)).thenReturn(subject);

        assertNotNull(professor, "The professor is not null");
        assertNotNull(subject, "The subject is not null");
        assertEquals(0, professor.getSubjects().size(), "The size of the set is 1");
        verify(professorRepository, times(1)).findById(1L);
        verify(subjectRepository, times(1)).findById(1L);
        verify(professorRepository, times(1)).save(professor);
        verify(subjectRepository, times(1)).save(subject);
    }

    @Test
    void deleteSubjectFromProfessor_ProfessorNotFoundTest(){
        Professor professor = professorTest;
        Subject subject = subjectTest;
        professor.getSubjects().add(subject);
        subject.setProfessor(professor);

        when(professorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProfessorNotFoundExeption.class, () -> professorService.deleteSubjectFromProfessor(1L, 1L));

        verify(professorRepository, times(1)).findById(1L);
        verify(subjectRepository, never()).findById(1L);
        verify(professorRepository, never()).save(professor);
        verify(subjectRepository, never()).save(subject);
    }

    @Test
    void deleteSubjectFromProfessor_subjectNotFoundTest(){
        Professor professor = professorTest;
        Subject subject = subjectTest;
        professor.getSubjects().add(subject);
        subject.setProfessor(professor);

        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(SubjectNotFoundExeption.class, () -> professorService.deleteSubjectFromProfessor(1L, 1L));

        verify(professorRepository, times(1)).findById(1L);
        verify(subjectRepository, times(1)).findById(1L);
        verify(professorRepository, never()).save(professor);
        verify(subjectRepository, never()).save(subject);
    }


}
