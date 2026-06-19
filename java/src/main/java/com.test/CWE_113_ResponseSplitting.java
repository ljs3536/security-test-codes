import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_113_ResponseSplitting {

    // 분석기 테스트 포인트:
    // Source: request.getParameter("authorName")
    // Sink: response.setHeader() 또는 response.addHeader()
    @GetMapping("/set-author")
    public void setAuthorHeader(HttpServletRequest request, HttpServletResponse response) {
        
        // 사용자 입력값 (예: "John\r\nContent-Length: 0\r\n\r\nHTTP/1.1 200 OK...")
        String author = request.getParameter("authorName");
        
        // 취약점: 개행문자(\r, \n)를 제거하거나 치환하는 로직 없이 그대로 헤더에 삽입 (VULNERABLE)
        response.setHeader("X-Author-Name", author);
        
        // 추가 테스트 Sink (쿠키에 넣을 때도 동일하게 발생함)
        // Cookie cookie = new Cookie("author", author);
        // response.addCookie(cookie);
    }
}