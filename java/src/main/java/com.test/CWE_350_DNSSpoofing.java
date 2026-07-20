import java.net.InetAddress;

public class CWE_350_DNSSpoofing {
    public boolean validateClientAccess(String clientIpAddress) {
        try {
            // 1. IP 주소를 기반으로 역방향 DNS 조회 수행 (Reverse DNS Lookup)
            InetAddress inetAddr = InetAddress.getByName(clientIpAddress);
            
            // 2. 호스트 이름을 가져옴 (취약점 핵심 포인트)
            String hostName = inetAddr.getHostName(); 
            
            // 3. DNS 조회 결과를 바탕으로 중요한 보안 결정(권한 부여)을 내림
            if (hostName.endsWith(".trusted-domain.com")) {
                grantAdminPrivileges();
                return true;
            }
        } catch (UnknownHostException e) {
            log.error("DNS lookup failed", e);
        }
        
        return false;
    }

    private void grantAdminPrivileges() {
        // 관리자 권한 부여 로직
    }
}
