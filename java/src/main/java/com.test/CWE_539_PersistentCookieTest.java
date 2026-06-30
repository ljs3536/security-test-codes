import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class CWE_539_PersistentCookieTest {

    @GetMapping("/api/remember-me")
    public String setRememberMe(HttpServletResponse response, String userToken) {
        
        // [위험] 중요 정보(Token, Session ID 등)를 쿠키에 담음
        Cookie tokenCookie = new Cookie("AuthToken", userToken);
        
        // [CWE-539 취약점] 쿠키의 생존 기간(Max-Age)을 1년(31536000초) 등 과도하게 길게 설정
        // 분석기가 setMaxAge()의 인자값이 특정 임계치(예: 0보다 큰 아주 큰 수)인지 확인해야 함
        tokenCookie.setMaxAge(60 * 60 * 24 * 365); 
        
        // Secure, HttpOnly 플래그 설정도 누락됨 (CWE-614 등 복합적 취약점)
        response.addCookie(tokenCookie);
        
        return "영구 쿠키 발급 완료";
    }
}