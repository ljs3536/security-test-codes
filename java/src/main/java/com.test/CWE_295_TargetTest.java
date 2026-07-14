import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;

public class CWE_295_TargetTest {

    public X509TrustManager getTargetTrustManager() {
        return new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] certs, String authType) {}

            // [분석기 타겟팅] 현재 분석기의 정규식 로직에 걸리도록 한 줄에 정의
            public void checkServerTrusted(X509Certificate[] certs, String authType) {} 

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }
}