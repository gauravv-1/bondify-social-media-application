package com.gaurav.linkedin.uploader_service.service;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service

@Slf4j
public class CloudinaryFileUploadService implements FileUploaderService{

    private final Cloudinary cloudinary;

    public CloudinaryFileUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String upload(MultipartFile file) throws IOException {
        Map uploadResult  = cloudinary.uploader().upload(file.getBytes(),Map.of());

        return uploadResult.get("secure_url").toString();
    }
}
