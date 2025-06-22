package com.app.elearningservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentDTO {
    private Long enrollmentId;
    private Long userId;
    private Long courseId;
    private String status;

    private String email;
    private String courseName;
    private String thumbnail;
    private String createdDate;
    private String updatedDate;
}
