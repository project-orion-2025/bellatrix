package com.example.project_orion.payload.dtos;

import com.example.project_orion.enums.Difficulty;
import com.example.project_orion.enums.Status;
import com.example.project_orion.enums.Subject;
import com.example.project_orion.models.Option;
import com.example.project_orion.models.Tag;
import com.example.project_orion.service.validation.CreateQuestion;
import com.example.project_orion.service.validation.UpdateQuestion;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QuestionDTO {

    private Long questionId;

    @NotBlank(groups = CreateQuestion.class)
    @Size(min = 3,
            max = 50,
            groups = {CreateQuestion.class, UpdateQuestion.class},
            message = "must contain at-least 3 characters & at-max 50 characters")
    private String title;

    @NotBlank(groups = CreateQuestion.class)
    @Size(
            min = 6,
            max = 1000,
            groups = {CreateQuestion.class, UpdateQuestion.class},
            message = "must contain at-least 6 characters & at-max 1000 characters")
    private String description;

    private String author;

    @NotNull(groups = CreateQuestion.class, message = "Status must not be null")
    private Status status;

    @NotNull(groups = CreateQuestion.class, message = "Subject must not be null")
    private Subject subject;

    @NotNull(groups = CreateQuestion.class, message = "Difficulty must not be null")
    private Difficulty difficulty;

    @NotNull(groups = CreateQuestion.class)
    @Size(min = 4,
            max = 4,
            groups = {CreateQuestion.class, UpdateQuestion.class},
            message = "must be exactly 4")
    private List<Option> options;

    @NotNull(groups = CreateQuestion.class)// use jakartha NotNull not lombok one
    @Size(min = 1,
            groups = {CreateQuestion.class, UpdateQuestion.class},
            message = "at-least 1 tag needs to present")
    private Set<Tag> tagList;

    @NotNull(groups = CreateQuestion.class)
    @Min(value = 1, groups = {CreateQuestion.class, UpdateQuestion.class})
    @Max(value = 4, groups = {CreateQuestion.class, UpdateQuestion.class})
    private Integer correctOptionId;

}
