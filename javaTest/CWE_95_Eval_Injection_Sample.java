package javaTest;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class CWE_95_Eval_Injection_Sample {

    // 1. 자바 ScriptEngine을 이용한 동적 Eval 인젝션 (CWE-95)
    public Object evaluateScriptUnsafe(String userExpression) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        
        try {
            // [취약점 포인트] 외부에서 전달된 문자열(userExpression)을 그대로 eval()에 전달
            // 공격자가 자바 리플렉션이나 런타임 객체 실행 코드를 주입할 경우 그대로 평가되어 실행됨
            return engine.eval(userExpression);
            
        } catch (ScriptException e) {
            System.err.println("Eval error: " + e.getMessage());
            return null;
        }
    }

    // 2. 스프링 SpEL(Expression Language)을 이용한 고위험 인젝션 (CWE-95 / 유사 변형)
    public Object evaluateSpelUnsafe(String userSpelInput) {
        ExpressionParser parser = new SpelExpressionParser();
        
        // [취약점 포인트] SpEL 파서가 사용자 입력값(userSpelInput)을 검증 없이 그대로 파싱 및 평가
        // 예: "T(java.lang.Runtime).getRuntime().exec('calc')" 같은 페이로드 유입 시 시스템 명령어 실행됨
        return parser.parseExpression(userSpelInput).getValue();
    }
}