package javaTest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_134_Format_String_Sample {

    // 1. 외부 입력값을 String.format 등의 포맷 문자열 자리에 직접 사용하는 취약한 패턴 (CWE-134)
    @GetMapping("/api/format/log")
    public String formatStringUnsafe(@RequestParam("userInput") String userInput) {
        
        // [취약점 포인트] 포맷 지정자(%s, %d 등)를 포함해야 하는 자리에 
        // 사용자가 입력한 문자열(userInput)이 포맷 스트링 템플릿으로 직접 전달됨
        // 공격자가 형식을 지정하는 특수문자나 인자 조작 패턴을 입력할 경우 예외 발생, 
        // 포맷 파싱 오류, 혹은 민감한 내부 시스템 정보 노출을 유발할 수 있음
        String formattedMessage = String.format(userInput, "Default Value");

        return "Formatted Result: " + formattedMessage;
    }
}