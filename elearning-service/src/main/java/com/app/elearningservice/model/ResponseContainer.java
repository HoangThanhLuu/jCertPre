package com.app.elearningservice.model;

import com.app.elearningservice.model.enums.ErrorCodeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE, staticName = "of")
public final class ResponseContainer<T> {

    private int status = 200;
    private String errorCode = "";
    private String message = "";
    private T data;

    public static <T> ResponseContainer<T> success() {
        return of(200, "", "Success", null);
    }


    public static <T> ResponseContainer<T> success(T data) {
        return of(200, "", "", data);
    }

    public static <T> ResponseContainer<T> success(T data, String message) {
        return of(200, "", message, data);
    }

    public static ResponseContainer<Object> failure(String message) {
        return failure(ErrorCodeEnum.BAD_REQUEST, message);
    }

    public static ResponseContainer<Object> error(Object data) {
        return of(
                ErrorCodeEnum.BAD_REQUEST.getCode(),
                ErrorCodeEnum.BAD_REQUEST.name(),
                ErrorCodeEnum.BAD_REQUEST.name(),
                data
        );
    }

    public static ResponseContainer<Object> failure(ErrorCodeEnum errorCode, String message) {
        return failure(errorCode.getCode(), errorCode.name(), message);
    }

    public static ResponseContainer<Object> failure(int status, String errorCode, String message) {
        return of(status, errorCode, message, Collections.emptyMap());
    }

    @JsonProperty("success")
    public boolean isSuccess() {
        return String.valueOf(status).startsWith("2") && StringUtils.isBlank(errorCode);
    }

}
