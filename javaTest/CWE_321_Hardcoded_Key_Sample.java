package javaTest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@RestController
public class CWE_321_Hardcoded_Key_Sample {

    // [취약점 포인트] 소스코드 내부에 대칭키 암호화에 사용되는 비밀키(Secret Key) 문자열을 
    // 상수로 직접 하드코딩함 (CWE-321)
    // 소스코드가 형상 관리 시스템이나 빌드 아티팩트를 통해 노출될 경우, 
    // 공격자가 이 키를 이용해 암호화된 모든 데이터를 쉽게 복호화할 수 있음
    private static final String ENCRYPTION_KEY = "MySuperSecretKey!"; // 하드코딩된 암호화 키

    @PostMapping("/api/crypto/encrypt-data")
    public String encryptDataUnsafe(@RequestParam("plainText") String plainText) {
        try {
            // 하드코딩된 비밀키를 바이트 배열로 변환하여 시크릿 키 규격 생성
            byte[] keyBytes = ENCRYPTION_KEY.getBytes("UTF-8");
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
            
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
            return "Encrypted Data: " + Base64.getEncoder().encodeToString(encryptedBytes);
            
        } catch (Exception e) {
            return "Encryption Error: " + e.getMessage();
        }
    }
}