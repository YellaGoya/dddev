package com.d103.dddev.api.file.service;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
    String saveImg(String documentId, MultipartFile file) throws Exception;
    String createRandomFileName() throws Exception;
    void deleteFile(String documentId);
}
