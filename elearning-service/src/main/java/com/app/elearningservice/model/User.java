package com.app.elearningservice.model;

import com.app.elearningservice.model.enums.GenderEnum;
import com.app.elearningservice.model.enums.RoleEnum;
import com.app.elearningservice.model.enums.StatusEnum;
import com.app.elearningservice.utils.JDBCUtils;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PROTECTED)
public class User {
    long userId;
    String email;
    String password;
    String firstName;
    String lastName;
    String phone;
    String address;
    Date dob;
    String avatar;

    @Builder.Default
    GenderEnum gender = GenderEnum.OTHER;
    @Builder.Default
    RoleEnum role = RoleEnum.STUDENT;
    @Builder.Default
    StatusEnum status = StatusEnum.INACTIVE;
    String lastUpdate;

    public User(long userId, String email, String password) {
        this.userId = userId;
        this.email = email;
        this.password = password;
    }

    public User(ResultSet rs) throws SQLException {
        this(
                rs.getLong("user_id"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("phone"),
                rs.getString("address"),
                rs.getDate("dob"),
                rs.getString("avatar"),
                Optional.ofNullable(rs.getString("gender")).map(e -> GenderEnum.valueOf(e.toUpperCase())).orElse(null),
                RoleEnum.valueOf(rs.getString("role").toUpperCase()),
                StatusEnum.valueOf(rs.getString("status").toUpperCase()),
                JDBCUtils.getValueResultSet(rs, String.class, "", "updated_at")
        );
    }
}
