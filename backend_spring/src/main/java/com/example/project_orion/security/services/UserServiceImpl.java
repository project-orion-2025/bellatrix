package com.example.project_orion.security.services;

import com.example.project_orion.security.models.*;
import com.example.project_orion.security.payloads.dtos.UserDTO;
import com.example.project_orion.security.payloads.requests.SignupRequest;
import com.example.project_orion.security.payloads.responses.MessageResponse;
import com.example.project_orion.security.repository.EmailOTPRepository;
import com.example.project_orion.security.repository.PasswordResetTokenRepository;
import com.example.project_orion.security.repository.RoleRepository;
import com.example.project_orion.security.repository.UserRepository;
import com.example.project_orion.security.utils.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${frontend.url}")
    String frontendurl;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    EmailOTPRepository emailOTPRepository;

    @Autowired
    EmailService emailService;

    @Override
    public void updateUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        AppRole appRole = AppRole.valueOf(roleName);
        Role role = roleRepository.findByRoleName(appRole)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(role);
        userRepository.save(user);
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return convertToDto(user);
    }

    private UserDTO convertToDto(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.isAccountNonLocked(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                user.getCredentialsExpiryDate(),
                user.getAccountExpiryDate(),
                user.getTwoFactorSecret(),
                user.isTwoFactorEnabled(),
                user.getSignUpMethod(),
                user.getRole(),
                user.getCreatedDate(),
                user.getUpdatedDate()
        );
    }

    @Override
    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByUserName(username);
        return user.orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    @Override
    public void updatePassword(Long userId, String password) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update password");
        }
    }

    @Override
    public void updateAccountLockStatus(Long userId, boolean lock) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        user.setAccountNonLocked(!lock);
        userRepository.save(user);
    }

    @Override
    public void updateAccountExpiryStatus(Long userId, boolean expire) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        user.setAccountNonExpired(!expire);
        userRepository.save(user);
    }

    @Override
    public void updateAccountEnabledStatus(Long userId, boolean enabled) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    @Override
    public void updateCredentialsExpiryStatus(Long userId, boolean expire) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        user.setCredentialsNonExpired(!expire);
        userRepository.save(user);
    }

    @Override
    public void generatePasswordResetToken(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token =  UUID.randomUUID().toString();

        Instant expiryDate = Instant.now().plus(24, ChronoUnit.HOURS);

        PasswordResetToken resetToken = new PasswordResetToken(token, expiryDate, user);

        passwordResetTokenRepository.save(resetToken);

        String resetUrl = frontendurl + "/reset-password?token=" + token;

        emailService.sendPasswordResetEmail(user.getEmail(), resetUrl);
    }

    @Override
    public void resetPassword(String token, String newPassword) {

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid password reset token"));

        if(resetToken.isUsed()){
            throw new RuntimeException("Password reset token has already been used");
        }

        if(resetToken.getExpiryDate().isBefore(Instant.now())){
            throw new RuntimeException("Password reset token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }

    @Override
    public Boolean validateOTP(SignupRequest signUpRequest) {
        EmailOTP emailOTP = emailOTPRepository.findByEmail(signUpRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid password reset token"));

        if(emailOTP.getExpiryDate().isBefore(Instant.now())){
            throw new RuntimeException("Password reset token has expired");
        }

        if(emailOTP.getOtp().equals(signUpRequest.getOtp())){
            emailOTPRepository.delete(emailOTP);
            return true;
        }
        return false;
    }

    @Override
    public ResponseEntity<?> sendSignUpOTP(String email) {

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        EmailOTP savedEmailOTP = emailOTPRepository.findByEmail(email).orElse(null);

        Instant expiryDate = Instant.now().plus(24, ChronoUnit.HOURS);

        Integer otp = (int)(Math.random() * 900000) + 100000;

        EmailOTP emailOTP;
        if(savedEmailOTP != null){
            savedEmailOTP.setOtp(otp);
            savedEmailOTP.setExpiryDate(expiryDate);
            emailOTP = savedEmailOTP;
        }else{
            emailOTP = new EmailOTP(otp, expiryDate, email);
        }

        emailOTPRepository.save(emailOTP);

        emailService.sendSignUpOTPEmail(email, otp.toString());

        return ResponseEntity.ok().body(new MessageResponse("OTP sent successfully"));
    }


}