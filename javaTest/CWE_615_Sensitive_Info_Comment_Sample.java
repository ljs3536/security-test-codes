package javaTest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_615_Sensitive_Info_Comment_Sample {

    // [취약점 포인트] 소스코드 주석 내부에 시스템 내부 IP, 관리자 계정, 
    // 혹은 하드코딩된 내부 API 엔드포인트 등의 민감 정보를 그대로 기록함 (CWE-615)
    // 
    // [취약한 주석 예시]
    // TODO: 운영 서버 이관 전 내부 DB 접속 확인 필요 (IP: 192.168.10.150, Port: 5432)
    // ADMIN_CONTACT: dev_admin@internal.co.kr / Pass: Admin#1234!
    // Internal API Route: http://10.0.1.55:8080/internal/v1/system-dump
    
    @GetMapping("/api/system/status")
    public String getSystemStatusUnsafe() {
        try {
            // 주석에 노출된 내부망 주소나 관리자 정보는 빌드 및 배포 시 
            // 소스코드 형태 그대로 노출되어 정보 유출의 빌미가 됨
            return "System status normal. (Check source code comments for internal configuration)";
        } catch (Exception e) {
            return "Status Error: " + e.getMessage();
        }
    }
}