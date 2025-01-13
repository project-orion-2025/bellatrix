package com.example.project_orion.crons;

import com.example.project_orion.security.repository.EmailOTPRepository;
import com.example.project_orion.security.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;


//@Component
public class ClearJunkDataCron {

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    EmailOTPRepository otpRepository;

    /*TODO: fix this cron jobs, they are not working */
    @Scheduled(cron = "0 20 21 * * ?") // Run at 9:20 PM every day
    public void clearExpiredPasswordResetData() {
        Instant now = Instant.now();
        passwordResetTokenRepository.deleteByExpiryDateBefore(now);
    }

    @Scheduled(cron = "0 20 21 * * ?") // Run at 9:20 PM every day
    public void clearExpiredOTPData() {
        Instant now = Instant.now();
        otpRepository.deleteByExpiryDateBefore(now);
    }

}
