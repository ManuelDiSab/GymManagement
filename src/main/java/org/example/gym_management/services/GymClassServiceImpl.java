package org.example.gym_management.services;

import org.example.gym_management.dto.GymClassRequestDto;
import org.example.gym_management.entities.GymClass;
import org.example.gym_management.security.exception.MyAPIException;
import org.example.gym_management.security.exception.UserIsBusyException;
import org.example.gym_management.security.exception.UserNotInstructorException;
import org.example.gym_management.repositories.GymClassRepository;
import org.example.gym_management.security.entity.User;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GymClassServiceImpl implements GymClassService {

    private final GymClassRepository gymClassRepository;
    private final UserServiceImpl userService;
    private final ObjectProvider <GymClass> gymClassProvider;

    public GymClassServiceImpl(GymClassRepository gymClassRepository,  UserServiceImpl userService,
                               @Qualifier("createGymClass") ObjectProvider <GymClass> gymClassProvider) {
        this.gymClassRepository = gymClassRepository;
        this.userService = userService;
        this.gymClassProvider = gymClassProvider;
    }

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
        return gymClassRepository.save(gymClass);
    }

    @Override
    public GymClass updateCustomGymCLass(GymClassRequestDto request, GymClass gymClass){
        User instructorRequest = userService.findUserById(request.getInstructorId());
        User instructor2 = gymClass.getInstructor();
        if(!instructorRequest.isInstructor()){
            throw new UserNotInstructorException("The user is not instructor");
        }
        if (!instructor2.equals(instructorRequest) && gymClassRepository.instructorIsBusy(instructorRequest, gymClass.getStartDate(), gymClass.getEndDate())) {
            throw new UserIsBusyException("The instructor is already in another class in that time!");
        }
        gymClass.setName(request.getName());
        gymClass.setDescription(request.getDescription());
        gymClass.setNPlaces(request.getNPlaces());
        gymClass.setStartDate(request.getStartDate());
        gymClass.setEndDate(request.getEndDate());
        gymClass.setInstructor(instructorRequest);
        return saveGymClass(gymClass);
    }

    @Override
    public void deleteGymClass(GymClass gymClass) {
        gymClassRepository.delete(gymClass);
    }

    @Override
    public GymClass findGymCLassById(long id) {
        return gymClassRepository.findById(id).orElseThrow(
                ()->new MyAPIException(HttpStatus.NOT_FOUND, "Gym Class with id  " + id +" not found")
        );
    }

    @Override
    public List<GymClass> findAllGymClasses() {
        return gymClassRepository.findAll();
    }
}
