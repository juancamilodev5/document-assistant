package com.docassistant.da_backend.document.dto;

import com.docassistant.da_backend.document.DocumentStatus;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DocumentResponse {
    private Long id;
    private String originalName;
    private Long sizeBytes;
    private String mimeType;
    private DocumentStatus status;
    private LocalDateTime createdAt;
}