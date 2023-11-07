package com.d103.dddev.api.file.repository;

import com.d103.dddev.api.file.repository.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<FileEntity, Integer> {
}
