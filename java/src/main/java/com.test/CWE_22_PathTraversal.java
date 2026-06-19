import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_22_PathTraversal {

    // 분석기 테스트 포인트:
    // Source: request.getParameter("fileName")
    // Sink: new FileInputStream(file)
    @GetMapping("/download")
    public void downloadFile(HttpServletRequest request) throws IOException {
        
        // 사용자 입력값 (예: "../../../windows/system.ini")
        String fileName = request.getParameter("fileName");
        
        // 기본 다운로드 디렉토리와 사용자 입력값을 결합
        String basePath = "/var/www/uploads/";
        File file = new File(basePath + fileName);
        
        // 경로 조작 필터링(예: replace("../", "")) 없이 바로 파일 읽기 수행 (VULNERABLE)
        FileInputStream fis = new FileInputStream(file);
        
        int data;
        while ((data = fis.read()) != -1) {
            // 응답으로 파일 내용 출력 로직 (생략)
            System.out.print((char) data);
        }
        fis.close();
    }
}