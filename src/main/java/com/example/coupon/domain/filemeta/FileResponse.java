package com.example.coupon.domain.filemeta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class FileResponse {

    private Long id;
    private String originalFilename;
    private String storedFilename;
    private String uploadPath;
    private String contentType;
    private long fileSize;
    private LocalDateTime uploadedAt;

    public static FileResponse from(FileMeta meta) {
        return new FileResponse(
                meta.getId(),
                meta.getOriginalFilename(),
                meta.getStoredFilename(),
                meta.getUploadPath(),
                meta.getContentType(),
                meta.getFileSize(),
                meta.getUploadedAt()
        );
    }
}
