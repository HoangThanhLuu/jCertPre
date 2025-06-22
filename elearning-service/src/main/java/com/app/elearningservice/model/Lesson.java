package com.app.elearningservice.model;

import com.app.elearningservice.dto.CourseDetailDTO;
import com.app.elearningservice.dto.LessonDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
    private Long lessonId;
    private Long courseId;
    private String title;
    private String description;
    private Integer sequence;

    private List<MediaLesson> media;

    public Lesson(Map.Entry<Long, List<LessonDTO>> entry) {
        this(
                entry.getKey(),
                0L,
                entry.getValue().getFirst().getTitle(),
                entry.getValue().getFirst().getDescription(),
                0,
                entry.getValue().stream().map(MediaLesson::new).toList()
        );
    }

    public static Lesson from(Map.Entry<Long, List<CourseDetailDTO>> dtos) {
        return new Lesson(
                dtos.getValue().getFirst().getLessonId(),
                dtos.getValue().getFirst().getCourseId(),
                dtos.getValue().getFirst().getLessonName(),
                dtos.getValue().getFirst().getLessonDescription(),
                0,
                dtos.getValue().stream().map(MediaLesson::new).toList()
        );
    }
}
