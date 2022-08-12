package com.perficient.path.practice.academic_registration_system.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.perficient.path.practice.academic_registration_system.errors.CourseNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.errors.DuplicatedDataExeption;
import com.perficient.path.practice.academic_registration_system.errors.SubjectNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.errors.UserNotFoundExeption;
import com.perficient.path.practice.academic_registration_system.models.Course;
import com.perficient.path.practice.academic_registration_system.models.Subject;
import com.perficient.path.practice.academic_registration_system.repositories.CourseRepository;
import com.perficient.path.practice.academic_registration_system.repositories.SubjectRepository;
import com.perficient.path.practice.academic_registration_system.repositories.UserRepository;



@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    private final UserRepository userRepository;

    private final SubjectRepository subjectRepository;

    public CourseServiceImpl(CourseRepository courseRepository, UserRepository userRepository, SubjectRepository subjectRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(()-> new CourseNotFoundExeption("Course with id "+ id+ " not found"));
    }

    @Override
    public Set<Course> getAllCourses() {
        Set<Course> coursesSet = new HashSet<>();
        courseRepository.findAll().iterator().forEachRemaining(coursesSet::add);

        return coursesSet;
    }

    @Override
    public Course createCourse(Course course) {
        try{
            courseRepository.save(course);
        }catch(Exception e){
            throw new DuplicatedDataExeption("Course with name "+ course.getName()+ " already exists");
        }
        
        return course;
    }

    @Override
    public Course updateCourse(Long id, Course course) {
        Course courseToUpdate = courseRepository.findById(id).orElseThrow(()-> new CourseNotFoundExeption("Course with id "+ id+ " not found to update"));
        courseToUpdate.setName(course.getName());
        courseToUpdate.setDescription(course.getDescription());
        courseToUpdate.setDuration(course.getDuration());
        courseToUpdate.setDurationType(course.getDurationType());
        courseToUpdate.setPrice(course.getPrice());
        return courseRepository.save(courseToUpdate);
    }

    @Override
    public void deleteCourseById(Long id) {
        Course courseToDelete = courseRepository.findById(id).orElseThrow(()-> new CourseNotFoundExeption("Course with id "+ id+ " not found to delete"));
        courseRepository.delete(courseToDelete);
    }

    @Override
    public Set<Course> getCoursesByName(String name){
        Set<Course> coursesSet = new HashSet<>();
        courseRepository.findByNameContaining(name).iterator().forEachRemaining(coursesSet::add);
        if(coursesSet.isEmpty()){
            throw new CourseNotFoundExeption("Course with  "+ name+ " in name not found");
        }
        return coursesSet;
    }

    @Override
    public String getCourseDurationByCourseId(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(()-> new CourseNotFoundExeption("Course with id "+ courseId+ " not found to get duration"));
        return course.getDuration() + " " + course.getDurationType();
    }
    

    @Override
    public List<Course> getCoursesByUserId(Long userId) {
       if(!userRepository.existsById(userId)){
           throw new UserNotFoundExeption("User with id "+ userId+ " not found to get courses");
       }
       List<Course> courses = courseRepository.findCoursesByUsersId(userId);
       if(courses.isEmpty()){
           throw new CourseNotFoundExeption("User with id "+ userId+ " not found to get courses");
       }
       return courses;
    }

    @Override
    public Course addSubjectToCourse(Long courseId, Long subjectId) {
        Course courseToUpdate = courseRepository.findById(courseId).orElseThrow(()-> new CourseNotFoundExeption("Course with id "+ courseId+ " not found to add subject"));
        Subject subjectToAdd = subjectRepository.findById(subjectId).orElseThrow(()-> new SubjectNotFoundExeption("Subject with id "+ subjectId+ " not found to add to course"));
        courseToUpdate.getSubjects().add(subjectToAdd);
        return courseRepository.save(courseToUpdate);
    }

    @Override
    public List<Course> getCoursesBySubjectId(Long subjectId) {
       if(!subjectRepository.existsById(subjectId)){
           throw new SubjectNotFoundExeption("Subject with id "+ subjectId+ " not found to get courses");
       }
         List<Course> courses = courseRepository.findCoursesBySubjectsId(subjectId);
        if(courses.isEmpty()){
             throw new CourseNotFoundExeption("Subject with id "+ subjectId+ " not found to get courses");
        }
        return courses;
    }

    @Override
    public Course deleteSubjectFromCourse(Long courseId, Long subjectId){
        Course courseToUpdate = courseRepository.findById(courseId).orElseThrow(()-> new CourseNotFoundExeption("Course with id "+ courseId+ " not found to delete subject"));
        Subject subjectToDelete = subjectRepository.findById(subjectId).orElseThrow(()-> new SubjectNotFoundExeption("Subject with id "+ subjectId+ " not found to delete from course"));
        courseToUpdate.getSubjects().remove(subjectToDelete);
        return courseRepository.save(courseToUpdate);
    }
}