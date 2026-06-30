import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_190_IntegerOverflow {
    /**
     * [CWE-190 취약점] 정수 오버플로우
     * 수량(quantity)에 매우 큰 값을 입력하면 totalAmount가 음수로 변환됨
     */
    @PostMapping("/api/order")
    public String processOrder(@RequestParam("price") int price, @RequestParam("quantity") int quantity) {
        
        // [위험] 입력값에 대한 최대치 검증(Range Check) 없이 바로 산술 연산 수행
        int totalAmount = price * quantity;
        
        // totalAmount가 음수가 되어 결제 우회 또는 환불 악용 가능
        if (totalAmount <= 0) {
            return "오류: 비정상적인 금액이거나 무료 결제 처리됨. 결제액: " + totalAmount;
        }

        // 정상 결제 로직 진행...
        return "결제 완료. 총액: " + totalAmount;
    }
}
