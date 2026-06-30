import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
public class CWE_807_SecurityDecision {

    /**
     * [CWE-807 취약점] 검증되지 않은 외부 입력(Cookie)에 의존한 권한 인가
     */
    @GetMapping("/api/admin/dashboard")
    public String getAdminDashboard(HttpServletRequest request) {
        boolean isAdmin = false;

        // 클라이언트에서 전송된 쿠키 확인
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // [위험] 클라이언트가 변조하기 쉬운 "role" 쿠키 값을 직접 읽어 보안 결정에 사용
                if ("role".equals(cookie.getName()) && "admin".equals(cookie.getValue())) {
                    isAdmin = true;
                    break;
                }
            }
        }

        if (isAdmin) {
            return "관리자 대시보드 접근 성공. 기밀 데이터 출력...";
        } else {
            return "접근 권한이 없습니다.";
        }
    }
}