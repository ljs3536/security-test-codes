import java.util.Random; // [탐지 포인트] java.util.Random 클래스 import

public class CWE_330_InsecureRandom {

    public String generateSessionId() {
        // [취약점] 예측 가능한 Random 클래스 사용
        // 보안 심사에서는 반드시 java.security.SecureRandom 사용을 권장함
        Random random = new Random(); 
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}