package com.docassistant.da_backend.document.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AskDocumentRequest {

    @NotNull
    private Long documentId;

    @NotBlank
    private String question;

}
