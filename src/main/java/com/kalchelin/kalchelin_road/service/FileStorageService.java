package com.kalchelin.kalchelin_road.service;
import com.kalchelin.kalchelin_road.exception.InvalidFileException;
import org.springframework.beans.factory.annotation.Value;      // 설정 파일 값을 가져오는 어노테이션
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;     // 업로드된 파일을 담는 타입

import java.io.IOException;
import java.nio.file.Files;     // 파일과 폴더에 대해 실제 작업을 하는 도구 모음
import java.nio.file.Path;      // 경로 위치를 문자열이 아니라 Path라는 객체로 다룸
import java.nio.file.Paths;     // 위의 Path를 만들어주는 도구
import java.util.List;
import java.util.Locale;
import java.util.UUID;          // 겹치지 않는 랜덤 이름을 만드는 도구

@Service
public class FileStorageService {
    // application.properties의 file.upload-dir 값(uploads)을 이 변수에 가져옴
    @Value("${file.upload-dir}")
    private String uploadDir;

    // 허용 확장자 (소문자로 비교)
    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "webp");

    // 허용 MTIME 타입 - 이중 확인
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of("image/jpeg", "image/png", "image/webp");

    // 최대 크기 10MB (바이트 단위)
    private static final long MAX_SIZE = 10 * 1024 * 1024;

    // 파일을 저장하고, 저장된 경로(문자열)를 돌려주는 메서드
    public String store(MultipartFile file) {
        validate(file);
        try {
            // 1) 저장 폴더가 없으면 만든다
            Path dirPath = Paths.get(uploadDir);
            Files.createDirectories(dirPath);

            // 2) 랜덤 이름 + 검증된 확장자로 새 이름을 짓는다
            String ext = extractExtension(file.getOriginalFilename());
            String savedName = UUID.randomUUID() + "." + ext;

            // 3) 실제로 그 위치에 파일을 저장한다
            Path targetPath = dirPath.resolve(savedName);   // 폴더 경로 + 파일 이름 = 최종 저장 위치
            file.transferTo(targetPath);    // 파일을 그 경로에 저장

            // 4) 저장된 경로를 문자열로 돌려준다(추후 DB에 저장)
            return uploadDir + "/" + savedName;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다.", e);
        }

    }

    // 파일이 조건에 맞는지 확인. 안 맞으면 InvalidFileException -> 400
    private void validate(MultipartFile file) {
        // 1. 파일이 있나
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("업로드할 파일이 없습니다.");
        }

        // 2. 크기
        if (file.getSize() > MAX_SIZE) {
            throw new InvalidFileException("파일 크기는 10MB 이하여야 합니다.");
        }

        // 3. MTIME 타입 (브라우저가 알려주는 파일 종류)
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
            throw new InvalidFileException("이미지 파일만 업로드할 수 있습니다.");
        }

        // 4. 확장자
        String ext = extractExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new InvalidFileException("jpg, jpeg, png, webp 파일만 업로드할 수 있습니다.");
        }
    }

    // 파일명에서 확장자만 소문자로 뽑아낸다. 없으면 예외
    private String extractExtension(String originalName) {
        if (originalName == null) {
            throw new InvalidFileException("파일 이름을 확인할 수 없습니다.");
        }
        int dotIndex = originalName.lastIndexOf(".");
        // 점이 없거나(-1), 맨 끝에 있으면 ("abc.") 확장자가 없는 것
        if (dotIndex == -1 || dotIndex == originalName.length() - 1) {
            throw new InvalidFileException("확장자가 없는 파일은 업로드할 수 없습니다.");
        }
        return originalName.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
    }
}
