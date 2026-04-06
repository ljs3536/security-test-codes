import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class InternalServiceScanner {
    public String getServiceStatus(String host) throws Exception {
        // 🔍 테스트 포인트: 호스트 주소를 외부에서 입력받음
        String internalUrl = "http://" + host + ":8080/health";
        URL url = new URL(internalUrl);
        URLConnection conn = url.openConnection();
        
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        return in.readLine();
    }
}