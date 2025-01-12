package com.example.project_orion.models;

import com.example.project_orion.enums.Difficulty;
import com.example.project_orion.enums.Status;
import com.example.project_orion.enums.Subject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;

/*
// @Data
WARNING :: don't use @Data , use @Getter & @Setter
IF YOU WANT TO USE THE @Data then Override the @hashCode function,
inorder to avoid the stackoverflow in case of many-to-many relationships

The error happens because the hash code calculation recursively calls the
hashCode() method on entities like collections (HashSet, List, etc.) within the entity itself,
causing infinite recursion.

Overriding of the hasCode function
    @Override
    public int hashCode(){
        return Objects.hash(questionId);
    }
* */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long questionId;

    @NotBlank
    @Size(min = 3, max = 50, message = "must contain at-least 3 characters & at-max 50 characters")
    private String title;

    @NotBlank
    @Size(min = 6, max = 1000, message = "must contain at-least 6 characters & at-max 1000 characters")
    private String description;

    private String author;

    @NotNull(message = "Status must not be null")
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull(message = "Subject must not be null")
    @Enumerated(EnumType.STRING)
    private Subject subject;

    @NotNull(message = "Difficulty must not be null")
    @Enumerated(EnumType.ORDINAL)
    private Difficulty difficulty;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = Answer.class)
    @JoinColumn(name = "answer_id", referencedColumnName = "answerId", nullable = true)
    private Answer answer;

    @NotNull
    @Size(min = 4, max = 4, message = "must be exactly 4")
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Option> options;

    @NonNull
    @Size(min = 1, message = "at-least 1 tag needs to present")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "question_tags",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tagList;
}
