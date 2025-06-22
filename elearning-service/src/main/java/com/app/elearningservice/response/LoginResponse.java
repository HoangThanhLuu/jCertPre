package com.app.elearningservice.response;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        boolean isAdmin,
        boolean isTeacher,
        boolean valid
) {
}
