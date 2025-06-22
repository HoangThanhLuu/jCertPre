package com.app.elearningservice.payload;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitTestPayload {
    private Long quizId;
    private List<AnswerQuiz> answer;

    @Getter
    @Setter
    public static class AnswerQuiz {
        private Long qid;
        private String ans;
    }
}
