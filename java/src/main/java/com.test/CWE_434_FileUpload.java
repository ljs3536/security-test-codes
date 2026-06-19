import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

@RestController
public class CWE_434_FileUpload {

    // 분석기 테스트 포인트:
    // Source: MultipartFile file 로 들어온 데이터가
    // Sink: file.transferTo(dest) 로 확장자 검사 없이 바로 저장되는지 확인
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "File is empty";
        }

        try {
            // 사용자가 올린 원본 파일명 (예: "shell.jsp", "malware.exe")을 그대로 사용
            String originalFilename = file.getOriginalFilename();
            
            // 확장자 화이트리스트 검사나 파일명 난독화 없이 웹 접근이 가능한 경로에 저장 (VULNERABLE)
            String uploadDir = "/var/www/html/uploads/";
            File dest = new File(uploadDir + originalFilename);
            
            // Sink 지점
            file.transferTo(dest);
            
            return "File uploaded successfully: " + originalFilename;
        } catch (IOException e) {
            return "Upload failed";
        }
    }
} 
