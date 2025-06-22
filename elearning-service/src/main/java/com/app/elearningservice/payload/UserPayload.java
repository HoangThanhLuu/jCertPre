package com.app.elearningservice.payload;

public record UserPayload(
        String firstName,
        String lastName,
        String address,
        String phone,
        String dob,
        String gender,
        Long userId
) {
}
