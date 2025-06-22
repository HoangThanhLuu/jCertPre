package com.app.elearningservice.model;

import com.app.elearningservice.dto.CourseDetailDTO;
import com.app.elearningservice.utils.JDBCUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    private Long courseId;
    private String name;
    private String description;
    private LevelCourse level;
    private String thumbnail;
    private String status;
    private Date startDate;
    private Date endDate;
    private Integer numberOfStudents;
    private List<Lesson> lessons;
    private int isJoined;  // 0: pending, 1: approve, 2: other

    public Course(ResultSet rs) throws SQLException {
        this(
                rs.getLong("course_id"),
                rs.getString("name"),
                rs.getString("description"),
                new LevelCourse(
                        JDBCUtils.getValueResultSet(rs, Integer.class, 0, "level_id"),
                        JDBCUtils.getValueResultSet(rs, String.class, "", "level_name"),
                        ""
                ),
                rs.getString("thumbnail"),
                rs.getString("status"),
                rs.getDate("start_date"),
                rs.getDate("end_date"),
                rs.getInt("number_of_students"),
                List.of(),
                2
        );
    }

    public Course(List<CourseDetailDTO> dtos) {
        this(
                dtos.getFirst().getCourseId(),
                dtos.getFirst().getCourseName(),
                dtos.getFirst().getCourseDescription(),
                new LevelCourse(0, dtos.getFirst().getLevelName(), ""),
                dtos.getFirst().getThumbnail(),
                "",
                Date.valueOf(dtos.getFirst().getStartDate()),
                Date.valueOf(dtos.getFirst().getEndDate()),
                0,
                dtos.stream()
                    .filter(dto -> dto.getLessonId() != null)
                    .collect(Collectors.groupingBy(CourseDetailDTO::getLessonId))
                    .entrySet()
                    .stream()
                    .map(Lesson::from)
                    .toList(),
                dtos.getFirst().getIsJoin()
        );
    }
}
