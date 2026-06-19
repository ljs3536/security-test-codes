import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_494_UnverifiedDownload {

    // 분석기 테스트 포인트:
    // URL 통신으로 파일을 내려받은 후, MessageDigest나 Signature 객체를 사용한 
    // 해시/서명 검증 로직 없이 Runtime.exec()나 ClassLoader로 실행하는 패턴 감지
    @PostMapping("/update-system")
    public String downloadAndApplyUpdate() {
        try {
            // 취약점 1: HTTPS가 아닌 HTTP를 사용하여 중간자 공격(MITM)에 취약함
            URL updateUrl = new URL("http://update.example.com/latest-patch.jar");
            InputStream in = updateUrl.openStream();
            
            File patchFile = new File("/tmp/latest-patch.jar");
            FileOutputStream out = new FileOutputStream(patchFile);
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            in.close();
            out.close();

            // 취약점 2: 다운로드한 파일의 무결성(해시값이나 전자서명) 검증 없이 바로 실행 (VULNERABLE)
            Runtime.getRuntime().exec("java -jar " + patchFile.getAbsolutePath());

            return "Update applied successfully!";
        } catch (Exception e) {
            return "Update failed";
        }
    }
}