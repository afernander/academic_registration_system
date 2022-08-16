package com.perficient.path.practice.academic_registration_system.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.perficient.path.practice.academic_registration_system.errors.CourseNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.errors.DuplicatedDataExeption;
import com.perficient.path.practice.academic_registration_system.errors.ProfessorNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.errors.SubjectNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.models.Subject;
import com.perficient.path.practice.academic_registration_system.repositories.CourseRepository;
import com.perficient.path.practice.academic_registration_system.repositories.ProfessorRepository;
import com.perficient.path.practice.academic_registration_system.repositories.SubjectRepository;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    private final CourseRepository courseRepository;

    private final ProfessorRepository professorRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository, CourseRepository courseRepository, ProfessorRepository professorRepository) {
        this.subjectRepository = subjectRepository;
        this.courseRepository = courseRepository;
        this.professorRepository = professorRepository;
    }

    @Override
    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id).orElseThrow(()->new SubjectNotFoundExeption("Subject with id "+id+" not found"));
    }

    @Override
    public Page<Subject> getAllSubjects(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Subject> subjectPage = subjectRepository.findAll(pageable);
        if(subjectPage.isEmpty()){
            throw new SubjectNotFoundExeption("No subjects found");
        }
        return subjectPage;
    }

    @Override
    public Subject createSubject(Subject subject) {
        try{
            subjectRepository.save(subject);
        }catch(Exception e){
            throw new DuplicatedDataExeption("Subject with name "+ subject.getName()+ " already exists");
        }

        return subject;
    }

    @Override
    public Subject updateSubject(Long id,Subject subject) {
        Subject subjectToUpdate = subjectRepository.findById(id).orElseThrow(()->new SubjectNotFoundExeption("Subject with id "+subject.getId()+" not found to update"));
        subjectToUpdate.setName(subject.getName());
        subjectToUpdate.setDescription(subject.getDescription());
        subjectToUpdate.setArea(subject.getArea());
        subjectToUpdate.setCredits(subject.getCredits());
        subjectToUpdate.setContents(subject.getContents());
        subjectToUpdate.setPrerequisites(subject.getPrerequisites());
        subjectToUpdate.setCorequisites(subject.getCorequisites());
      
        return subjectRepository.save(subjectToUpdate);
    }

    @Override
    public void deleteSubjectById(Long id) {
        Subject subjectToDelete = subjectRepository.findById(id).orElseThrow(()->new SubjectNotFoundExeption("Subject with id "+id+" not found to delete"));
        subjectRepository.delete(subjectToDelete);
    }
    
    @Override
    public Page<Subject> getSubjectsByName(int page, int size, String name){
        Pageable pageable = PageRequest.of(page, size);
        Page<Subject> subjects = subjectRepository.findByNameContaining(pageable, name);
   
        if(subjects.isEmpty()){
            throw new SubjectNotFoundExeption("Subjects with name "+name+" not found");
        }
        return subjects;
    }

    @Override
    public Page<Subject> getSubjectsByArea(int page, int size,String area){
        Pageable pageable = PageRequest.of(page, size);
        Page<Subject> subjects =subjectRepository.findByAreaContaining(pageable,area);
        if(subjects.isEmpty()){
            throw new SubjectNotFoundExeption("Subjects with area "+area+" not found");
        }
        return subjects;
    }

    @Override
    public Page<Subject> getSubjectsByCredits(int page, int size,Integer credits){
        Pageable pageable = PageRequest.of(page, size);
        Page<Subject> subjects =subjectRepository.findByCredits(pageable,credits);
        if(subjects.isEmpty()){
            throw new SubjectNotFoundExeption("Subjects with credits "+credits+" not found");
        }
        return subjects;
    }

    @Override
    public List<Subject> getSubjectsByCourseId(Long courseId){
        if(!courseRepository.existsById(courseId)){
            throw new CourseNotFoundExeption("Course with id "+courseId+" not found");
        }
        List<Subject> subjects = subjectRepository.findSubjectsByCoursesId(courseId);
        if(subjects.isEmpty()){
            throw new SubjectNotFoundExeption("Subjects with course id "+courseId+" not found");
        }
        return subjects;
    }

    @Override
    public List<Subject> getSubjectsByProfessorId(Long professorId){
        if(!professorRepository.existsById(professorId)){
            throw new ProfessorNotFoundExeption("Professor with id "+professorId+" not found");
        }
        List<Subject> subjects = subjectRepository.findSubjectsByProfessorId(professorId);
        if(subjects.isEmpty()){
            throw new SubjectNotFoundExeption("Subjects with professor id "+professorId+" not found");
        }
        return subjects;
    }
}