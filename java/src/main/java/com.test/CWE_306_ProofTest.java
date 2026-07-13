import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_306_ProofTest {

    // [CWE-306 증명용 코드]
    // GET 요청이라 CSRF 엔진이 작동하지 않으면서, 
    // 세션 확인 로직(인증)이 아예 없는 경우입니다.
    @GetMapping("/api/my/secret-data")
    public String getSecretData() {
        // [위험] 세션 객체(HttpSession) 인자를 아예 안 받아서, 
        // 인증 여부를 확인할 방법 자체가 없음.
        return "진짜 비밀 정보: [인증 절차 없이 누구나 접근 가능]";
    }
}