package com.app.elearningservice.model;

import com.app.elearningservice.dto.CourseDetailDTO;
import com.app.elearningservice.dto.LessonDTO;
import com.app.elearningservice.utils.Base64Utils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaLesson {
    private Long mediaId;
    private Long lessonId;
    private String title;
    private String url;
    private String type;
    private byte[] urlBlob;


    public MediaLesson(LessonDTO dto) {
        this(
                dto.getMediaId(),
                dto.getLessonId(),
                dto.getMediaTitle(),
                dto.getUrl(),
                dto.getType(),
                null
        );
    }

    public MediaLesson(CourseDetailDTO dto) {
        this(
                dto.getMediaId(),
                dto.getLessonId(),
                dto.getMediaTitle(),
                dto.getMediaUrl(),
                dto.getMediaType(),
                dto.getUrlBlob()
        );
    }
}
