package com.example.project_orion.security.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Entity
@Data
@NoArgsConstructor
@Table(name = "email_otps")
public class EmailOTP {
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer otp;

    @Column(nullable = false)
    private Instant expiryDate; // Instant gives high precision(nano second) whereas Date given millisecond precision

    @NotNull
    private String email;

    public EmailOTP(Integer otp, Instant expiryDate, String email) {
        this.email = email;
        this.otp = otp;
        this.expiryDate = expiryDate;
    }
}
