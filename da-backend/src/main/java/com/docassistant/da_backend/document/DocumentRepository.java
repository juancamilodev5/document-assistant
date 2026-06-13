package com.docassistant.da_backend.document;

import com.docassistant.da_backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByUser(User user);
}