package com.app.elearningservice.controller;

import com.app.elearningservice.model.MediaLesson;
import com.app.elearningservice.model.ResponseContainer;
import com.app.elearningservice.payload.SyllabusPayload;
import com.app.elearningservice.security.UserPrincipal;
import com.app.elearningservice.service.SyllabusService;
import com.app.elearningservice.utils.Base64Utils;
import com.app.elearningservice.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Log
@RestController
@RequestMapping("syllabus")
@RequiredArgsConstructor
public class SyllabusController {
    private final SyllabusService syllabusService;

    @GetMapping("course/{courseId}")
    public Object findByCourseId(@PathVariable Long courseId) {
        return ResponseContainer.success(syllabusService.findByCourseId(courseId));
    }

    @DeleteMapping("media/{mediaId}")
    public Object deleteMedia(@PathVariable Long mediaId) {
        syllabusService.deleteMedia(mediaId);
        return ResponseContainer.success("Delete media success");
    }

    @DeleteMapping("{lessonId}")
    public Object deleteLesson(@PathVariable Long lessonId) {
        syllabusService.deleteLesson(lessonId);
        return ResponseContainer.success("Delete lesson success");
    }

    @PatchMapping
    public Object update(@RequestBody SyllabusPayload data) {
        syllabusService.update(data);
        return ResponseContainer.success("Update syllabus success");
    }

    @PostMapping("media")
    public Object media(@RequestParam("lessonId") Long lessonId, @RequestParam("file") MultipartFile file) {
        syllabusService.saveMedia(
                lessonId,
                file.getOriginalFilename(),
                ImageUtils.uploadImageToCloudinary(file),
                file.getContentType()
        );
        return ResponseContainer.success("Save media success");
    }


    @PostMapping
    public Object save(
            @RequestPart("data") SyllabusPayload data,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        try {
            var medias = new ArrayList<MediaLesson>();
            if (CollectionUtils.isNotEmpty(files)) {
                var listMedia = files.stream()
                        .map(e -> new MediaLesson(
                                0L,
                                data.getCourseId(),
                                e.getOriginalFilename(),
                                ImageUtils.uploadImageToCloudinary(e),
                                e.getContentType(),
                                null
                        ))
                        .toList();
                medias.addAll(listMedia);
            }
            syllabusService.save(data, medias);
            return ResponseContainer.success("Save syllabus success");
        } catch (Exception e) {
            return ResponseContainer.error(e.getMessage());
        }
    }

}
