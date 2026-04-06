import java.io.IOException;
import org.springframework.web.bind.annotation.*;

@RestController
public class NetworkController {

    @GetMapping("/ping")
    public String pingHost(@RequestParam String host) throws IOException {
        // 리스트 형태로 전달하지만, 쉘 커맨드(-c) 내부에 입력값이 포함됨
        // 공격자가 "127.0.0.1 && cat /etc/passwd" 등을 넣을 수 있음
        ProcessBuilder pb = new ProcessBuilder("sh", "-c", "ping -c 3 " + host);
        
        Process process = pb.start();
        return "Ping request sent to " + host;
    }
}