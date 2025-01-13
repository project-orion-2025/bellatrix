package com.example.project_orion.security.services;

import com.example.project_orion.security.payloads.dtos.UserDTO;
import com.example.project_orion.security.models.User;
import com.example.project_orion.security.payloads.requests.SignupRequest;
import com.example.project_orion.security.payloads.responses.MessageResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;

public interface UserService {
    void updateUserRole(Long userId, String roleName);

    List<User> getAllUsers();

    UserDTO getUserById(Long id);

    User findByUsername(String username);

    void updatePassword(Long userId, String password);

    void updateAccountLockStatus(Long userId, boolean lock);

    void updateAccountExpiryStatus(Long userId, boolean expire);

    void updateAccountEnabledStatus(Long userId, boolean enabled);

    void updateCredentialsExpiryStatus(Long userId, boolean expire);

    void generatePasswordResetToken(String email);

    void resetPassword(String token, String newPassword);

    Boolean validateOTP(@Valid SignupRequest signUpRequest);

    ResponseEntity<?> sendSignUpOTP(String email);

    HashMap<String, Boolean> checkUsernameAndEmailUsed(String username, String email);

    MessageResponse registerUser(SignupRequest signUpRequest);
}
