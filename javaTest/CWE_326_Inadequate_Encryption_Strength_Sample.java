package javaTest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

@RestController
public class CWE_326_Inadequate_Encryption_Strength_Sample {

    // [취약점 포인트] RSA 비대칭키 쌍을 생성할 때 키 길이를 1024비트(또는 그 이하)로 낮게 설정함 (CWE-326)
    // 현대의 컴퓨팅 및 크래킹 성능 기준으로 1024비트 이하의 키는 무작위 대입이나 소인수분해 공격에 쉽게 뚫릴 위험이 있음
    @PostMapping("/api/crypto/generate-keypair")
    public String generateWeakKeyPairUnsafe(@RequestParam("keySize") int keySize) {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            
            // [취약점 싱크] 보안 표준 권장치인 2048비트 이상을 만족하지 못하고 
            // 취약한 키 길이(예: 1024 이하)가 동적이든 정적이든 전달되어 생성됨
            int unsafeKeySize = (keySize < 2048) ? 1024 : keySize; 
            keyPairGen.initialize(unsafeKeySize);
            
            KeyPair keyPair = keyPairGen.generateKeyPair();
            String publicKeyStr = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            
            return "Weak RSA KeyPair Generated with size: " + unsafeKeySize + " bits.";
        } catch (Exception e) {
            return "Key Generation Error: " + e.getMessage();
        }
    }
}