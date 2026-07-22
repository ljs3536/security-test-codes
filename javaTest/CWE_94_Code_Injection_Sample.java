package javaTest;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class CWE_94_Code_Injection_Sample {

    // 1. 외부에서 전달받은 스크립트 문자열을 자바 ScriptEngine을 통해 그대로 실행하는 전형적인 코드 삽입 (CWE-94)
    public Object evaluateDynamicScriptUnsafe(String userScriptInput) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript"); // 또는 Nashorn, Rhino 등
        
        try {
            // [취약점 포인트] 사용자 입력값(userScriptInput)에 악성 자바스크립트 코드 
            // (예: Java 객체를 생성하여 시스템 명령어를 실행하는 스크립트 등)가 포함되어 있을 경우,
            // 별도의 검증 없이 곧바로 eval()을 통해 실행되어 서버 권한이 탈취될 수 있음
            return engine.eval(userScriptInput);
            
        } catch (ScriptException e) {
            System.err.println("Script execution error: " + e.getMessage());
            return null;
        }
    }
}