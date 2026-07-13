import java.io.OutputStream;
import java.net.Socket;

public class CWE_319_CleartextTransmission {

    // [CWE-319 취약점] 보안 처리가 없는 소켓 통신을 통해 민감 정보 전송
    public void sendSensitiveData(String host, int port, String creditCardNumber) throws Exception {
        // SSL/TLS 소켓이 아닌 일반 소켓(Socket) 사용
        Socket socket = new Socket(host, port);
        
        OutputStream os = socket.getOutputStream();
        
        // [위험] 신용카드 정보를 평문으로 네트워크에 전송
        // 분석기가 'creditCardNumber' 변수가 네트워크 출력 스트림으로 
        // 암호화 과정 없이 나가는 것을 감지해야 함
        os.write(creditCardNumber.getBytes());
        
        socket.close();
    }
}