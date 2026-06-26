import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class CWE_79_Xss_TestController {
    /**
     * [CWE-79 취약점] 검증되지 않은 외부 입력이 직접 화면에 출력됨
     * Taint Source: @RequestParam("name")
     * Taint Sink: response.getWriter().print()
     */
    @GetMapping("/api/greet")
    public void greetUser(@RequestParam("name") String name, HttpServletResponse response) throws IOException {
        
        // 브라우저가 응답을 HTML로 해석하도록 유도
        response.setContentType("text/html;charset=UTF-8");
        
        // [위험] Taint된 변수 'name'이 HTML 치환(Escape) 로직 없이 직접 Sink로 흘러감
        // 분석기가 "Unvalidated Input in HTTP Response" 등으로 잡아내야 함
        response.getWriter().print("<h1>환영합니다, " + name + "님!</h1>");
    }
}
