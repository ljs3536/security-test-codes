package javaTest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class CWE_330_Insecure_Random_Sample {

    // [취약점 포인트] 보안에 민감한 세션 토큰이나 인증 값을 생성할 때 
    // 암호학적으로 취약하고 예측 가능한 선형 합동 시드 기반의 java.util.Random을 사용함 (CWE-330)
    // 공격자가 이전 생성 값들을 바탕으로 다음 난수나 시드 값을 예측하여 세션 하이잭킹 등의 공격을 수행할 수 있음
    private static final Random insecureRandom = new Random();

    @GetMapping("/api/auth/generate-token")
    public String generateInsecureTokenUnsafe() {
        try {
            // [취약점 싱크] 보안용 난수가 필요한 곳에 예측 가능한 Random.nextInt() 또는 Math.random()을 호출함
            int randomTokenValue = insecureRandom.nextInt(1000000);
            String sessionToken = String.format("%06d", randomTokenValue);
            
            return "Insecure Session Token Generated: " + sessionToken;
        } catch (Exception e) {
            return "Token Generation Error: " + e.getMessage();
        }
    }
}