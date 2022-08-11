package com.perficient.path.practice.academic_registration_system.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.perficient.path.practice.academic_registration_system.errors.CourseNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.errors.SubjectNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.models.Subject;
import com.perficient.path.practice.academic_registration_system.repositories.CourseRepository;
import com.perficient.path.practice.academic_registration_system.repositories.SubjectRepository;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    private final CourseRepository courseRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository, CourseRepository courseRepository) {
        this.subjectRepository = subjectRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id).orElseThrow(()->new SubjectNotFoundExeption("Subject with id "+id+" not found"));
    }

    @Override
    public Set<Subject> getAllSubjects() {
        Set<Subject> subjectsSet = new HashSet<>();

        subjectRepository.findAll().iterator().forEachRemaining(subjectsSet::add);
        return subjectsSet;
    }

    @Override
    public Subject createSubject(Subject subject) {
        return subjectRepository.save(subject);
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
    public Set<Subject> getSubjectsByName(String name){
        Set<Subject> subjectsSet = new HashSet<>();
        subjectRepository.findByNameContaining(name).iterator().forEachRemaining(subjectsSet::add);
        if(subjectsSet.isEmpty()){
            throw new SubjectNotFoundExeption("Subjects with name "+name+" not found");
        }
        return subjectsSet;
    }

    @Override
    public Set<Subject> getSubjectsByArea(String area){
        Set<Subject> subjectsSet = new HashSet<>();
        subjectRepository.findByAreaContaining(area).iterator().forEachRemaining(subjectsSet::add);
        if(subjectsSet.isEmpty()){
            throw new SubjectNotFoundExeption("Subjects with area "+area+" not found");
        }
        return subjectsSet;
    }

    @Override
    public Set<Subject> getSubjectsByCredits(Integer credits){
        Set<Subject> subjectsSet = new HashSet<>();
        subjectRepository.findByCredits(credits).iterator().forEachRemaining(subjectsSet::add);
        if(subjectsSet.isEmpty()){
            throw new SubjectNotFoundExeption("Subjects with credits "+credits+" not found");
        }
        return subjectsSet;
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
}