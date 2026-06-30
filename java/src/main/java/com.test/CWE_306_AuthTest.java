import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_306_AuthTest {

    // [CWE-306 취약점] 비밀번호 변경이라는 '중요 기능'에 인증 확인 로직이 아예 없음
    // 정상이라면 @PreAuthorize("isAuthenticated()") 같은 어노테이션이나 세션 검증 로직이 있어야 함
    @PostMapping("/api/admin/change-password")
    public String changePassword(String targetUser, String newPassword) {
        
        // 인증 절차 없이 바로 로직 수행
        return targetUser + "의 비밀번호가 " + newPassword + "로 변경되었습니다.";
    }
}