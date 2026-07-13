import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;

@RestController
public class CWE_434_Pure_Test {
    
    // 경로 조작을 방지하기 위해 업로드 디렉토리를 고정함
    private static final String UPLOAD_DIR = "/var/www/webapps/root/";

    @PostMapping("/api/upload/pure")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        
        // 사용자가 제공한 파일명을 그대로 사용하지만, 
        // 경로는 UPLOAD_DIR로 고정되어 있어 CWE-22 탐지를 회피함
        String fileName = file.getOriginalFilename();
        Path targetPath = Paths.get(UPLOAD_DIR, fileName);

        // [핵심] 확장자 화이트리스트 검증(예: .jpg, .png만 허용)이 전혀 없음
        // 분석기가 파일 업로드 기능을 탐지하고, 화이트리스트 체크 로직 부재를 확인하면 434를 띄워야 함
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        
        return "파일 업로드 완료";
    }
}