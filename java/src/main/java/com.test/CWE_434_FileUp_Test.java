import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;

@RestController
public class CWE_434_FileUp_Test {
    // 1. 경로 조작(CWE-22) 이슈 회피를 위해 하드코딩된 안전한 경로 사용
    private static final String UPLOAD_DIR = "/var/www/uploads/";

    @PostMapping("/api/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        
        // 2. [핵심] 파일 이름 그대로 사용 (경로 조작 아님)
        String fileName = file.getOriginalFilename();
        Path targetPath = Paths.get(UPLOAD_DIR, fileName);

        // 3. [핵심] 확장자 검증 로직이 전혀 없음!
        // 여기서 분석기가 '파일 업로드 Sink'는 탐지하되 
        // 434 룰이 있다면 반드시 경고를 띄워야 함
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        
        return "업로드 성공";
    }
}
