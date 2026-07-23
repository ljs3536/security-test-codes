package javaTest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class CWE_434_Unsafe_FileUpload_Sample {

    private static final String UPLOAD_DIR = "/var/app/uploads/";

    // 1. 업로드된 파일의 확장자 검증 없이 원본 파일명 그대로 저장하는 전형적인 취약점 (CWE-434)
    @PostMapping("/api/upload/profile")
    public String uploadProfileUnsafe(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "File is empty";
        }

        try {
            // [취약점 포인트] 사용자가 업로드한 원본 파일명(file.getOriginalFilename())을 
            // 어떠한 화이트리스트 확장자 검증(예: .jpg, .png 허용)이나 이름 난독화 없이
            // 업로드 디렉토리에 그대로 저장함
            // 공격자가 "webshell.jsp" 같은 악성 코드가 담긴 파일을 올리면 웹 서버 경로에 그대로 적재되어 실행 가능
            String originalFileName = file.getOriginalFilename();
            File destFile = new File(UPLOAD_DIR + originalFileName);
            
            file.transferTo(destFile);

            return "Upload success: " + originalFileName;
            
        } catch (IOException e) {
            return "Upload failed: " + e.getMessage();
        }
    }
}