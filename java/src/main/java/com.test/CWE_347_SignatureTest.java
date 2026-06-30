import java.security.Signature;

public class CWE_347_SignatureTest {

    public String processData(byte[] data, byte[] signatureBytes, Signature sig) throws Exception {
        
        // 서명 검증을 위한 초기화 및 데이터 업데이트 (여기까지는 정상)
        sig.update(data);
        
        // [CWE-347 취약점] sig.verify(signatureBytes)는 true/false를 반환하는데, 
        // 반환값을 조건문(if)으로 체크하지 않고 그냥 호출만 하거나 아예 누락한 상태로 데이터를 신뢰함.
        // 분석기가 verify()의 리턴값이 제어 흐름(if 문 등)에 사용되는지 추적해야 함.
        sig.verify(signatureBytes); 

        // 서명이 유효한지 확인하지 않은 채로 중요한 비즈니스 로직 수행
        return "데이터 처리 완료 (서명 검증 안 됨)";
    }
}