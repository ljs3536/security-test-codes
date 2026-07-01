import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CWE_759_NoSaltTest {

    public byte[] hashPassword(String password) throws NoSuchAlgorithmException {
        // 알고리즘 자체는 안전함 (CWE-327 아님)
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        
        // [CWE-759 취약점] Salt(무작위 난수)를 추가하는 과정(md.update(salt)) 없이 
        // 비밀번호만 곧바로 해싱함
        return md.digest(password.getBytes());
    }
}