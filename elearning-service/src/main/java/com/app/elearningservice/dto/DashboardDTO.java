package com.app.elearningservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {
    private Integer totalCourses;
    private Integer totalQuiz;
    private Integer totalStudent;
    private Integer totalLesson;

    public DashboardDTO(ResultSet res) throws SQLException {
        this(
                res.getInt("course_count"),
                res.getInt("quiz_count"),
                res.getInt("student_count"),
                res.getInt("lesson_count")
        );
    }
}
