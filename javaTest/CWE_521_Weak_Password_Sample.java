package javaTest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_521_Weak_Password_Sample {

    // [취약점 포인트] 비밀번호 변경/리셋 시 비밀번호 최소 길이 및 복잡도 검증 정책이 극단적으로 취약함 (CWE-521)
    // 최소 길이 조건이 4자리로 지나치게 짧고, 영문 대소문자/숫자/특수문자 혼용 및 연속/반복 문자 제한 등의 정규식 검증이 누락됨
    @PostMapping("/api/user/change-password")
    public String changePasswordUnsafe(
            @RequestParam("userId") String userId,
            @RequestParam("newPassword") String newPassword) {
        
        try {
            // [취약점 싱크] 4자리 이상의 매우 짧고 단순한 비밀번호(예: "1234", "abcd")까지 그대로 허용함
            if (newPassword == null || newPassword.length() < 4) {
                return "Error: Password must be at least 4 characters long.";
            }

            return "Password successfully updated with weak policy (Unsafe).";
        } catch (Exception e) {
            return "Password Update Error: " + e.getMessage();
        }
    }
}