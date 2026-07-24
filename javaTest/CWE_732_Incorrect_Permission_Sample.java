package javaTest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

@RestController
public class CWE_732_Incorrect_Permission_Sample {

    // 1. 중요한 설정 정보나 인증 키 파일을 디스크에 저장하면서 과도한 권한을 부여하는 취약한 패턴 (CWE-732)
    @PostMapping("/api/system/config/save")
    public String saveCriticalConfigUnsafe(@RequestParam("configData") String configData) {
        
        try {
            // [취약점 포인트] 시스템 핵심 설정이나 민감한 키가 담긴 파일을 생성할 때,
            // OS 기본 파일 생성 권한을 그대로 따르거나 누구나 읽고 쓸 수 있는 권한(예: 777, 세계 공유 권한)으로 생성함
            // 유닉스/리눅스 환경에서 악의적인 로컬 사용자가 해당 파일에 접근하여 민감 정보를 탈취하거나 변조할 수 있음
            File criticalFile = new File("/var/app/config/secret_system_config.properties");
            
            if (!criticalFile.getParentFile().exists()) {
                criticalFile.getParentFile().mkdirs();
            }

            FileWriter writer = new FileWriter(criticalFile);
            writer.write(configData);
            writer.close();

            // [참고] Java NIO에서 POSIX 권한을 명시적으로 제한하지 않고 기본 생성할 경우 CWE-732에 해당할 수 있음
            
            return "Critical configuration saved successfully (Unsafe Permission).";
        } catch (IOException e) {
            return "Error saving file: " + e.getMessage();
        }
    }
}