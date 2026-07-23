package javaTest;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_807_Untrusted_Input_Security_Decision_Sample {

    // 1. 쿠키나 헤더 등 사용자가 변조하기 쉬운 외부 입력을 보안 결정(권한 체크)의 조건으로 사용하는 패턴
    @GetMapping("/api/secure/data")
    public String accessSecureDataUnsafe(
            @CookieValue(value = "userRole", defaultValue = "guest") String userRole,
            @RequestHeader(value = "X-User-Type", defaultValue = "normal") String userType) {
        
        // [취약점 포인트] 서버 측 세션이나 토큰 검증 없이,
        // 클라이언트가 임의로 조작할 수 있는 쿠키(userRole)나 HTTP 헤더(X-User-Type) 값을
        // 보안 권한 판단 조건식의 근거로 그대로 신뢰함 (CWE-807)
        if ("admin".equals(userRole) || "administrator".equals(userType)) {
            return "Confidential Admin Data: [OK]";
        } else {
            return "Access Denied";
        }
    }
}