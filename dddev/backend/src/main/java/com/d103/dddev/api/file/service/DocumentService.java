package com.d103.dddev.api.file.service;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
    String saveImg(MultipartFile[] files);
}
