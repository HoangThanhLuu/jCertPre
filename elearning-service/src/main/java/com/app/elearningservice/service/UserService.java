package com.app.elearningservice.service;

import com.app.elearningservice.dto.ResultDTO;
import com.app.elearningservice.exception.AppException;
import com.app.elearningservice.jwt.JwtTokenProvider;
import com.app.elearningservice.model.PagingContainer;
import com.app.elearningservice.model.User;
import com.app.elearningservice.model.enums.ErrorCodeEnum;
import com.app.elearningservice.model.enums.RoleEnum;
import com.app.elearningservice.model.enums.StatusEnum;
import com.app.elearningservice.payload.RegisterPayload;
import com.app.elearningservice.payload.UserPayload;
import com.app.elearningservice.response.LoginResponse;
import com.app.elearningservice.security.PasswordEncodeImpl;
import com.app.elearningservice.security.UserPrincipal;
import com.app.elearningservice.utils.PagingUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final NamedParameterJdbcTemplate writeDb;
    private final PasswordEncodeImpl passwordEncode;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(
            NamedParameterJdbcTemplate writeDb,
            PasswordEncodeImpl passwordEncode,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.writeDb = writeDb;
        this.passwordEncode = passwordEncode;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String randomPassword() {
        return passwordEncode.generateRandomPassword();
    }

    @Transactional
    public void updatePassword(Long userId, String password) {
        var sql = """
                UPDATE users
                SET password = :password
                WHERE user_id = :user_id
                """;
        var params = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("password", passwordEncode.encode(password));
        writeDb.update(sql, params);
    }

    public List<ResultDTO> result(User user) {
        var sql = """
                select result_id,
                       q.quiz_id,
                       q.title      as quiz_title,
                       score_str,
                       r.status,
                       r.created_at as start_time,
                       r.updated_at as end_time
                from results r
                         inner join quizzes q on r.quiz_id = q.quiz_id
                where user_id = :user_id
                """;
        var params = new MapSqlParameterSource().addValue("user_id", user.getUserId());
        return writeDb.query(sql, params, BeanPropertyRowMapper.newInstance(ResultDTO.class));
    }

    @Transactional
    public void updateAvatar(Long userId, String avatar) {
        var sql = """
                update users
                set avatar = IFNULL(:avatar, avatar)
                where user_id = :user_id;
                """;
        var params = new MapSqlParameterSource()
                .addValue("avatar", avatar)
                .addValue("user_id", userId);
        writeDb.update(sql, params);
    }

    @Transactional
    public void update(UserPayload user, Long userId) {
        var sql = """
                update users
                set first_name = IFNULL(:first_name, first_name),
                    last_name  = IFNULL(:last_name, last_name),
                    address    = IFNULL(:address, address),
                    phone      = IFNULL(:phone, phone),
                    dob        = IFNULL(:dob, dob),
                    gender     = IFNULL(:gender, gender)
                where user_id = :user_id;
                """;
        var params = new MapSqlParameterSource()
                .addValue("first_name", user.firstName())
                .addValue("last_name", user.lastName())
                .addValue("address", user.address())
                .addValue("phone", user.phone())
                .addValue("dob", user.dob())
                .addValue("gender", user.gender())
                .addValue("user_id", userId);
        writeDb.update(sql, params);
    }


    public LoginResponse authenticate(String email, String password) {
        var user = findByEmail(email);
        if (user.isEmpty()) {
            throw new AppException(ErrorCodeEnum.ACCOUNT_NOT_FOUND);
        }
        validateLogin(user.get(), password);
        var userToken = jwtTokenProvider.generateToken(user);
        return new LoginResponse(
                userToken,
                "",
                RoleEnum.ADMIN.equals(user.get().getRole()),
                RoleEnum.TEACHER.equals(user.get().getRole()),
                true
        );
    }

    @Transactional
    public void register(RegisterPayload payload) {
        var user = new User();
        user.setEmail(payload.email());
        user.setPassword(passwordEncode.encode(payload.password()));
        user.setFirstName(payload.firstName());
        user.setLastName(payload.lastName());
        user.setRole(RoleEnum.STUDENT);
        user.setStatus(StatusEnum.ACTIVE);
        save(user, true);
    }

    public void activeUser(String email) {
        activateUser(email);
    }

    public boolean validateEmailExist(String email) {
        return findByEmail(email).isPresent();
    }


    private void validateLogin(User user, String password) {
        if (!passwordEncode.matches(password, user.getPassword())) {
            throw new AppException(ErrorCodeEnum.PASSWORD_INCORRECT);
        }
        if (StatusEnum.BLOCK.equals(user.getStatus())) {
            throw new AppException(ErrorCodeEnum.ACCOUNT_LOCKED);
        } else if (StatusEnum.INACTIVE.equals(user.getStatus())) {
            throw new AppException(ErrorCodeEnum.ACCOUNT_INACTIVE);
        }
    }

    @Transactional
    public User save(User user, boolean isRegister) {
        var params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("role", user.getRole().name())
                .addValue("first_name", user.getFirstName())
                .addValue("last_name", user.getLastName())
                .addValue("avatar", user.getAvatar())
                .addValue("is_register", isRegister)
                .addValue("status", user.getStatus().name());
        var sql = "CALL up_SaveUser(:email, :password, :first_name, :last_name, null, :avatar, :role, :is_register, :status)";
        return getUser(sql, params).orElse(null);
    }

    @Transactional
    public void changeStatus(Long userId, String status) {
        var sql = """
                UPDATE users
                SET status = :status
                WHERE user_id = :id
                """;
        var params = new MapSqlParameterSource()
                .addValue("id", userId)
                .addValue("status", status);
        writeDb.update(sql, params);
    }

    public List<User> findAll() {
        var sql = """
                SELECT *
                FROM users
                where role = 'student'
                """;
        return writeDb.query(sql, (rs, i) -> new User(rs));
    }

    public PagingContainer<User> findAllWithPaging(int page, int limit, String key) {
        var offset = PagingUtil.calculateOffset(page, limit);
        var sql = """
                SELECT *
                FROM users
                WHERE role = 'student'
                    AND (first_name LIKE :key OR last_name LIKE :key OR email LIKE :key)
                LIMIT :limit OFFSET :offset
                """;
        var params = new MapSqlParameterSource()
                .addValue("limit", limit)
                .addValue("offset", offset)
                .addValue("key", "%" + key + "%");
        var content = writeDb.query(sql, params, (rs, i) -> new User(rs));
        var totalRecords = writeDb.queryForObject(
                "SELECT COUNT(1) FROM users WHERE role = 'student' AND (first_name LIKE :key OR last_name LIKE :key OR email LIKE :key)",
                new MapSqlParameterSource().addValue("key", "%" + key + "%"),
                Long.class
        );
        return new PagingContainer<>(page, limit, totalRecords, content);
    }

    public Optional<User> findById(Long id) {
        var sql = """
                SELECT *
                FROM users
                WHERE id = :id
                """;
        return getUser(sql, new MapSqlParameterSource().addValue("id", id));
    }

    public Optional<User> getById(Long id) {
        return findById(id);
    }

    public Optional<User> findByEmail(String email) {
        var sql = """
                SELECT *
                FROM users
                WHERE email = :email
                """;
        return getUser(sql, new MapSqlParameterSource().addValue("email", email));
    }

    public Optional<User> getByEmail(String email) {
        return findByEmail(email);
    }

    public Optional<UserPrincipal> findByEmailV2(String email) {
        var sql = """
                SELECT *
                FROM users
                WHERE email = :email
                """;
        return getUserPrincipal(sql, new MapSqlParameterSource().addValue("email", email));
    }

    public Optional<User> getUser(String sql, MapSqlParameterSource params) {
        return writeDb.query(sql, params, (rs, i) -> new User(rs)).stream().findFirst();
    }

    public Optional<UserPrincipal> getUserPrincipal(String sql, MapSqlParameterSource params) {
        return writeDb.query(sql, params, (rs, i) -> new UserPrincipal(rs)).stream().findFirst();
    }

    public void activateUser(String email) {
        var sql = """
                UPDATE users
                SET status = 'ACTIVE'
                WHERE email = :email
                """;
        var params = new MapSqlParameterSource().addValue("email", email);
        writeDb.update(sql, params);
    }
}
