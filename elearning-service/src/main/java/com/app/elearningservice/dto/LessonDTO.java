package com.app.elearningservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonDTO {
    private Long lessonId;
    private String title;
    private String description;
    private Integer sequence;

    private Long mediaId;
    private String mediaTitle;
    private String url;
    private String type;
}
