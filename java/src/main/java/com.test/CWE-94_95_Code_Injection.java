import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_94_95_CodeInjection {

    // 분석기 테스트 포인트: 
    // Source: @RequestParam("expression") 으로 들어온 오염된 데이터가
    // Sink: parser.parseExpression(userInput).getValue() 로 들어가는 흐름을 잡아야 함.
    @GetMapping("/evaluate")
    public String evaluateExpression(@RequestParam("expression") String userInput) {
        
        // 사용자가 "T(java.lang.Runtime).getRuntime().exec('calc')" 같은 악의적 코드를 입력할 수 있음
        ExpressionParser parser = new SpelExpressionParser();
        
        // 사용자 입력값이 아무런 검증 없이 코드 표현식으로 파싱되고 실행됨 (VULNERABLE)
        Object result = parser.parseExpression(userInput).getValue();
        
        return "Result: " + result.toString();
    }
}