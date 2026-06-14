package com.docassistant.da_backend.document.dto;

import com.docassistant.da_backend.document.DocumentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStatusRequest {
    private DocumentStatus status;
}
