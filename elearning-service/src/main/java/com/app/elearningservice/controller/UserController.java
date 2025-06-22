package com.app.elearningservice.controller;

import com.app.elearningservice.model.ResponseContainer;
import com.app.elearningservice.model.User;
import com.app.elearningservice.model.enums.StatusEnum;
import com.app.elearningservice.payload.UserPayload;
import com.app.elearningservice.response.UserInfoResponse;
import com.app.elearningservice.service.UserService;
import com.app.elearningservice.utils.Base64Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("result")
    public Object result(@AuthenticationPrincipal User user) {
        return ResponseContainer.success(userService.result(user));
    }

    @GetMapping
    public Object findAll(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "key", defaultValue = "") String key
    ) {
        return ResponseContainer.success(userService.findAllWithPaging(page, size, key));
    }

    @PatchMapping("block/{id}")
    public Object block(@PathVariable Long id) {
        userService.changeStatus(id, StatusEnum.BLOCK.name());
        return ResponseContainer.success();
    }

    @PatchMapping("unblock/{id}")
    public Object unblock(@PathVariable Long id) {
        userService.changeStatus(id, StatusEnum.ACTIVE.name());
        return ResponseContainer.success();
    }

    @GetMapping("info")
    public Object info(@AuthenticationPrincipal User user) {
        return ResponseContainer.success(new UserInfoResponse(user));
    }

    @PatchMapping
    public Object update(@RequestBody UserPayload user, @AuthenticationPrincipal User currentUser) {
        userService.update(user, currentUser.getUserId());
        return ResponseContainer.success();
    }

    @PatchMapping("change-avatar")
    public Object changeAvatar(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User currentUser
    ) throws IOException {
        var url = Base64Utils.encodeImage(file);
        userService.updateAvatar(currentUser.getUserId(), url);
        return ResponseContainer.success("Change avatar success");
    }
}
