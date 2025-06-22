package com.app.elearningservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyllabusPayload {
    private Long lessonId;
    private Long courseId;
    private String title;
    private String description;
    private Integer sequence;
}
