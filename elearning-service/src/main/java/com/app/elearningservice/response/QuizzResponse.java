package com.app.elearningservice.response;

import com.app.elearningservice.dto.QuizzDTO;

public record QuizzResponse(
        Long quizId,
        String title,
        String description,
        String numberOfQuestion
) {
    public QuizzResponse(QuizzDTO dto) {
        this(
                dto.getQuizId(),
                dto.getTitle(),
                dto.getDescription(),
                dto.getNumberOfQuestion()
        );
    }
}
