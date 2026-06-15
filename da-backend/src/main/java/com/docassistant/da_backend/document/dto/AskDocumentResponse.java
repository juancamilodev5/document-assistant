package com.docassistant.da_backend.document.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AskDocumentResponse {

    private String answer;
    private Integer usedChunks;

}
