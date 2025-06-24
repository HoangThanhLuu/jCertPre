package com.app.elearningservice.controller;

import com.app.elearningservice.model.ResponseContainer;
import com.app.elearningservice.model.User;
import com.app.elearningservice.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("enrollment")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @GetMapping
    public Object findAll(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "key", defaultValue = "") String key,
            @AuthenticationPrincipal User user
    ) {
        return ResponseContainer.success(enrollmentService.findAll(page, size, key, user.getUserId()));
    }

    @GetMapping("mana")
    public Object findAllNotMe(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "key", defaultValue = "") String key,
            @AuthenticationPrincipal User user
    ) {
        return ResponseContainer.success(enrollmentService.findAllNotMe(page, size, key, user.getUserId()));
    }

    @PatchMapping("{enrollmentId}/{courseId}")
    public Object update(@PathVariable Long enrollmentId, @RequestParam String status, @PathVariable Long courseId) {
        enrollmentService.update(enrollmentId, status, courseId);
        return ResponseContainer.success();
    }
}
