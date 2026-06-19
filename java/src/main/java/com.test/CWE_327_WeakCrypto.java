import java.security.MessageDigest;
import javax.crypto.Cipher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_327_WeakCrypto {

    // 분석기 테스트 포인트:
    // 엔진이 "MD5"나 "DES"라는 문자열 상수가 암호화 관련 클래스에 주입되는 것을 잡는지 확인
    @PostMapping("/encrypt")
    public String encryptData(@RequestParam("data") String data) {
        try {
            // 취약점 1: 해시 충돌 공격에 취약한 MD5 해시 함수 사용 (VULNERABLE)
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] hash = md5.digest(data.getBytes());

            // 취약점 2: 키 길이가 짧아 브루트포스 공격에 취약한 DES 알고리즘 사용 (VULNERABLE)
            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            // ... 초기화 및 암호화 로직 (생략) ...

            return "Data processed securely... (Actually not)";
        } catch (Exception e) {
            return "Encryption error";
        }
    }
}