package com.platform.billprocessing.dto;

import jakarta.validation.constraints.NotBlank;

public class NaturalLanguageQueryRequestDTO {
    @NotBlank(message = "Prompt query must not be empty")
    private String prompt;

    public NaturalLanguageQueryRequestDTO() {
    }

    public NaturalLanguageQueryRequestDTO(String prompt) {
        this.prompt = prompt;
    }

    public static NaturalLanguageQueryRequestDTOBuilder builder() {
        return new NaturalLanguageQueryRequestDTOBuilder();
    }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }

    public static class NaturalLanguageQueryRequestDTOBuilder {
        private String prompt;

        public NaturalLanguageQueryRequestDTOBuilder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public NaturalLanguageQueryRequestDTO build() {
            return new NaturalLanguageQueryRequestDTO(prompt);
        }
    }
}
