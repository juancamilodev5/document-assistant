package com.docassistant.da_backend.document;

import com.docassistant.da_backend.document.dto.DocumentResponse;
import com.docassistant.da_backend.document.dto.UpdateStatusRequest;
import com.docassistant.da_backend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<DocumentResponse> upload(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user
    ) throws IOException {
        return ResponseEntity.status(201).body(documentService.upload(file, user));
    }

    @GetMapping
    public ResponseEntity<List<DocumentResponse>> getUserDocuments(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(documentService.getUserDocuments(user));
    }

    @PatchMapping("/{documentId}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long documentId,
            @RequestBody UpdateStatusRequest request) {
        documentService.changeStatus(documentId, request.getStatus());
        return ResponseEntity.noContent().build();
    }
}