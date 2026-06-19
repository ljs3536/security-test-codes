import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CWE_918_SSRF {

    // 분석기 테스트 포인트:
    // Source: @RequestParam("targetUrl")
    // Sink: restTemplate.getForObject(targetUrl, String.class)
    @GetMapping("/proxy")
    public String proxyRequest(@RequestParam("targetUrl") String targetUrl) {
        
        // 화이트리스트 검증(예: 허용된 도메인인지 확인)이 없음 (VULNERABLE)
        // 공격자 입력 예시: "http://169.254.169.254/latest/meta-data/" (AWS 메타데이터 탈취)
        // 또는 "http://localhost:6379" (내부망 레디스 접근)
        
        RestTemplate restTemplate = new RestTemplate();
        
        try {
            // 사용자 입력값이 서버의 요청 URL로 그대로 사용됨
            String response = restTemplate.getForObject(targetUrl, String.class);
            return response;
        } catch (Exception e) {
            return "Failed to fetch from " + targetUrl;
        }
    }
}