package javaTest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
public class CWE_319_Cleartext_Transmission_Sample {

    // 1. 민감한 인증 정보나 개인정보를 암호화되지 않은 HTTP 프로토콜로 외부 서버에 전송하는 취약한 패턴 (CWE-319)
    @PostMapping("/api/sync/user-data")
    public String transmitDataUnsafe(
            @RequestParam("username") String username, 
            @RequestParam("authToken") String authToken) {
        
        try {
            // [취약점 포인트] 평문으로 전송되면 안 되는 민감한 토큰(authToken)과 사용자 정보를 
            // 보안 채널(HTTPS)이 아닌 암호화되지 않은 일반 HTTP URL로 전송함
            // 네트워크 구간에서 패킷을 가로채는 스니핑 공격에 노출될 경우 자격 증명이 그대로 탈취됨
            String targetUrl = "http://api.partner-system.com/v1/sync"; // 취약한 HTTP 프로토콜 사용
            
            URL url = new URL(targetUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String postData = "username=" + username + "&authToken=" + authToken;
            
            OutputStream os = conn.getOutputStream();
            os.write(postData.getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            return "Data transmitted via HTTP (Unsafe). Response Code: " + responseCode;
            
        } catch (Exception e) {
            return "Transmission Error: " + e.getMessage();
        }
    }
}