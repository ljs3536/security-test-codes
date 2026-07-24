package javaTest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

@RestController
public class CWE_327_Weak_Crypto_Algorithm_Sample {

    // 1. 취약한 해시 알고리즘(MD5, SHA-1)을 비밀번호 암호화나 데이터 무결성 검증에 사용하는 패턴 (CWE-327)
    @PostMapping("/api/crypto/hash")
    public String hashDataUnsafe(@RequestParam("plainText") String plainText) {
        try {
            // [취약점 포인트] 이미 보안상 취약성이 검증되어 레거시로 분류된 MD5 또는 SHA-1 알고리즘을 사용함
            // 충돌(Collision) 공격에 취약하며, 무작위 대입(Brute-force)이나 레인보우 테이블을 통해 쉽게 복호화될 수 있음
            MessageDigest md = MessageDigest.getInstance("MD5"); // 혹은 "SHA-1"
            byte[] hashBytes = md.digest(plainText.getBytes());
            
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return "Weak Hash Result (MD5): " + sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return "Error: Algorithm not found";
        }
    }

    // 2. 취약하거나 안전하지 않은 대칭키 암호화 알고리즘(DES, RC4 등)을 사용하는 패턴 (CWE-327)
    @PostMapping("/api/crypto/encrypt")
    public String encryptDataUnsafe(@RequestParam("secretText") String secretText) {
        try {
            // [취약점 포인트] 키 길이가 짧고 암호학적으로 안전하지 않은 DES 알고리즘을 사용하여 기밀 정보를 암호화함
            // 현대의 컴퓨팅 성능으로 매우 짧은 시간 안에 키가 크래킹될 위험이 있음
            SecretKeySpec secretKey = new SecretKeySpec("12345678".getBytes(), "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding"); // 취약한 대칭키 및 운용 모드
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            
            byte[] encryptedBytes = cipher.doFinal(secretText.getBytes());
            return "Weak Encryption Result (DES): [Encrypted Data Generated]";
        } catch (Exception e) {
            return "Encryption Error: " + e.getMessage();
        }
    }
}