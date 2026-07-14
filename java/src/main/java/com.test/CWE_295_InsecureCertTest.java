import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;

public class CWE_295_InsecureCertTest {

    // [CWE-295 취약점] 모든 서버 인증서를 신뢰하도록 설정된 위험한 구현
    public X509TrustManager getInsecureTrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // 클라이언트 인증서 검증 안 함
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // [핵심 취약점] 서버 인증서 검증 로직이 비어있음
                // 분석기가 이 메서드가 호출되었을 때 아무런 검증(예: 유효기간, 발급기관 확인)을 수행하지 않음을 감지해야 함
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }
}