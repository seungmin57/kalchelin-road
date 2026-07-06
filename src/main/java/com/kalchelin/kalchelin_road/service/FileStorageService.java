package com.kalchelin.kalchelin_road.service;
import org.springframework.beans.factory.annotation.Value;      // 설정 파일 값을 가져오는 어노테이션
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;     // 업로드된 파일을 담는 타입

import java.io.IOException;
import java.nio.file.Files;     // 파일과 폴더에 대해 실제 작업을 하는 도구 모음
import java.nio.file.Path;      // 경로 위치를 문자열이 아니라 Path라는 객체로 다룸
import java.nio.file.Paths;     // 위의 Path를 만들어주는 도구
import java.util.UUID;          // 겹치지 않는 랜덤 이름을 만드는 도구

@Service
public class FileStorageService {
    // application.properties의 file.upload-dir 값(uploads)을 이 변수에 가져옴
    @Value("${file.upload-dir}")
    private String uploadDir;

    // 파일을 저장하고, 저장된 경로(문자열)를 돌려주는 메서드
    public String store(MultipartFile file) {
        try {
            // 1) 저장 폴더가 없으면 만든다
            Path dirPath = Paths.get(uploadDir);
            Files.createDirectories(dirPath);

            // 2) 파일 이름이 겹치지 않게 랜덤 이름으로 새로 짓는다
            String originalName = file.getOriginalFilename();
            String ext = originalName.substring(originalName.lastIndexOf("."));  // ".jpg"만 추출
            String savedName = UUID.randomUUID() + ext;

            // 3) 실제로 그 위치에 파일을 저장한다
            Path targetPath = dirPath.resolve(savedName);   // 폴더 경로 + 파일 이름 = 최종 저장 위치
            file.transferTo(targetPath);    // 파일을 그 경로에 저장

            // 4) 저장된 경로를 문자열로 돌려준다(추후 DB에 저장)
            return uploadDir + "/" + savedName;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다.", e);
        }

    }
}
