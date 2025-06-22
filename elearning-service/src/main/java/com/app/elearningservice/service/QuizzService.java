package com.app.elearningservice.service;

import com.app.elearningservice.dto.QuestionDTO;
import com.app.elearningservice.dto.QuizzDTO;
import com.app.elearningservice.payload.QuizzPayload;
import com.app.elearningservice.response.LessonQuizzResponse;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizzService {
    private final NamedParameterJdbcTemplate writeDb;

    public QuizzService(NamedParameterJdbcTemplate writeDb) {
        this.writeDb = writeDb;
    }

    @Transactional
    public void delete(long quizId) {
        var sql = """
                update quizzes
                set status = 'inactive'
                where quiz_id = :quiz_id
                """;
        var param = new MapSqlParameterSource().addValue("quiz_id", quizId);
        writeDb.update(sql, param);
    }

    @Transactional
    public void update(long quizId, String title) {
        var sql = """
                update quizzes
                set title = :title
                where quiz_id = :quiz_id
                """;
        var param = new MapSqlParameterSource()
                .addValue("title", title)
                .addValue("quiz_id", quizId);
        writeDb.update(sql, param);
    }


    public List<QuestionDTO> findQuestionsByQuizzId(long quizId) {
        var sql = """
                select *
                from questions
                where quiz_id = :quiz_id
                and status = 'active'
                """;
        var param = new MapSqlParameterSource().addValue("quiz_id", quizId);
        return writeDb.query(sql, param, BeanPropertyRowMapper.newInstance(QuestionDTO.class));
    }

    public List<QuestionDTO> findQuestionsWithAnswerByQuizzId(long quizId) {
        var sql = """
                 select q.*,
                        rd.answer as user_answer
                 from questions q
                          inner join results_detail rd on q.question_id = rd.question_id
                 where true
                   and quiz_id = :quiz_id
                """;
        var param = new MapSqlParameterSource().addValue("quiz_id", quizId);
        return writeDb.query(sql, param, BeanPropertyRowMapper.newInstance(QuestionDTO.class));
    }

    public List<LessonQuizzResponse> findByCourseId(long courseId) {
        var sql = """
                   select q.quiz_id,
                        q.title,
                        q.description,
                        (SELECT COUNT(1) FROM questions WHERE quiz_id = q.quiz_id and status = 'active') as number_of_question,
                        l.lesson_id,
                        l.title                                                    as lesson_title
                 from courses c
                          inner join lessons l on c.course_id = l.course_id and l.status = 'active'
                          inner join quizzes q on l.lesson_id = q.lesson_id and q.status = 'active'
                 where c.course_id = :course_id;
                """;
        var param = new MapSqlParameterSource().addValue("course_id", courseId);
        var result = writeDb.query(sql, param, BeanPropertyRowMapper.newInstance(QuizzDTO.class));
        return result.stream()
                     .collect(Collectors.groupingBy(QuizzDTO::getLessonId))
                     .entrySet()
                     .stream()
                     .map(LessonQuizzResponse::new)
                     .toList();
    }

    @Transactional
    public void save(QuizzPayload payload) {
        var keyHolder = new GeneratedKeyHolder();
        // save quizz
        var sqlInsertQuiz = """
                   insert into quizzes(lesson_id, title, description, number_of_questions)
                   VALUES (:lesson_id, :title, :description, :number_of_questions)
                """;
        var paramInsertQuiz = new MapSqlParameterSource()
                .addValue("lesson_id", payload.getQuiz().getLessonId())
                .addValue("title", payload.getQuiz().getTitle())
                .addValue("description", payload.getQuiz().getDescription())
                .addValue("number_of_questions", payload.getQuestions().size());
        writeDb.update(sqlInsertQuiz, paramInsertQuiz, keyHolder);
        // save questions
        var quizId = keyHolder.getKey().longValue();
        var sqlInsertQuestion = """
                   insert into questions(quiz_id, no, content, option_a, option_b, option_c, option_d, answer)
                    VALUES (:quiz_id, :no, :content, :option_a, :option_b, :option_c, :option_d, :answer)
                """;
        var paramInsertQuestions = payload.getQuestions()
                                          .stream()
                                          .map(q -> new MapSqlParameterSource()
                                                  .addValue("quiz_id", quizId)
                                                  .addValue("no", q.getNo())
                                                  .addValue("content", q.getContent())
                                                  .addValue("option_a", q.getOptionA())
                                                  .addValue("option_b", q.getOptionB())
                                                  .addValue("option_c", q.getOptionC())
                                                  .addValue("option_d", q.getOptionD())
                                                  .addValue("answer", q.getAnswer())
                                          )
                                          .toArray(MapSqlParameterSource[]::new);
        writeDb.batchUpdate(sqlInsertQuestion, paramInsertQuestions);
    }
}
