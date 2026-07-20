import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.ServerSocket;
public class CWE_246_DirectSocket {
    // 1. 클라이언트 소켓을 직접 생성하여 외부 서버와 통신하는 경우 (취약점 유발)
    public void sendDataViaDirectSocket(String host, int port, String message) {
        Socket socket = null;
        try {
            // [취약점 탐지 지점] WAS의 커넥션 관리나 안전한 프레임워크 통신을 거치지 않고 직접 소켓 생성
            socket = new Socket(host, port);
            
            OutputStream out = socket.getOutputStream();
            out.write(message.getBytes());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) socket.close();
            } catch (Exception ignored) {}
        }
    }

    // 2. 서버 소켓을 직접 열어 클라이언트의 연결을 대기하는 경우 (취약점 유발)
    public void startDirectServer(int port) {
        ServerSocket serverSocket = null;
        try {
            // [취약점 탐지 지점] 애플리케이션 내부에 직접 ServerSocket을 바인딩하여 리소스를 점유
            serverSocket = new ServerSocket(port);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                // 클라이언트 처리 로직...
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
