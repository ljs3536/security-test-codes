import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_285_AuthzTest {

    // [CWE-285 취약점] 관리자 전용 API에 일반 사용자 권한을 부여함
    // 논리적 결함: URL은 admin인데, 인가 설정은 hasRole('USER')로 되어 있음
    @PreAuthorize("hasRole('USER')") 
    @GetMapping("/api/admin/system-logs")
    public String getSystemLogs() {
        
        return "시스템 최상위 로그 데이터: [기밀 정보 노출]";
    }
}