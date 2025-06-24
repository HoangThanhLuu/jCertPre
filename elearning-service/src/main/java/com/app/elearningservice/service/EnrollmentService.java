package com.app.elearningservice.service;

import com.app.elearningservice.dto.EnrollmentDTO;
import com.app.elearningservice.exception.AppException;
import com.app.elearningservice.model.Course;
import com.app.elearningservice.model.PagingContainer;
import com.app.elearningservice.utils.PagingUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class EnrollmentService {
    private final NamedParameterJdbcTemplate writeDb;

    public EnrollmentService(NamedParameterJdbcTemplate writeDb) {
        this.writeDb = writeDb;
    }

    @Transactional
    public void update(Long eid, String status, Long courseId) {
        var check = writeDb.query("""
                select c.is_register, c.course_id
                from courses c
                where course_id = :eid
                """, Map.of("eid", courseId), BeanPropertyRowMapper.newInstance(Course.class)).stream().findFirst().orElse(null);
        if (check != null && check.isRegister()) {
            throw new AppException("400", "Course already registered");
        }

        var sql = "UPDATE time_table SET status = :status WHERE time_table_id = :enrollment_id";
        var param = Map.of("status", status, "enrollment_id", eid);
        writeDb.update(sql, param);
        if ("approved".equalsIgnoreCase(status)) {
            sql = """
                    update courses
                    set is_register= true
                    where course_id = :course_id
                    """;
            param = Map.of("course_id", courseId);
            writeDb.update(sql, param);

            sql = """
                    update time_table
                    set status = 'rejected'
                    where course_id = :course_id and time_table_id <> :id
                    """;
            param = Map.of("course_id", courseId, "id", eid);
            writeDb.update(sql, param);
        }
    }

    public PagingContainer<EnrollmentDTO> findAllNotMe(Integer page, Integer size, String key, Long userId) {
        var sql = """
                select e.time_table_id,
                       e.user_id,
                       e.course_id,
                       e.status,
                       u.email,
                       CONCAT(c.start_date, ' - ', c.end_date) as time,
                       c.name       as course_name,
                       c.thumbnail,
                       e.created_at as created_date,
                       e.updated_at as updated_date
                from time_table e
                         inner join users u on e.user_id = u.user_id
                         inner join courses c on e.course_id = c.course_id
                where true
                  and (c.name like :key or c.description like :key)
                  and e.user_id <> :user_id
                order by e.created_at desc, e.status + 1
                limit :size offset :offset
                """;
        var offset = PagingUtil.calculateOffset(page, size);
        var param = Map.of("size", size, "offset", offset, "key", "%" + key + "%", "user_id", userId);
        var result = writeDb.query(sql, param, BeanPropertyRowMapper.newInstance(EnrollmentDTO.class));
        var sqlCount = "SELECT COUNT(1) FROM time_table e inner join users u on e.user_id = u.user_id inner join courses c on e.course_id = c.course_id WHERE true and (u.email like :key or c.name like :key) and e.user_id <> :user_id";
        var total = writeDb.query(sqlCount, Map.of("key", "%" + key + "%", "user_id", userId), (rs, i) -> rs.getLong(1)).stream().findFirst().orElse(0L);
        return new PagingContainer<>(page, size, total, result);
    }

    public PagingContainer<EnrollmentDTO> findAll(Integer page, Integer size, String key, Long userId) {
        var sql = """
                select e.time_table_id,
                       e.user_id,
                       e.course_id,
                       e.status,
                       CONCAT(c.start_date, ' - ', c.end_date) as time,
                       c.name       as course_name,
                       c.thumbnail,
                       e.created_at as created_date,
                       e.updated_at as updated_date
                from time_table e
                         inner join users u on e.user_id = u.user_id
                         inner join courses c on e.course_id = c.course_id
                where true
                  and (c.name like :key or c.description like :key)
                  and e.user_id = :user_id
                order by e.created_at desc, e.status + 1
                limit :size offset :offset
                """;
        var offset = PagingUtil.calculateOffset(page, size);
        var param = Map.of("size", size, "offset", offset, "key", "%" + key + "%", "user_id", userId);
        var result = writeDb.query(sql, param, BeanPropertyRowMapper.newInstance(EnrollmentDTO.class));
        var sqlCount = "SELECT COUNT(1) FROM time_table e inner join users u on e.user_id = u.user_id inner join courses c on e.course_id = c.course_id WHERE true and (u.email like :key or c.name like :key) and e.user_id = :user_id";
        var total = writeDb.query(sqlCount, Map.of("key", "%" + key + "%", "user_id", userId), (rs, i) -> rs.getLong(1)).stream().findFirst().orElse(0L);
        return new PagingContainer<>(page, size, total, result);
    }
}
