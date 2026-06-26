import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

@RestController
@RequestMapping("/auth")

public class CWE_307_POC {
    // [취약점] 로그인 시도 횟수를 전혀 저장하거나 제한하지 않음
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        
        // 1. 단순 인증 로직
        if (checkPassword(username, password)) {
            return "Login Success";
        } else {
            // [취약점 지점] 
            // - 실패 시 카운트 증가 로직 없음
            // - 계정 잠금(Lockout) 메커니즘 없음
            // - 실패 시 지연(Delay) 처리 없음
            // - 공격자가 초당 수천 번의 요청을 보내도 서버는 무조건 검증을 수행함
            return "Login Failed";
        }
    }

    private boolean checkPassword(String username, String password) {
        // 실제 데이터베이스 조회 로직 (가정)
        return "admin".equals(username) && "1234".equals(password);
    }
}
