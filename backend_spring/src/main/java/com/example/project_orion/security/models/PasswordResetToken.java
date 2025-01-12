package com.example.project_orion.security.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate; // Instant gives high precision(nano second) whereas Date given millisecond precision

    private boolean used;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public PasswordResetToken(String token, Instant expiryDate, User user) {
        this.user = user;
        this.token = token;
        this.expiryDate = expiryDate;
    }
}
