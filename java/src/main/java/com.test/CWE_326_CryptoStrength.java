import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class CWE_326_CryptoStrength {

    public void generateWeakKey() throws NoSuchAlgorithmException {
        // RSA 알고리즘 자체는 안전하지만...
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        
        // [취약점] 키 길이가 1024비트로 너무 짧아 암호화 강도가 부적절함 (최소 2048비트 권장)
        // 분석기가 initialize() 메서드의 인자값 '1024'를 검사하는지 확인
        keyGen.initialize(1024); 
        
        System.out.println("약한 강도의 키가 생성되었습니다.");
    }
}