package com.app.elearningservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollCourseDTO {
    private String courseName;
    private String thumbnail;
    private String startDate;
    private String endDate;
    private String enrollmentDate;
    private String enrollmentStatus;
    private String courseStatus;
}
