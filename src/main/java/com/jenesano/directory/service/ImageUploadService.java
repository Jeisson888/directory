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

    public String uploadImage(MultipartFile image) throws Exception {
        byte[] imageBytes = image.getBytes();

        ByteArrayResource byteArrayResource = new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
        };

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("key", imgbbApiKey);
        body.add("image", byteArrayResource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String uploadUrl = "https://api.imgbb.com/1/upload";

        ResponseEntity<Map> response = restTemplate.exchange(
                uploadUrl,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
            return (String) data.get("url");
        }

        throw new Exception("Error al subir la imagen.");
    }
}
