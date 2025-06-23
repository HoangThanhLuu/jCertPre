package com.app.elearningservice.controller;

import com.app.elearningservice.model.ResponseContainer;
import com.app.elearningservice.payload.CoursePayload;
import com.app.elearningservice.security.UserPrincipal;
import com.app.elearningservice.service.CourseService;
import com.app.elearningservice.utils.Base64Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping("{courseId}")
    public Object findById(@PathVariable(value = "courseId") Long courseId) {
        return ResponseContainer.success(courseService.findById(courseId));
    }

    @GetMapping
    public Object findAll(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "key", defaultValue = "") String key,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseContainer.success(courseService.findAll(page, size, key, userPrincipal.getUserId()));
    }

    @PostMapping
    public Object upsert(
            @RequestPart("data") CoursePayload data,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail
    ) throws IOException {
        String url = null;
        if (thumbnail != null && thumbnail.getContentType().startsWith("image")) {
            url = Base64Utils.encodeImage(thumbnail);
        }
        courseService.save(data, url);
        return ResponseContainer.success("Upsert course success");
    }

    @DeleteMapping("{courseId}")
    public Object delete(@PathVariable(value = "courseId") Long courseId) {
        courseService.delete(courseId);
        return ResponseContainer.success("Delete course success");
    }
}
