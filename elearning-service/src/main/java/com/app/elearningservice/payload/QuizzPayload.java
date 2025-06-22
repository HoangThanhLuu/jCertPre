package com.app.elearningservice.payload;

import com.app.elearningservice.dto.QuestionDTO;
import com.app.elearningservice.dto.QuizzDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizzPayload {
    private QuizzDTO quiz;
    private List<QuestionDTO> questions;
}
