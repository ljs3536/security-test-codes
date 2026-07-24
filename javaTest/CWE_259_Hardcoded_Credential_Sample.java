package javaTest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@RestController
public class CWE_259_Hardcoded_Credential_Sample {

    // [취약점 포인트] 소스코드 내부에 데이터베이스 접속 비밀번호나 
    // 외부 연동 API 시크릿 키를 문자열 상수로 직접 하드코딩함 (CWE-259)
    // 형상 관리 시스템(Git 등)에 소스코드가 올라가거나 빌드 결과물이 유출될 경우 
    // 누구나 민감한 인증 정보를 탈취하여 시스템에 무단 접근할 수 있음
    private static final String DB_ADMIN_USERNAME = "sa";
    private static final String DB_ADMIN_PASSWORD = "HardcodedSecurePassword123!"; // 하드코딩된 비밀번호

    @GetMapping("/api/admin/metrics")
    public String getSystemMetricsUnsafe(@RequestParam("metricType") String metricType) {
        try {
            // 하드코딩된 자격 증명을 사용하여 데이터베이스에 연결 시도
            String dbUrl = "jdbc:h2:mem:testdb";
            Connection conn = DriverManager.getConnection(dbUrl, DB_ADMIN_USERNAME, DB_ADMIN_PASSWORD);
            
            Statement stmt = conn.createStatement();
            // 쿼리 실행 시뮬레이션
            stmt.close();
            conn.close();

            return "System metrics retrieved successfully using hardcoded credentials.";
        } catch (Exception e) {
            return "Connection Error: " + e.getMessage();
        }
    }
}