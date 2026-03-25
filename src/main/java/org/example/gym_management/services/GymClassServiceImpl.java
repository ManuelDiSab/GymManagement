package org.example.gym_management.services;

import org.example.gym_management.dto.GymClassRequestDto;
import org.example.gym_management.entities.GymClass;
import org.example.gym_management.security.exception.UserIsBusyException;
import org.example.gym_management.security.exception.UserNotInstructorException;
import org.example.gym_management.reposiotries.GymClassRepository;
import org.example.gym_management.security.entity.User;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GymClassServiceImpl implements GymClassService {

    @Autowired GymClassRepository gymClassRepository;
    @Autowired
    UserServiceImpl userService;
    @Autowired @Qualifier("createGymClass") ObjectProvider <GymClass> gymClassProvider;
    public GymClass createCustomGymCLass(String name, String description, int nPlaces, LocalDateTime start, LocalDateTime end, User instructor){
        if(!instructor.isInstructor()){
            throw new UserNotInstructorException("The user is not instructor");
        }
        if(gymClassRepository.instructorIsBusy(instructor, start,end )){
            throw new UserIsBusyException("The instructor is already in another class in that time!");
        }
        GymClass gymClass = gymClassProvider.getObject();
        gymClass.setName(name);
        gymClass.setDescription(description);
        gymClass.setNPlaces(nPlaces);
        gymClass.setStartDate(start);
        gymClass.setEndDate(end);
        gymClass.setInstructor(instructor);
        return gymClass;
    }

    // Database methods
    @Override
    public GymClass saveGymClass(GymClass gymClass) {
        System.out.println("Saving Gym Class");
        return gymClassRepository.save(gymClass);
    }

    @Override
    public GymClass updateCustomGymCLass(GymClassRequestDto request, GymClass gymClass){
        User instructor = userService.findUserById(request.getInstructorId());
        if(!instructor.isInstructor()){
            throw new UserNotInstructorException("The user is not instructor");
        }
        if(gymClassRepository.instructorIsBusy(instructor, gymClass.getStartDate(), gymClass.getEndDate() )){
            throw new UserIsBusyException("The instructor is already in another class in that time!");
        }
        gymClass.setName(request.getName());
        gymClass.setDescription(request.getDescription());
        gymClass.setNPlaces(request.getNPlaces());
        gymClass.setStartDate(request.getStartDate());
        gymClass.setEndDate(request.getEndDate());
        gymClass.setInstructor(instructor);
        return saveGymClass(gymClass);
    }

    @Override
    public void deleteGymClass(GymClass gymClass) {
        System.out.println("Deleting Gym Class");
        gymClassRepository.delete(gymClass);
        System.out.println("Deleted Gym Class");
    }

    @Override
    public GymClass findGymCLassById(long id) {
        System.out.println("Finding Gym Class by ID");
        return gymClassRepository.findById(id).get();
    }

    @Override
    public List<GymClass> findAllGymClasses() {
        System.out.println("Finding All Gym Classes");
        return gymClassRepository.findAll();
    }
}
