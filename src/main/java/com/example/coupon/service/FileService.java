package com.example.coupon.service;

import com.example.coupon.domain.filemeta.FileMeta;
import com.example.coupon.domain.filemeta.FileResponse;
import com.example.coupon.repository.FileMetaRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
public class FileService {

    private static final int MB = 1024 * 1024;
    private static final int MAX_FILE_SIZE = 5 * MB;
    private static final List<String> SUPPORTED_EXTENSIONS = List.of(".csv", ".xls", ".xlsx");
    private static final List<String> REQUIRED_HEADERS = List.of("user_id");
    private static final String UPLOAD_DIR = "C:\\Users\\USER\\Desktop\\study\\coupon\\uploaded\\";

    private final FileMetaRepository fileMetaRepository;

    @Autowired
    public FileService(FileMetaRepository fileMetaRepository) {
        this.fileMetaRepository = fileMetaRepository;
    }


    @Transactional
    public FileResponse saveAndUpload(MultipartFile file) throws IOException {

        String filename = file.getOriginalFilename();

        log.info("파일명 : {}", filename);
        log.info("컨텐트타입 : {} ", file.getContentType());
        log.info("파일 크기 : {} ", file.getSize());

        // 타입 검증 - csv or excel
        validateFileSizeAndType(file);

        // 헤더 검증
        validateHeaders(file);

        // 파일 저장 및 메타데이터 엔티티 생성
        return storeFileAndMetadata(file);
    }

    private FileResponse storeFileAndMetadata(MultipartFile file) {

        String originalFilename = file.getOriginalFilename();
        String storedFilename = UUID.randomUUID() + "_" + originalFilename;
        File destination = new File(UPLOAD_DIR + storedFilename);
        String uploadPath = UPLOAD_DIR + storedFilename;


        try {
            // 업로드 폴더 없으면 생성
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            file.transferTo(destination);
            log.info("파일 저장 완료: {}", destination.getAbsolutePath());

            FileMeta fileMeta = FileMeta.builder()
                    .originalFilename(originalFilename)
                    .storedFilename(storedFilename)
                    .uploadPath(uploadPath)
                    .contentType(file.getContentType())
                    .fileSize(file.getSize())
                    .uploadedAt(LocalDateTime.now()).build();

            FileMeta savedFileMeta = fileMetaRepository.save(fileMeta);

            return FileResponse.from(savedFileMeta);

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생", e);
        }
    }


    private void validateFileSizeAndType(MultipartFile file) {

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("5MB 이하 파일만 허용됩니다.");
        }

        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();

        if (!isSupportedContentType(contentType) || !hasSupportedExtension(originalFilename)) {
            throw new IllegalArgumentException("csv, Excel(xlsx) 파일만 업로드할 수 있습니다.");
        }
    }

    /**
     * 필수 헤더가 있는지
     * @param file
     */
    private void validateHeaders(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();

        if (filename.endsWith(".csv")) {
            validateRequiredHeaders(file);
        } else if (filename.endsWith(".xls") || filename.endsWith(".xlsx")) {
            // 구현 필요
        } else {
            throw new IllegalArgumentException("지원하지 않는 파일 형식입니다. (csv, xls, xlsx)");
        }
    }

    private void validateRequiredHeaders(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser parser = CSVFormat.DEFAULT
                     .builder()
                     .setHeader()  // 첫 번째 줄을 헤더로 사용
                     .setSkipHeaderRecord(true)  // 헤더는 레코드로 처리하지 않음
                     .build()
                     .parse(reader)
            ) {
            // 헤더 검증
            List<String> headers = new ArrayList<>(parser.getHeaderMap().keySet());
            checkContainsRequiredHeaders(headers);
        }
    }

    private List<Map<String, String>> extractExcelWithHeaders(MultipartFile file) {
        return null;
    }


    private boolean isSupportedContentType(String contentType) {
        return contentType != null && (
                contentType.equals("text/csv") ||
                contentType.equals("application/vnd.ms-excel") || // 일부 csv 업로드 시 사용됨
                contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        );

    }

    private boolean hasSupportedExtension(String filename) {
        if (filename == null) return false;
        String lowerCase = filename.toLowerCase();
        return SUPPORTED_EXTENSIONS.stream().anyMatch(lowerCase::endsWith);
    }


    public void checkContainsRequiredHeaders(List<String> actualHeaders) {
        for (String required : REQUIRED_HEADERS) {
            if (!actualHeaders.contains(required)) {
                throw new IllegalArgumentException("필수 헤더 누락: " + required);
            }
        }
    }
}
