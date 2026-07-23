package javaTest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
public class CWE_918_SSRF_Sample {

    // 1. 사용자로부터 전달받은 URL을 검증 없이 그대로 호출하는 취약한 패턴 (CWE-918)
    @GetMapping("/api/fetch/url")
    public String fetchExternalUrlUnsafe(@RequestParam("targetUrl") String targetUrl) {
        StringBuilder responseContent = new StringBuilder();
        
        try {
            // [취약점 포인트] targetUrl에 대한 도메인 화이트리스트 검증이나 
            // 내부망 IP(예: localhost, 127.0.0.1, 169.254.169.254 등 메타데이터 서버) 차단 로직이 누락됨
            // 공격자가 내부망 인프라나 클라우드 메타데이터 엔드포인트를 타겟으로 지정하면 내부 시스템을 공격할 수 있음
            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();

        } catch (Exception e) {
            return "SSRF Fetch Error: " + e.getMessage();
        }

        return responseContent.toString();
    }
}