package com.app.elearningservice.exception;

import com.app.elearningservice.model.enums.ErrorCodeEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppException extends RuntimeException {
    int errorCode;
    String errorMessage;
    String customMessage;

    public AppException(ErrorCodeEnum resp) {
        super(resp.getMessage());
        this.errorCode = resp.getCode();
        this.errorMessage = resp.name();
        this.customMessage = resp.getMessage();
    }

    public AppException(String errorCode, String message) {
        super(message);
        this.errorCode = 200;
        this.errorMessage = errorCode;
        this.customMessage = message;
    }
}
