package com.deco.apideco.services;

import java.io.File;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    Map<?, ?> upload(MultipartFile multipartFile) throws Exception;
    Map<?, ?> upload(File file) throws Exception;
    Map<?, ?> upload(MultipartFile multipartFile, String publicId) throws Exception; // ← nuevo
    Map<?, ?> delete(String id) throws Exception;
}
