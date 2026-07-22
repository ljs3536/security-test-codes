package javaTest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class CWE_79_XSS_Sample {

    // 1. Spring @RestController 환경에서 Raw String을 직접 반환할 때 발생하는 XSS
    // 실무에서 검색 결과나 커스텀 디버그 메시지를 빠르게 브라우저로 쏴줄 때 흔히 발생
    @GetMapping("/api/search")
    public String searchUnsafe(@RequestParam("keyword") String keyword) {
        
        // [취약점 포인트] 사용자가 입력한 검색어(keyword)를 HTML 문자열에 그대로 결합하여 반환
        // Spring @RestController는 기본적으로 응답을 내려줄 때 이 문자열을 그대로 노출함
        // 공격자가 "?keyword=<script>alert(document.cookie)</script>" 를 전달하면 브라우저에서 실행됨
        return "<div><h2>검색 결과</h2><p>입력하신 검색어: " + keyword + "</p></div>";
    }

    // 2. 전통적인 Servlet 방식: HttpServletResponse 객체의 출력 스트림을 직접 건드릴 때 발생하는 XSS
    // 레거시 시스템이나 필터/인터셉터 단에서 예외 처리를 할 때 자주 등장하는 패턴
    @GetMapping("/api/error")
    public void handleError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String errorMessage = request.getParameter("msg");
        
        // Content-Type이 text/html로 설정된 상태에서
        response.setContentType("text/html; charset=UTF-8");
        
        // [취약점 포인트] 외부 입력값(msg)을 HTML 인코딩(치환) 없이 PrintWriter로 직접 전송
        response.getWriter().println("<h1>시스템 에러 발생</h1><p>상세 내용: " + errorMessage + "</p>");
    }
}