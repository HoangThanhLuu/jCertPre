package com.app.elearningservice.service;

import com.app.elearningservice.dto.EnrollmentDTO;
import com.app.elearningservice.exception.AppException;
import com.app.elearningservice.model.PagingContainer;
import com.app.elearningservice.model.User;
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
    public void update(Long eid, String status) {
        var sql = "UPDATE enrollments SET status = :status WHERE enrollment_id = :enrollment_id";
        var param = Map.of("status", status, "enrollment_id", eid);
        writeDb.update(sql, param);
    }

    public PagingContainer<EnrollmentDTO> findAll(Integer page, Integer size, String key) {
        var sql = """
                select e.enrollment_id,
                       e.user_id,
                       e.course_id,
                       e.status,
                       u.email,
                       c.name as course_name,
                       c.thumbnail,
                       e.created_at as created_date,
                       e.updated_at as updated_date
                from enrollments e
                         inner join users u on e.user_id = u.user_id
                         inner join courses c on e.course_id = c.course_id
                where true
                  and (u.email like :key or c.name like :key)
                order by e.created_at desc, e.status + 1
                limit :size offset :offset
                """;
        var offset = PagingUtil.calculateOffset(page, size);
        var param = Map.of("size", size, "offset", offset, "key", "%" + key + "%");
        var result = writeDb.query(sql, param, BeanPropertyRowMapper.newInstance(EnrollmentDTO.class));
        var sqlCount = "SELECT COUNT(1) FROM enrollments e inner join users u on e.user_id = u.user_id inner join courses c on e.course_id = c.course_id WHERE true and (u.email like :key or c.name like :key)";
        var total = writeDb.queryForObject(sqlCount, Map.of("key", "%" + key + "%"), Integer.class);
        return new PagingContainer<>(page, size, total, result);
    }

    @Transactional
    public void save(User user, Long cid) {
        var checkIsEnroll = "SELECT EXISTS(SELECT 1 FROM enrollments WHERE user_id = :user_id AND course_id = :course_id AND (status = 'approved' OR status = 'pending'))";
        var param = Map.of("user_id", user.getUserId(), "course_id", cid);
        var isEnroll = writeDb.queryForObject(checkIsEnroll, param, Boolean.class);
        if (isEnroll) {
            throw new AppException("400", "You have already subscribed to this course. Please wait for the approval");
        }
        var sqlEnroll = "INSERT INTO enrollments(user_id, course_id) VALUES (:user_id, :course_id)";
        writeDb.update(sqlEnroll, param);

        var sqlUpdateCourse = """
                UPDATE courses
                SET number_of_students = courses.number_of_students + 1
                WHERE course_id = :course_id
                """;
        writeDb.update(sqlUpdateCourse, Map.of("course_id", cid));
    }
}
