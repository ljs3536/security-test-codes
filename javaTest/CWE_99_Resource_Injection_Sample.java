package javaTest;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CWE_99_Resource_Injection_Sample {

    // 1. 외부 입력값을 URL 리소스로 직접 사용하여 연결하는 전형적인 리소스 삽입 (CWE-99 / SSRF)
    public InputStream fetchImageResourceUnsafe(String userResourceUrl) {
        
        try {
            // [취약점 포인트] 사용자가 전달한 URL(userResourceUrl)을 어떠한 검증(화이트리스트 등) 없이
            // 그대로 URL 객체로 생성하고 커넥션을 맺습니다.
            // 공격자가 "http://localhost:8080/admin/shutdown" 또는 "http://169.254.169.254/latest/meta-data/" (AWS 메타데이터) 
            // 같은 내부망 리소스 식별자를 주입하면, 서버가 대신 요청을 보내어 내부 정보가 유출됩니다.
            URL targetUrl = new URL(userResourceUrl);
            HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
            connection.setRequestMethod("GET");
            
            return connection.getInputStream();
            
        } catch (Exception e) {
            System.err.println("Resource fetch error: " + e.getMessage());
            return null;
        }
    }
}