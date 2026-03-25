package org.example.gym_management.services;

import org.example.gym_management.dto.GymClassRequestDto;
import org.example.gym_management.entities.GymClass;

import java.util.List;

public interface GymClassService {
    public GymClass saveGymClass(GymClass gymClass);
    public void deleteGymClass(GymClass gymClass);
    public GymClass findGymCLassById(long id);
    public List<GymClass> findAllGymClasses();
    public GymClass updateCustomGymCLass(GymClassRequestDto request, GymClass gymClass);

}
