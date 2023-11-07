package com.d103.dddev.api.file.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentServiceImpl implements DocumentService{
    @Value("${spring.servlet.multipart.location}")
    private String FILE_PATH;

    @Value("${file.folder.document}")
    private String DOCUMENT_FOLDER;

    @Override
    public String saveImg(MultipartFile[] files) {
        return null;
    }
}
