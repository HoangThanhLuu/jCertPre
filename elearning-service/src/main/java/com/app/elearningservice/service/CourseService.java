package com.app.elearningservice.service;

import com.app.elearningservice.exception.AppException;
import com.app.elearningservice.model.Course;
import com.app.elearningservice.model.PagingContainer;
import com.app.elearningservice.payload.CoursePayload;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class CourseService {
    private final NamedParameterJdbcTemplate writeDb;

    public CourseService(NamedParameterJdbcTemplate writeDb) {
        this.writeDb = writeDb;
    }

    public Course findById(Long courseId) {
        return writeDb.queryForObject(
                "SELECT * FROM courses WHERE course_id = :courseId",
                Map.of("courseId", courseId),
                BeanPropertyRowMapper.newInstance(Course.class)
        );
    }

    @Transactional
    public void delete(long courseId) {
        writeDb.update(
                "update courses set status = 'inactive' where course_id = :courseId",
                Map.of("courseId", courseId)
        );
    }

    @Transactional
    public void save(CoursePayload payload, String thumbnail) {
        if (existsName(payload.getName(), payload.getCourseId())) {
            throw new AppException("400", "Course name already exists");
        }
        var param = new MapSqlParameterSource()
                .addValue("name", payload.getName())
                .addValue("description", payload.getDescription())
                .addValue("start_date", payload.getStartDate())
                .addValue("end_date", payload.getEndDate())
                .addValue("thumbnail", thumbnail);
        if (payload.getCourseId() > 0) {
            param.addValue("course_id", payload.getCourseId());
            writeDb.update("""
                                   update courses
                                   set name        = IFNULL(:name, name),
                                       description = IFNULL(:description, description),
                                       start_date  = IFNULL(:start_date, start_date),
                                       end_date    = IFNULL(:end_date, end_date),
                                       thumbnail   = IFNULL(:thumbnail, thumbnail)
                                   where course_id = :course_id;
                                   """, param);
        } else {
            writeDb.update("""
                                   insert into courses(name, description,  thumbnail, start_date, end_date) 
                                   VALUES (:name, :description, :thumbnail, :start_date, :end_date);
                                   """, param);
        }
    }

    public PagingContainer<Course> findAll(int page, int size, String key, Long userId) {
        var sql = """
                select c.course_id,
                       c.name,
                       c.description,
                       c.thumbnail,
                       c.status,
                       c.start_date,
                       c.end_date,
                       c.number_of_students,
                       if(tt.status = 'approved', 1, 0) is_join
               from courses c
                        left join time_table tt
                                  on tt.course_id = c.course_id and tt.user_id = :user_id
                where true
                    and c.status = 'active'
                    and c.name like :key;
                """;
        var param = Map.of("key", "%" + key + "%", "userId", userId);
        var data = writeDb.query(sql, param, (rs, i) -> new Course(rs));
        var sqlCount = """
                SELECT COUNT(1) FROM courses WHERE status = 'active' and name like :key;
                """;
        var total = writeDb.queryForObject(sqlCount, param, Integer.class);
        return new PagingContainer<>(page, size, total, data);
    }

    public boolean existsName(String name, long courseId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM courses WHERE name = :name AND status = 'active' and course_id <> :courseId);";
        var param = new MapSqlParameterSource().addValue("name", name).addValue("courseId", courseId);
        return writeDb.queryForObject(sql, param, Boolean.class);
    }

}
