package org.example.gym_management.services;

import org.example.gym_management.security.entity.ERole;
import org.example.gym_management.security.entity.User;
import org.example.gym_management.security.exception.MyAPIException;
import org.example.gym_management.security.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(long id) {
        return userRepository.findById(id).orElseThrow(
                ()-> new MyAPIException(HttpStatus.NOT_FOUND, "User with id  " + id + " not found")
        );
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
