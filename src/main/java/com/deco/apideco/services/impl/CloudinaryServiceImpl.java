package com.deco.apideco.services.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.deco.apideco.services.CloudinaryService;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret) {
        cloudinary = new Cloudinary(Map.of(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }

    @Override
    public Map<?, ?> upload(MultipartFile multipartFile) throws Exception {
        File file = convert(multipartFile);
        return uploadAndCleanup(file);
    }

    @Override
    public Map<?, ?> upload(File file) throws Exception {
        return uploadAndCleanup(file);
    }

    private Map<String, Object> uploadAndCleanup(File file) throws Exception {
        try {
            Map result = cloudinary.uploader().upload(file, ObjectUtils.asMap(
                    "folder", "productos"));

            return Map.of(
                    "url", result.get("secure_url"), // ← fix del typo
                    "public_id", result.get("public_id"));
        } finally {
            Files.deleteIfExists(file.toPath()); // siempre limpia aunque falle
        }
    }

    @Override
    public Map<?, ?> delete(String id) throws Exception {
        return cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
    }

    private File convert(MultipartFile multipartFile) throws Exception {
        File file = File.createTempFile("upload_", "_" + multipartFile.getOriginalFilename());
        try (FileOutputStream fo = new FileOutputStream(file)) {
            fo.write(multipartFile.getBytes());
        }
        return file;
    }

    @Override
    public Map<?, ?> upload(MultipartFile multipartFile, String publicId) throws Exception {
        File file = convert(multipartFile);
        try {
            Map result = cloudinary.uploader().upload(file, ObjectUtils.asMap(
                    "folder", "productos",
                    "public_id", publicId));
            return Map.of(
                    "url", result.get("secure_url"),
                    "public_id", result.get("public_id"));
        } finally {
            Files.deleteIfExists(file.toPath());
        }
    }
}
