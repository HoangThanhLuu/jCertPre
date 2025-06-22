package com.app.elearningservice.response;

import com.app.elearningservice.dto.QuizzDTO;

import java.util.List;
import java.util.Map;

public record LessonQuizzResponse(
        Long lessonId,
        String lessonName,
        List<QuizzResponse> quizzes
) {
    public LessonQuizzResponse(Map.Entry<Long, List<QuizzDTO>> entry) {
        this(
                entry.getKey(),
                entry.getValue().getLast().getLessonTitle(),
                entry.getValue().stream().map(QuizzResponse::new).toList()
        );
    }
}
