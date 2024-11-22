package com.jenesano.directory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class ImageUploadService {

    @Value("${imgbb.api.key}")
    private String imgbbApiKey;

    private final RestTemplate restTemplate;

    @Autowired
    public ImageUploadService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Método para subir una imagen a imgbb y retornar la URL de la imagen subida.
    public String uploadImage(MultipartFile image) throws Exception {
        byte[] imageBytes = image.getBytes();

        // Crea un recurso de byte array a partir de la imagen recibida.
        ByteArrayResource byteArrayResource = new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
        };

        // Configura los encabezados de la solicitud HTTP.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Crea el cuerpo de la solicitud con los datos de la imagen y la clave API.
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("key", imgbbApiKey);
        body.add("image", byteArrayResource);

        // Crea la solicitud HTTP con los encabezados y el cuerpo.
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // URL de la API de imgbb para cargar la imagen.
        String uploadUrl = "https://api.imgbb.com/1/upload";

        // Realiza la solicitud HTTP POST para cargar la imagen.
        ResponseEntity<Map> response = restTemplate.exchange(
                uploadUrl,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        // Procesa la respuesta de la API para obtener la URL de la imagen.
        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
            return (String) data.get("url");
        }

        // Si la respuesta no contiene la URL, lanza una excepción.
        throw new Exception("Error al subir la imagen.");
    }
}
