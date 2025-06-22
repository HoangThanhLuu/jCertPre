package com.app.elearningservice.controller;

import com.app.elearningservice.dto.QuizzDTO;
import com.app.elearningservice.model.ResponseContainer;
import com.app.elearningservice.model.User;
import com.app.elearningservice.payload.QuizzPayload;
import com.app.elearningservice.payload.SubmitTestPayload;
import com.app.elearningservice.service.QuizzService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("quizz")
@RequiredArgsConstructor
public class QuizzController {
    private final QuizzService quizzService;

    @PostMapping
    public Object save(@RequestBody QuizzPayload payload) {
        quizzService.save(payload);
        return ResponseContainer.success("Save quizz success");
    }

    @GetMapping("course/{courseId}")
    public Object findByCourseId(@PathVariable(value = "courseId") Long courseId) {
        return ResponseContainer.success(quizzService.findByCourseId(courseId));
    }

    @GetMapping("{quizzId}/questions")
    public Object findQuestionsByQuizzId(@PathVariable(value = "quizzId") Long quizzId) {
        return ResponseContainer.success(quizzService.findQuestionsByQuizzId(quizzId));
    }

    @PatchMapping
    public Object update(@RequestBody QuizzDTO payload) {
        quizzService.update(payload.getQuizId(), payload.getTitle());
        return ResponseContainer.success("Update quizz success");
    }

    @DeleteMapping("{quizzId}")
    public Object delete(@PathVariable(value = "quizzId") Long quizzId) {
        quizzService.delete(quizzId);
        return ResponseContainer.success("Delete quizz success");
    }

}
