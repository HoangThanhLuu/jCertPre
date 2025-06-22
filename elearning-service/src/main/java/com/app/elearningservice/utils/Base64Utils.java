package com.app.elearningservice.utils;

import lombok.experimental.UtilityClass;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@UtilityClass
public class Base64Utils {
    public static final String BASE64_PREFIX = "data:image/png;base64,";
    public static final String BASE64_PDF_PREFIX = "data:application/pdf;base64,";

    public static String encode(String value) {
        return java.util.Base64.getEncoder().encodeToString(value.getBytes());
    }

    public static String decode(String value) {
        byte[] decodedValue = java.util.Base64.getDecoder().decode(value);
        return new String(decodedValue);
    }

    public static String encode(byte[] value) {
        return java.util.Base64.getEncoder().encodeToString(value);
    }

    public static byte[] decodeToBytes(String value) {
        return java.util.Base64.getDecoder().decode(value);
    }

    public static String encodeImage(byte[] value) {
        return BASE64_PREFIX + encode(value);
    }

    public static byte[] decodeImage(String value) {
        return decodeToBytes(value.replace(BASE64_PREFIX, ""));
    }

    public static String encodeImage(MultipartFile file) throws IOException {
        if (file != null && file.getContentType().startsWith("image")) {
            return encodeImage(file.getBytes());
        }
        return "";
    }

    public static String encodePdf(MultipartFile file) throws IOException {
        if (file != null && file.getContentType().startsWith(MediaType.APPLICATION_PDF_VALUE)) {
            var bytes = file.getBytes();
            return BASE64_PDF_PREFIX + encode(bytes);
        }
        return "";
    }

    public static void main(String[] args) {

    }
}
