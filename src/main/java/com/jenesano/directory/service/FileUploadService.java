package com.jenesano.directory.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class FileUploadService {

    @Value("${google.credentials.file.path}")
    private String credentialsFilePath;

    @Value("${google.application.name}")
    private String applicationName;

    private Drive getDriveService() throws GeneralSecurityException, IOException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(credentialsFilePath))
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/drive.file"));
        return new Drive.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(applicationName)
                .build();
    }

    public String uploadPdf(MultipartFile file) throws GeneralSecurityException, IOException {
        Drive driveService = getDriveService();

        File fileMetadata = new File();
        fileMetadata.setName(file.getOriginalFilename());
        fileMetadata.setMimeType("application/pdf");

        InputStreamContent mediaContent = new InputStreamContent("application/pdf", file.getInputStream());

        File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, webContentLink, webViewLink")
                .execute();

        Permission permission = new Permission();
        permission.setRole("reader");
        permission.setType("anyone");
        driveService.permissions().create(uploadedFile.getId(), permission).execute();

        return uploadedFile.getWebContentLink();
    }
}
