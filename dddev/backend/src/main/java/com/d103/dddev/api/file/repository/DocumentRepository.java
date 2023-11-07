package com.d103.dddev.api.file.repository;

import com.d103.dddev.api.file.repository.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<FileEntity, Integer> {
    Optional<List<FileEntity>> findByDocumentId(String documentId);
    void deleteByDocumentId(String documentId);
}
