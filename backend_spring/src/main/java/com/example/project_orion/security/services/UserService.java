package com.example.project_orion.security.services;

import com.example.project_orion.security.dtos.UserDTO;
import com.example.project_orion.security.models.User;

import java.util.List;

public interface UserService {
    void updateUserRole(Long userId, String roleName);

    List<User> getAllUsers();

    UserDTO getUserById(Long id);
}
