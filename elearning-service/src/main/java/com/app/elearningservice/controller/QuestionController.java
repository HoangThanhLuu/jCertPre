package com.app.elearningservice.controller;

import com.app.elearningservice.dto.QuestionDTO;
import com.app.elearningservice.model.ResponseContainer;
import com.app.elearningservice.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PatchMapping
    public Object save(@RequestBody QuestionDTO payload) {
        questionService.save(payload);
        return ResponseContainer.success("OK");
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable Long id) {
        questionService.delete(id);
        return ResponseContainer.success("OK");
    }
}
