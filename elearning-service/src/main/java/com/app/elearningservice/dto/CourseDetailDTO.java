package com.app.elearningservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDetailDTO {
    private Long courseId;
    private String courseName;
    private String courseDescription;
    private String levelName;
    private String startDate;
    private String endDate;
    private String thumbnail;

    private Long lessonId;
    private String lessonName;
    private String lessonDescription;

    private Long mediaId;
    private String mediaType;
    private String mediaUrl;
    private String mediaTitle;
    private byte[] urlBlob;

    private int isJoin;
}
