package com.d103.dddev.api.file.repository.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "file")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Builder
public class FileEntity {
    @Id
    private int id;
    @Column(name = "document_id")
    private String documentId;
    @Column(name = "file_path")
    private String filePath;
    @Column(name = "file_name")
    private String fileName;
}
