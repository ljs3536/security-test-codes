import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_382_VulTest {

    /**
     * [CWE-382 취약점 지점]
     * 복구할 수 없는 에러가 발생했다고 해서 System.exit()를 호출하면,
     * 해당 스레드만 죽는 것이 아니라 WAS(Tomcat 등) 전체 프로세스가 종료됨.
     */
    @GetMapping("/api/process")
    public String processData(@RequestParam(value = "status", defaultValue = "ok") String status) {
        
        try {
            if ("fatal".equalsIgnoreCase(status)) {
                throw new RuntimeException("심각한 데이터 불일치 발생!");
            }
            return "데이터 처리 완료";
            
        } catch (RuntimeException e) {
            // 잘못된 에러 처리 방식 (API 오용)
            System.err.println("시스템을 복구할 수 없어 강제 종료합니다: " + e.getMessage());
            
            // 분석기가 이 라인을 잡아내야 하지만, 현재 룰셋이 없어 미탐(False Negative) 발생
            System.exit(1); 
            
            return "이 코드는 도달하지 않습니다.";
        }
    }
}