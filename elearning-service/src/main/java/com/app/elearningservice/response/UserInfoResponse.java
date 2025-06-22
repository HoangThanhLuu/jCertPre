package com.app.elearningservice.response;


import com.app.elearningservice.model.User;

import java.util.Date;
import java.util.Optional;

public record UserInfoResponse(
        String email,
        String firstName,
        String lastName,
        String gender,
        String avatar,
        String phone,
        String address,
        Date dob,
        String lastUpdate
) {
    public UserInfoResponse(User u) {
        this(
                u.getEmail(),
                u.getFirstName(),
                u.getLastName(),
                Optional.ofNullable(u.getGender()).map(Enum::name).orElse(null),
                u.getAvatar(),
                u.getPhone(),
                u.getAddress(),
                u.getDob(),
                u.getLastUpdate()
        );
    }
}
