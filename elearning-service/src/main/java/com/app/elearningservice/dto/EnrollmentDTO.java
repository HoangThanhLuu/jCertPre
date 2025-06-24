package com.app.elearningservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentDTO {
    private Long timeTableId;
    private Long userId;
    private Long courseId;
    private String status;
    private String email;
    private String time;
    private String courseName;
    private String thumbnail;
    private String createdDate;
    private String updatedDate;
}
