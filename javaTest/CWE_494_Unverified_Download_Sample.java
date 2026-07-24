package javaTest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class CWE_494_Unverified_Download_Sample {

    // [취약점 포인트] 원격 URL로부터 프로그램 업데이트 패치나 실행 파일, 플러그인 등을 다운로드할 때 
    // SHA-256 해시값 비교나 디지털 서명 검증 등 무결성 확인 절차를 거치지 않고 곧바로 저장함 (CWE-494)
    // 공격자가 네트워크 경로를 변조하여 악성 코드가 포함된 파일을 주입해도 시스템이 이를 그대로 수용하게 됨
    @PostMapping("/api/system/download-update")
    public String downloadUpdateUnsafe(@RequestParam("fileUrl") String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            String saveDir = "/var/app/updates/";
            String fileName = "patch_update.bin";
            Path targetPath = Paths.get(saveDir, fileName);

            // 디렉터리 생성
            Files.createDirectories(Paths.get(saveDir));

            // [취약점 싱크] 무결성 검증(Checksum / Hash / Signature Validation) 로직 없이 
            // 다운로드한 스트림을 파일 시스템에 곧바로 기록함
            try (InputStream in = url.openStream();
                 FileOutputStream fos = new FileOutputStream(targetPath.toFile())) {
                
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }

            // [취약한 로직] 다운로드 완료 후 파일의 해시값(SHA-256 등)을 공식 메타데이터와 대조하는 과정이 누락됨

            return "Update file downloaded successfully without integrity check (Unsafe).";
            
        } catch (Exception e) {
            return "Download Error: " + e.getMessage();
        }
    }
}