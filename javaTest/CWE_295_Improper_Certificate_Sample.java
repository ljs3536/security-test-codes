package javaTest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.*;
import java.net.URL;
import java.security.cert.X509Certificate;

@RestController
public class CWE_295_Improper_Certificate_Sample {

    // [취약점 포인트] SSL/TLS 통신 시 서버 인증서의 유효성 검증을 무력화하는 
    // 커스텀 TrustManager 및 HostnameVerifier를 구현하여 적용함 (CWE-295)
    // 공격자가 네트워크 트래픽을 가로채어 위조된 인증서로 중간자 공격(MitM)을 수행해도 탐지하지 못함
    @GetMapping("/api/external/call")
    public String callExternalApiUnsafe(@RequestParam("targetUrl") String targetUrl) {
        try {
            // [취약점 싱크] 모든 인증서를 검증 없이 무조건 신뢰하는 빈(Empty) TrustManager 정의
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {} // 검증 로직 누락
                }
            };

            // SSLContext에 취약한 TrustManager 주입
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // [취약점 싱크] 호스트 이름 검증을 무조건 허용(true)하도록 오버라이드
            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            // 취약한 설정이 적용된 상태로 HTTPS 통신 수행
            URL url = new URL(targetUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            
            int responseCode = conn.getResponseCode();
            return "External API called with insecure certificate validation. Response Code: " + responseCode;
            
        } catch (Exception e) {
            return "Certificate Error: " + e.getMessage();
        }
    }
}