package javaTest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class CWE_601_Open_Redirect_Sample {

    // 1. HttpServletResponse의 sendRedirect를 이용한 전형적인 Open Redirect (CWE-601)
    @GetMapping("/api/login")
    public void loginRedirectUnsafe(@RequestParam("targetUrl") String targetUrl, HttpServletResponse response) throws IOException {
        
        // [취약점 포인트] 사용자가 로그인 성공 후 이동할 경로로 지정한 targetUrl이 
        // 외부 도메인인지(예: http://malicious.com) 검증하는 화이트리스트 로직이나 도메인 체크 없이
        // 곧바로 response.sendRedirect()에 전달됨
        // 공격자가 피싱 사이트로 유도하는 파라미터를 심어 사용자를 속일 수 있음
        response.sendRedirect(targetUrl);
    }
}