import java.util.Random;

public class CWE_330_UsageTest {

    public boolean isAdmin(String userInput) {
        // [CWE-330 트리거 테스트]
        // 난수 생성기 자체가 아니라, 
        // 생성된 난수 값을 보안 판정(인가)의 인자로 사용하는 케이스
        Random random = new Random();
        int randomValue = random.nextInt(100); 
        
        // 예측 가능한 난수 값을 보안 결정(예: 특정 번호만 관리자로 인정)에 사용
        // 이 경우 엔진은 '난수 생성기 취약점(338)'보다는 
        // '부적절한 난수 값에 의존한 보안 결정(330)'으로 분류할 가능성이 큼
        if (randomValue == 42) {
            return true; // 관리자 권한 획득
        }
        return false;
    }
}