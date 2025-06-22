package com.app.elearningservice.response;

import com.app.elearningservice.dto.QuestionDTO;
import com.app.elearningservice.dto.QuizzDTO;

import java.util.List;

public record QuizDetailResponse(long quizId, String title, List<QuestionDTO> questions) {
    public QuizDetailResponse(QuizzDTO quizzDTO, List<QuestionDTO> questions) {
        this(quizzDTO.getQuizId(), quizzDTO.getTitle(), questions);
    }
}
