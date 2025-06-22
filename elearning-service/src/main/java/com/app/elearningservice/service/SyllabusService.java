package com.app.elearningservice.service;

import com.app.elearningservice.dto.LessonDTO;
import com.app.elearningservice.model.Lesson;
import com.app.elearningservice.model.MediaLesson;
import com.app.elearningservice.payload.SyllabusPayload;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SyllabusService {
    private final NamedParameterJdbcTemplate writeDb;

    public SyllabusService(NamedParameterJdbcTemplate writeDb) {
        this.writeDb = writeDb;
    }

    @Transactional
    public void update(SyllabusPayload payload) {
        var sql = """
                update lessons
                set title = :title,
                    description = :description
                where lesson_id = :lesson_id;
                """;
        writeDb.update(
                sql,
                new MapSqlParameterSource()
                        .addValue("title", payload.getTitle())
                        .addValue("description", payload.getDescription())
                        .addValue("lesson_id", payload.getLessonId())
        );
    }

    @Transactional
    public void saveMedia(Long lessonId, String title, String url, String type, byte[] urlBlob) {
        var sql = """
                insert into media(lesson_id, title, url, type, url_blob)
                values (:lesson_id, :title, :url, :type, :url_blob);
                """;
        writeDb.update(
                sql,
                new MapSqlParameterSource()
                        .addValue("lesson_id", lessonId)
                        .addValue("title", title)
                        .addValue("url", url)
                        .addValue("type", type)
                        .addValue("url_blob", urlBlob)
        );
    }

    @Transactional
    public void deleteMedia(Long mediaId) {
        var sql = "delete from media where media_id = :media_id";
        writeDb.update(sql, Map.of("media_id", mediaId));
    }

    @Transactional
    public void deleteLesson(Long lessonId) {
        var sql = "update lessons set status = 'inactive' where lesson_id = :lesson_id;";
        writeDb.update(sql, Map.of("lesson_id", lessonId));
    }

    @Transactional
    public void save(SyllabusPayload payload, List<MediaLesson> medias) {
        var sqlInsert = """
                insert into lessons(course_id, title, description)
                VALUES (:course_id, :title, :description);
                """;
        var keyHolder = new GeneratedKeyHolder();
        writeDb.update(
                sqlInsert,
                new MapSqlParameterSource()
                        .addValue("course_id", payload.getCourseId())
                        .addValue("title", payload.getTitle())
                        .addValue("description", payload.getDescription()),
                keyHolder
        );
        var lessonId = keyHolder.getKey().longValue();
        var sqlInsertMedia = """
                   insert into media(lesson_id, title, url, type, url_blob)
                values (:lesson_id, :title, :url, :type, :url_blob);
                   """;
        var params = medias.stream()
                           .map(e -> new MapSqlParameterSource()
                                   .addValue("lesson_id", lessonId)
                                   .addValue("title", e.getTitle())
                                   .addValue("url", e.getUrl())
                                   .addValue("type", e.getType())
                                   .addValue("url_blob", e.getUrlBlob())
                           ).toList();
        writeDb.batchUpdate(sqlInsertMedia, params.toArray(MapSqlParameterSource[]::new));
    }

    public List<Lesson> findByCourseId(Long courseId) {
        return writeDb.query(
                              """
                                      select l.lesson_id,
                                              l.title,
                                              l.description,
                                              m.media_id,
                                              m.title as media_title,
                                              m.url,
                                              m.type
                                       from lessons l
                                                inner join media m on l.lesson_id = m.lesson_id
                                       where 1=1 
                                       and l.course_id = :courseId
                                       and l.status = 'active';
                                      """,
                              Map.of("courseId", courseId),
                              BeanPropertyRowMapper.newInstance(LessonDTO.class)
                      ).stream()
                      .collect(Collectors.groupingBy(LessonDTO::getLessonId))
                      .entrySet()
                      .stream()
                      .map(Lesson::new)
                      .toList();
    }
}
