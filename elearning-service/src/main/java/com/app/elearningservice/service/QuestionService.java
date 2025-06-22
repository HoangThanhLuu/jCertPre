package com.app.elearningservice.service;

import com.app.elearningservice.dto.QuestionDTO;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class QuestionService {
    private final NamedParameterJdbcTemplate writeDb;

    public QuestionService(NamedParameterJdbcTemplate writeDb) {
        this.writeDb = writeDb;
    }

    @Transactional
    public void delete(Long id) {
        var sql = """
                update questions
                set status = 'active'
                where question_id = :question_id;
                """;
        writeDb.update(sql, Map.of("question_id", id));
    }

    @Transactional
    public void save(QuestionDTO dto) {
        var sql = """
                update questions
                set content  = IFNULL(:content, content),
                    answer   = IFNULL(:answer, answer),
                    option_a = IFNULL(:option_a, option_a),
                    option_b = IFNULL(:option_b, option_b),
                    option_c = IFNULL(:option_c, option_c),
                    option_d = IFNULL(:option_d, option_d)
                where question_id = :id;
                """;
        var param = Map.of(
                "id", dto.getQuestionId(),
                "content", dto.getContent(),
                "answer", dto.getAnswer(),
                "option_a", dto.getOptionA(),
                "option_b", dto.getOptionB(),
                "option_c", dto.getOptionC(),
                "option_d", dto.getOptionD()
        );
        writeDb.update(sql, param);
    }
}
