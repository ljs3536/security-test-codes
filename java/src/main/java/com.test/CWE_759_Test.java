import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CWE_759_Test {
    public void hashPassword(String password) throws NoSuchAlgorithmException {
        // [CWE-327 회피 테스트]
        // SHA-256 대신 비교적 안전한(혹은 분석기가 취약하다고 판단하지 않는) 알고리즘을 사용
        // 이렇게 하면 엔진은 327을 띄우지 않고, 그다음 순위인 759 탐지 로직을 돌릴 것입니다.
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] hashed = md.digest(password.getBytes());
    }
}