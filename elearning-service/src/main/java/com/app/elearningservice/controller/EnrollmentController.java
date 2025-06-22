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
            @RequestParam(value = "key", defaultValue = "") String key
    ) {
        return ResponseContainer.success(enrollmentService.findAll(page, size, key));
    }

    @PatchMapping("{enrollmentId}")
    public Object update(@PathVariable Long enrollmentId, @RequestParam String status) {
        enrollmentService.update(enrollmentId, status);
        return ResponseContainer.success();
    }

    @PostMapping
    public Object enroll(@AuthenticationPrincipal User user, @RequestParam("cid") Long cid) {
        enrollmentService.save(user, cid);
        return ResponseContainer.success("Enroll success");
    }
}
