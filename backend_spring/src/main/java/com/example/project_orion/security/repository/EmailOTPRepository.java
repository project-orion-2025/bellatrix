package com.example.project_orion.security.repository;

import com.example.project_orion.security.models.EmailOTP;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface EmailOTPRepository extends JpaRepository<EmailOTP, Long> {
    Optional<EmailOTP> findByEmail(@NotBlank @Size(max = 50) @Email String email);

    void deleteByExpiryDateBefore(Instant now);
}
