package javaTest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;

@RestController
public class CWE_759_Unsalted_Hash_Sample {

    // [취약점 포인트] 사용자 비밀번호를 해시화할 때 임의의 값(Salt)을 섞지 않고 
    // 평문 비밀번호 그 자체를 곧바로 단방향 해시(SHA-256 등)로 변환함 (CWE-759)
    // 공격자가 사전에 계산된 해시 값 모음(레인보우 테이블)을 이용하면 원본 비밀번호를 쉽게 역산할 수 있음
    @PostMapping("/api/user/register-password")
    public String registerPasswordUnsafe(@RequestParam("rawPassword") String rawPassword) {
        try {
            // [취약점 싱크] 솔트(Salt) 생성 및 결합 과정 없이 평문을 곧바로 다이제스트에 입력
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(rawPassword.getBytes("UTF-8"));
            
            // 바이트 배열을 Hex 문자열로 변환
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return "Password hashed without salt (Unsafe): " + hexString.toString();
        } catch (Exception e) {
            return "Hashing Error: " + e.getMessage();
        }
    }
}