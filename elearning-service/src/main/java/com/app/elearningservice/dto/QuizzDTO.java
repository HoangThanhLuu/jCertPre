package com.app.elearningservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizzDTO {
    private Long quizId;
    private Long lessonId;
    private String title;
    private String description;
    private String numberOfQuestion;
    private String lessonTitle;
}
