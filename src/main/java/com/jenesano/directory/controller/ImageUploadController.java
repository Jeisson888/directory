package com.jenesano.directory.controller;

import com.jenesano.directory.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
public class ImageUploadController {

    private final ImageUploadService imageUploadService;

    @Autowired
    public ImageUploadController(ImageUploadService imageUploadService) {
        this.imageUploadService = imageUploadService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OWNER')")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image) {
        try {
            String imageUrl = imageUploadService.uploadImage(image);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al subir la imagen: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
