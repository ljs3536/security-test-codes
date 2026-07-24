package javaTest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class CWE_539_Persistent_Cookie_Sample {

    // [취약점 포인트] 주민번호, 사번, 평문 비밀번호나 민감한 세션 정보를 
    // 보안 속성(Secure, HttpOnly)과 암호화 처리가 누락된 영구형 쿠키에 담아 사용자 클라이언트에 저장함 (CWE-539)
    // 사용자 디바이스의 쿠키 파일이 탈취되거나 로컬 악성코드에 노출될 경우 중요 정보가 그대로 유출됨
    @GetMapping("/api/user/save-cookie")
    public String saveInsecureCookieUnsafe(
            @RequestParam("userId") String userId,
            @RequestParam("userRole") String userRole,
            HttpServletResponse response) {
        
        try {
            // [취약점 싱크] 민감한 권한 정보나 식별 데이터를 평문 문자열 그대로 쿠키 값에 대입
            String sensitiveData = userId + ":" + userRole;
            
            Cookie insecureCookie = new Cookie("userInfoCookie", sensitiveData);
            
            // [취약점 유발 요소] 
            // 1. 쿠키의 유효 기간(Max-Age)을 길게 설정하여 하드디스크에 파일 형태로 오래 남게 함 (영구 쿠키)
            insecureCookie.setMaxAge(7 * 24 * 60 * 60); // 7일 동안 보존
            insecureCookie.setPath("/");
            
            // 2. HTTPS 전송을 강제하는 Secure 속성 누락
            // 3. 자바스크립트의 document.cookie 접근을 막는 HttpOnly 속성 누락
            
            response.addCookie(insecureCookie);

            return "Insecure persistent cookie saved successfully.";
            
        } catch (Exception e) {
            return "Cookie Error: " + e.getMessage();
        }
    }
}