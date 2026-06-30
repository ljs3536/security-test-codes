import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_134_unsafeStringformat {
    /**
     * [CWE-134 취약점] 형식 문자열 취약점
     */
    @GetMapping("/api/log")
    public String logEvent(@RequestParam("userInput") String userInput) {
        
        // [위험] 사용자가 입력한 문자열이 포맷 함수의 첫 번째 인자(format string)로 직접 들어감
        // 공격자가 userInput에 "%x %x %n" 등을 입력하면 내부 메모리 노출이나 크래시 유발 가능
        // 안전한 방법: String.format("User Input: %s", userInput)
        
        String formattedMessage = String.format(userInput);
        
        System.out.println("System Log: " + formattedMessage);
        
        return "로그가 기록되었습니다.";
    }
}
