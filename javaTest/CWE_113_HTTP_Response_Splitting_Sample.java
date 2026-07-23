package javaTest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class CWE_113_HTTP_Response_Splitting_Sample {

    // 1. 외부 입력값을 검증 없이 응답 헤더에 직접 설정하는 취약한 패턴 (CWE-113)
    @GetMapping("/api/set-header")
    public String setHeaderUnsafe(@RequestParam("lang") String lang, HttpServletResponse response) {
        
        // [취약점 포인트] 사용자가 입력한 lang 값에 CRLF 인젝션 문자열(\r\n)이 포함되어 있을 경우
        // 응답 헤더에 강제로 새로운 헤더나 본문을 주입할 수 있음 (HTTP Response Splitting)
        // 공격자가 헤더 조작을 통해 XSS(Cross-Site Scripting)나 캐시 오염 등을 유발할 수 있음
        response.setHeader("Content-Language", lang);

        return "Header set with language: " + lang;
    }
}