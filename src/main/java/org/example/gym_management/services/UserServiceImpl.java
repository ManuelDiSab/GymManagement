package org.example.gym_management.services;

import org.example.gym_management.security.entity.ERole;
import org.example.gym_management.security.entity.User;
import org.example.gym_management.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;


    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public List<User> findAllClients() {
        return userRepository.findAllClients(ERole.ROLE_CLIENT);
    }


}
