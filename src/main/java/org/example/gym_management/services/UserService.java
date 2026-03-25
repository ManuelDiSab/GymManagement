package org.example.gym_management.services;

import org.example.gym_management.security.entity.User;

import java.util.List;

public interface UserService {
    public List<User> findAllUsers();
    public User findUserById(long id);
    public void deleteUser(User user);
    public List<User> findAllClients();
}

