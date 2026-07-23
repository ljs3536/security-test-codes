package javaTest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_352_CSRF_Sample {

    // 1. 상태 변경 요청(비밀번호 변경 등)에 대해 CSRF 토큰 검증이나 Anti-CSRF 로직이 없는 취약한 패턴 (CWE-352)
    @PostMapping("/api/user/password/change")
    public String changePasswordUnsafe(@RequestParam("newPassword") String newPassword) {
        try {
            // [취약점 포인트] 시큐어코딩 및 스프링 시큐리티 기본 설정에서 
            // CSRF 보호 기능(.csrf().disable() 등)이 꺼져 있거나 
            // 폼/API 요청 시 고유한 CSRF 토큰을 검증하는 로직이 누락되어 있음
            // 공격자가 악성 사이트에 사용자를 유도시켜 의도치 않은 비밀번호 변경 요청을 백그라운드로 전송할 수 있음
            
            // 가상의 비밀번호 변경 로직 수행
            boolean isUpdated = updatePasswordInDatabase(newPassword);

            if (isUpdated) {
                return "Password changed successfully.";
            } else {
                return "Password change failed.";
            }
            
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private boolean updatePasswordInDatabase(String password) {
        // DB 업데이트 로직 시뮬레이션
        return true;
    }
}