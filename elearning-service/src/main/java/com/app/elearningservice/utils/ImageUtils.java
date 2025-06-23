package com.app.elearningservice.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.logging.Level;

@Log
@UtilityClass
public class ImageUtils {
    private static final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "",
            "api_key", "",
            "api_secret", "bg-En6Yj5cwmBZR-dCtt6-o7VG8"
    ));
    public String getBase64FromImageUrl(String url) {
        try {
            var imageUrl = URI.create(url).toURL();
            var ucon = imageUrl.openConnection();
            var is = ucon.getInputStream();
            var baos = new ByteArrayOutputStream();
            var buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, read);
            }
            baos.flush();
            return Base64Utils.encodeImage(baos.toByteArray());
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error while converting image to base64: {0}", e.getMessage());
        }
        return null;
    }

    public String uploadImageToCloudinary(MultipartFile file) {
        try {
            var result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "resource_type", "auto"
            ));
            log.log(Level.INFO, "Uploaded image to Cloudinary: {0}", result);
            return (String) result
                    .get("secure_url");
        }catch (Exception ex) {
            log.log(Level.SEVERE, "Error while uploading image to Cloudinary: {0}", ex);
            return "";
        }
    }
}
