package com.gaurav.linkedin.uploader_service.controller;


import com.gaurav.linkedin.uploader_service.exceptions.ApiResponse;
import com.gaurav.linkedin.uploader_service.service.FileUploaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")

public class FileUploaderController {
    @Autowired
    private  FileUploaderService fileUploaderService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> uploadImage(@RequestParam MultipartFile file){
        try {
            String url = fileUploaderService.upload(file);
            return ResponseEntity.ok(new ApiResponse<>(url));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
