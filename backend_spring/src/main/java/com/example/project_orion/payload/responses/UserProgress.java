package com.example.project_orion.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProgress {
    private Long easy;
    private Long medium;
    private Long hard;
}
