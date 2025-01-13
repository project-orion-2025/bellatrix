package com.example.project_orion.security.services;

import com.example.project_orion.models.Person;
import com.example.project_orion.repository.PersonRepository;
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
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

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

    @Autowired
    PersonRepository personRepository;

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

        Instant expiryDate = Instant.now().plus(10, ChronoUnit.MINUTES);

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
        passwordResetTokenRepository.delete(resetToken);
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

        Instant expiryDate = Instant.now().plus(10, ChronoUnit.MINUTES);

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

    @Override
    public HashMap<String, Boolean> checkUsernameAndEmailUsed(
            String username, String email) {
        Boolean isUserEmailExist = userRepository.existsByEmail(email);
        Boolean isUsernameExist = userRepository.existsByUserName(username);

        HashMap<String, Boolean> response = new HashMap<>();
        response.put("username", isUsernameExist);
        response.put("email", isUserEmailExist);
        return  response;
    }

    @Override
    public MessageResponse registerUser(SignupRequest signUpRequest){
        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
            return new MessageResponse("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new MessageResponse("Error: Email is already in use!");
        }

        /*TODO: if user inputs random 6 digit otp, then the api is giving 401, it should give invalid otp*/
        if(!validateOTP(signUpRequest)){
            return new MessageResponse("Error: Invalid OTP!");
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder .encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();

        // TODO: clean up this, make simple if-else
        Role role;
        if (strRoles == null || strRoles.isEmpty()) {
            role = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        }else {
            String roleStr = strRoles.iterator().next();
            if (roleStr.equalsIgnoreCase("admin")) {
                role = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            }else if (roleStr.equalsIgnoreCase("author")) {
                role = roleRepository.findByRoleName(AppRole.ROLE_AUTHOR)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            }else if(roleStr.equalsIgnoreCase("user")){
                role = roleRepository.findByRoleName(AppRole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            }
            else {
                return new MessageResponse("Error: Role is not found. Valid roles = user, author, admin");
            }
            user.setAccountNonLocked(true);
            user.setAccountNonExpired(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);
            user.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
            user.setAccountExpiryDate(LocalDate.now().plusYears(1));
            user.setTwoFactorEnabled(false);
            user.setSignUpMethod("email");
        }
        user.setRole(role);
        userRepository.save(user);
        Person person = Person.builder()
                .username(user.getUserName())
                .build();
        personRepository.save(person);
        return new MessageResponse("User registered successfully!");
    }

}