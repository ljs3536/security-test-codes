import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;

@RestController
public class CWE_306_AuthMissingTest {

    /**
     * [CWE-306 취약점] 비밀번호 변경 기능
     * 인증되지 않은 사용자가 URL만 알면 바로 호출할 수 있음.
     * 정상적인 코드라면 HttpSession에서 로그인 정보를 확인하거나,
     * Spring Security의 @PreAuthorize("isAuthenticated()") 어노테이션이 있어야 함.
     */
    @PostMapping("/api/user/change-password")
    public String changePassword(@RequestParam("newPassword") String newPassword, HttpSession session) {
        
        // [위험] 세션 확인 로직(session.getAttribute("USER_ID"))이 누락됨
        // 이 상태면 누구나 비밀번호를 바꿀 수 있음
        
        return "비밀번호가 " + newPassword + "로 변경되었습니다.";
    }
}