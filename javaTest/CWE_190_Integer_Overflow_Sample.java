package javaTest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_190_Integer_Overflow_Sample {

    // 1. 외부 입력값을 검증 없이 산술 연산에 사용하여 오버플로우를 유발하는 취약한 패턴 (CWE-190)
    @GetMapping("/api/calculate/total")
    public String calculateTotalUnsafe(@RequestParam("quantity") int quantity, @RequestParam("price") int price) {
        
        // [취약점 포인트] 사용자가 입력한 quantity와 price의 곱셈 연산 시 
        // 정수형(int) 최대 표현 범위를 초과(Overflow)할 경우 값이 음수나 엉뚱한 숫자로 변환됨
        // 이로 인해 결제 금액이 0원이 되거나 비즈니스 로직 검증이 우회될 수 있음
        int totalAmount = quantity * price;

        return "Total Calculated Amount: " + totalAmount;
    }
}