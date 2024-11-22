package com.jenesano.directory.controller;

import com.jenesano.directory.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @Autowired
    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @Operation(summary = "Subir un archivo PDF",
            description = "Permite subir un archivo en formato PDF al sistema. Devuelve la URL del archivo subido.")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) {
        try {
            if (!Objects.equals(file.getContentType(), "application/pdf")) {
                return new ResponseEntity<>("El archivo no es PDF.", HttpStatus.BAD_REQUEST);
            }
            String fileUrl = fileUploadService.uploadPdf(file);
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al subir el archivo: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
