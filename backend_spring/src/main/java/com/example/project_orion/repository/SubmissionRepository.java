package com.example.project_orion.repository;

import com.example.project_orion.enums.SubmissionStatus;
import com.example.project_orion.models.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByUsernameAndSubmissionStatus(String username, SubmissionStatus submissionStatus);
}
