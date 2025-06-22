package com.app.elearningservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultDTO {
    private Long resultId;
    private Long quizId;
    private String quizTitle;
    private String scoreStr;
    private String status;
    private String startTime;
    private String endTime;
}
