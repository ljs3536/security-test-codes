import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_99_ResourceInjection {

    // 분석기 테스트 포인트:
    // Source: request.getParameter("targetResource")
    // Sink: new URL(targetResource).openConnection()
    @GetMapping("/fetch")
    public String fetchResource(HttpServletRequest request) {
        
        // 사용자가 내부 시스템 리소스나 포트를 가리키는 URL을 입력
        // (예: "http://localhost:8080/admin/shutdown" 또는 "http://192.168.0.1:22")
        String targetResource = request.getParameter("targetResource");
        StringBuilder response = new StringBuilder();

        try {
            // 사용자 입력값이 그대로 URL(리소스 식별자)로 사용됨 (VULNERABLE)
            URL url = new URL(targetResource);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            return "Error fetching resource";
        }

        return response.toString();
    }
}