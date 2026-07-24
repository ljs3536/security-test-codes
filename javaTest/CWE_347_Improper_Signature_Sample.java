package javaTest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Signature;
import java.security.PublicKey;

@RestController
public class CWE_347_Improper_Signature_Sample {

    // [취약점 포인트] 디지털 서명이나 토큰의 암호학적 유효성 검증(Signature Verification)을 
    // 누락하거나, 검증 단계에서 발생한 에러를 무시하여 위조된 데이터가 정상 처리되도록 허용함 (CWE-347)
    // 공격자가 임의로 변조한 데이터와 서명 값을 주입해도 시스템이 이를 걸러내지 못해 보안이 무력화됨
    @PostMapping("/api/auth/verify-signature")
    public String verifySignatureUnsafe(
            @RequestParam("data") String data,
            @RequestParam("signature") String signatureHex) {
        
        try {
            // 시뮬레이션용 공개키 및 서명 바이트 변환
            byte[] dataBytes = data.getBytes("UTF-8");
            byte[] sigBytes = hexToBytes(signatureHex);

            // [취약점 싱크] 서명 검증 객체(Signature)를 생성하고 초기화했으나, 
            // 실제 verify() 결과를 검증하는 조건문이 없거나 
            // 검증 실패 시에도 예외 처리를 누락하여 무조건 통과시킴
            Signature sig = Signature.getInstance("SHA256withRSA");
            // sig.initVerify(publicKey); // 공개키 연동 생략 또는 검증 로직 누락 시뮬레이션
            sig.update(dataBytes);
            
            boolean isVerified = false; 
            // boolean isVerified = sig.verify(sigBytes); // 실제로는 이 결과값을 검증해야 함!

            // [취약한 로직] 서명이 위조되었거나 검증에 실패(isVerified == false)했음에도 
            // 이를 무시하고 요청을 정상 승인함
            if (!isVerified) {
                // 의도적으로 검증 실패를 무시하고 처리하는 안티 패턴
                return "Signature verification bypassed (Unsafe). Data accepted.";
            }

            return "Signature verified successfully.";
            
        } catch (Exception e) {
            return "Verification Error: " + e.getMessage();
        }
    }

    private byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                                 + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }
}