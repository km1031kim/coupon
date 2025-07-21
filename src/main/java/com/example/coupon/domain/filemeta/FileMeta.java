package com.example.coupon.domain.filemeta;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFilename;
    private String storedFilename;
    private String uploadPath;
    private String contentType;
    private long fileSize;
    private LocalDateTime uploadedAt;

    @Builder
    public FileMeta(String originalFilename, String storedFilename, String uploadPath, String contentType, long fileSize, LocalDateTime uploadedAt) {
        this.originalFilename = originalFilename;
        this. storedFilename = storedFilename;
        this.uploadPath = uploadPath;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.uploadedAt = uploadedAt;

    }

}