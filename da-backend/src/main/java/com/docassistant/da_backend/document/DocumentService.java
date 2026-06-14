package com.docassistant.da_backend.document;

import com.docassistant.da_backend.document.dto.DocumentResponse;
import com.docassistant.da_backend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final AiServiceClient aiServiceClient;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public DocumentResponse upload(MultipartFile file, User user) throws IOException {

        String mimeType = file.getContentType();
        if (mimeType == null || !mimeType.equals("application/pdf")) {
            throw new RuntimeException("only pdf files are allowed");
        }

        Path userDir = Paths.get(uploadDir, String.valueOf(user.getId()));
        Files.createDirectories(userDir);

        String uniqueFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = userDir.resolve(uniqueFilename);

        Files.copy(file.getInputStream(), filePath);

        Document document = Document.builder()
                .user(user)
                .filename(uniqueFilename)
                .originalName(file.getOriginalFilename())
                .sizeBytes(file.getSize())
                .mimeType(mimeType)
                .status(DocumentStatus.PROCESSING)
                .build();

        Document saved = documentRepository.save(document);

        aiServiceClient.processDocument(saved.getId(), filePath.toAbsolutePath().toString());

        return toResponse(saved);
    }

    public void changeStatus(Long documentId, DocumentStatus status) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("document not found"));

        if (document.getStatus() != DocumentStatus.PROCESSING)
            throw new RuntimeException("document status cannot be changed");

        document.setStatus(status);
        document.setProcessedAt(LocalDateTime.now());
        documentRepository.save(document);
    }

    public List<DocumentResponse> getUserDocuments(User user) {
        return documentRepository.findByUser(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private DocumentResponse toResponse(Document doc) {
        return DocumentResponse.builder()
                .id(doc.getId())
                .originalName(doc.getOriginalName())
                .sizeBytes(doc.getSizeBytes())
                .mimeType(doc.getMimeType())
                .status(doc.getStatus())
                .createdAt(doc.getCreatedAt())
                .build();
    }
}