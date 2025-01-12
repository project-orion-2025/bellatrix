package com.example.project_orion.payload.dtos;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TagDTO {

    @NotNull
    private Long tagId;

    @NotNull
    private String text;

}
