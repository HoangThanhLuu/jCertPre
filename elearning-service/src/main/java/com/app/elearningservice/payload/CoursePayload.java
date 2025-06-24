package com.app.elearningservice.payload;

import lombok.Data;

@Data
public class CoursePayload {
    private Long courseId;
    private String name;
    private Long levelId;
    private String description;
    private String startDate;
    private String endDate;
    private String status;
}
