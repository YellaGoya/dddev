package com.d103.dddev.api.file.service;

import com.d103.dddev.api.file.repository.DocumentRepository;
import com.d103.dddev.api.file.repository.entity.FileEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.TransactionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.NoSuchFileException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentServiceImpl implements DocumentService{
    @Value("${spring.servlet.multipart.location}")
    private String FILE_PATH;

    @Value("${file.folder.document}")
    private String DOCUMENT_FOLDER;
    @Value("{file.return.link}")
    private String DOCUMENT_RETURN_LINK;

    private final DocumentRepository documentRepository;

    @Override
    public String saveImg(String documentId, MultipartFile file) throws Exception {
        String path = FILE_PATH + DOCUMENT_FOLDER;
        String savedName = saveDocumentImg(documentId, path, file);
        return DOCUMENT_RETURN_LINK + savedName;
    }

    @Override
    public String createRandomFileName() throws Exception {
        return UUID.randomUUID().toString();
    }
    public String saveDocumentImg(String documentId, String path, MultipartFile newFile) throws Exception {
        if (newFile.isEmpty())
            throw new NoSuchFileException("저장하려는 파일이 없습니다.");

        // 파일 정보
        String originalFilename = newFile.getOriginalFilename();
        String uuid = createRandomFileName();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String savedName = uuid + extension;
        String savedPath = path + savedName;

        // 파일 객체 생성
        File file = new File(savedPath);

        // 서버에 저장
        newFile.transferTo(file);

        // content-type
//      String type = Files.probeContentType(file.toPath());

        // dto build
        FileEntity fileEntity = FileEntity.builder()
                .documentId(documentId)
                .filePath(savedPath)
                .fileName(savedName)
                .build();

        // db에 저장
        try{
            documentRepository.saveAndFlush(fileEntity);
        }catch(Exception e){
            throw new TransactionException("데이터베이스 파일 경로 저장에 실패했습니다.");
        }

        return savedName;
    }
}
