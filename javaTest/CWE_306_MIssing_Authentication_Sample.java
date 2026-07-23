package javaTest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_306_Missing_Authentication_Sample {

    // 1. 로그인 여부나 사용자 인증 세션을 전혀 검증하지 않고 수행되는 중요 기능 엔드포인트 (CWE-306)
    @PostMapping("/api/admin/system/reset")
    public String resetSystemConfigurationUnsafe(@RequestParam("confirmKey") String confirmKey) {
        
        // [취약점 포인트] 시스템 초기화 및 관리자 전용 변경 등 핵심적인 중요 기능임에도 불구하고,
        // 요청을 보낸 사용자가 올바른 인증(Authentication)을 거쳤는지 확인하는 
        // 세션 체크(HttpSession, SecurityContext 등)나 토큰 검증 로직이 아예 누락되어 있음
        // 인증되지 않은 익명의 사용자도 URL과 파라미터만 알면 해당 기능을 무단으로 실행할 수 있음
        
        if ("CONFIRM".equals(confirmKey)) {
            executeSystemReset();
            return "System configuration has been reset successfully.";
        } else {
            return "Invalid confirmation key.";
        }
    }

    private void executeSystemReset() {
        // 시스템 초기화 로직 시뮬레이션
    }
}