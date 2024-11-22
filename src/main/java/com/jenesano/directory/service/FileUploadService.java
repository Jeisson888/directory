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

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class FileUploadService {

    @Value("${google.credentials.json}")
    private String credentialsJson;

    @Value("${google.application.name}")
    private String applicationName;

    // Método para obtener el servicio de Google Drive utilizando las credenciales de la API.
    private Drive getDriveService() throws GeneralSecurityException, IOException {
        // Configura el transporte HTTP y el formato JSON.
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        // Carga las credenciales de la API desde un flujo de bytes (en este caso desde un String).
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream("/etc/secrets/credentials.json"))
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/drive.file"));
        /*GoogleCredential credential = GoogleCredential.fromStream(new ByteArrayInputStream(credentialsJson.getBytes()))
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/drive.file"));*/

        // Crea e inicializa el servicio de Google Drive con las credenciales.
        return new Drive.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(applicationName)
                .build();
    }

    // Método para cargar un archivo PDF en Google Drive y establecer permisos públicos.
    public String uploadPdf(MultipartFile file) throws GeneralSecurityException, IOException {
        // Obtiene el servicio de Google Drive.
        Drive driveService = getDriveService();

        // Crea los metadatos del archivo que se subirá.
        File fileMetadata = new File();
        fileMetadata.setName(file.getOriginalFilename());
        fileMetadata.setMimeType("application/pdf");

        // Crea el contenido del archivo con el flujo de entrada del archivo PDF.
        InputStreamContent mediaContent = new InputStreamContent("application/pdf", file.getInputStream());

        // Sube el archivo a Google Drive y obtiene la respuesta con los detalles del archivo.
        File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, webContentLink, webViewLink")
                .execute();

        // Crea y establece permisos para que cualquier persona pueda ver el archivo.
        Permission permission = new Permission();
        permission.setRole("reader");
        permission.setType("anyone");
        driveService.permissions().create(uploadedFile.getId(), permission).execute();

        // Retorna el enlace de contenido web del archivo subido.
        return uploadedFile.getWebContentLink();
    }
}
