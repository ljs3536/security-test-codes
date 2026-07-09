import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;

@RestController
public class CWE_285_AuthzMissingTest {

    /**
     * [CWE-285 취약점] 개인정보 조회 기능
     * 로그인한 사용자가 다른 사람의 ID를 넣어도 그대로 정보를 가져옴 (Insecure Direct Object Reference).
     * 정상적인 코드라면 session.getAttribute("USER_ID")와 pathVariable인 {userId}를 비교해야 함.
     */
    @GetMapping("/api/user/profile/{userId}")
    public String getProfile(@PathVariable("userId") String userId, HttpSession session) {
        
        // [위험] 세션의 사용자 ID(로그인한 본인)와 요청한 userId가 일치하는지 확인하는 로직이 없음
        // 공격자가 URL에 다른 사람의 ID만 넣으면 정보가 다 털림
        
        return userId + "님의 프로필 정보: [민감한 개인정보 출력]";
    }
}