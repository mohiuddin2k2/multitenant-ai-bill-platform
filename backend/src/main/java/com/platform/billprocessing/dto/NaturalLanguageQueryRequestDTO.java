package com.platform.billprocessing.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NaturalLanguageQueryRequestDTO {
    @NotBlank(message = "Prompt query must not be empty")
    private String prompt;
}

