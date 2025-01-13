package com.example.project_orion.models;
import com.example.project_orion.enums.SubmissionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "submissions")
public class Submission{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*TODO: fix this annotation shit & don't use lombok again*/
    private Long questionId;

    private Long optionId;

    private String username;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    SubmissionStatus submissionStatus;
}
