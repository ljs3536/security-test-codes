import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;

@RestController
public class CWE_113_Split_Test {
    // [취약점] 사용자 입력(authHeader)이 응답 헤더에 그대로 삽입됨
    @GetMapping("/api/set-header")
    public String setHeader(@RequestParam("auth") String authHeader, HttpServletResponse response) {
        
        // 공격자가 "Value\r\nSet-Cookie: session=hacked"를 입력하면 헤더 위조 가능
        // Sink: 직접적인 헤더 삽입
        response.setHeader("X-Auth-Token", authHeader);
        
        return "Header set";
    }
}
