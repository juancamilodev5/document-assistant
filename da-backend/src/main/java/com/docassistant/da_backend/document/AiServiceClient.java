package com.docassistant.da_backend.document;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiServiceClient {

    private final RestTemplate restTemplate;

    @Value("${ai.service.url}")
    private String aiServiceUrl;

    @Value("${internal.api.key}")
    private String internalApiKey;

    @Async
    public void processDocument(Long documentId, String filePath) {
        try {
            log.info("sending document {} to AI service", documentId);

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Internal-Api-Key", internalApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = Map.of(
                    "document_id", documentId,
                    "file_path", filePath
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            restTemplate.exchange(
                    aiServiceUrl + "/documents/process",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            log.info("document {} sent to AI service successfully", documentId);
        } catch (Exception e) {
            log.error("failed to send document {} to AI service: {}", documentId, e.getMessage());
        }
    }
}