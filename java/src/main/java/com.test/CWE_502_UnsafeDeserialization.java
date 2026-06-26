import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Base64;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_502_UnsafeDeserialization {

    // 분석기 테스트 포인트 (AST Taint 추적 엔진 테스트):
    // Source: @RequestBody 로 들어온 사용자의 base64Data 문자열이
    // Sink: ObjectInputStream의 readObject() 메서드로 도달하는 흐름을 잡아야 함.
    @PostMapping("/deserialize")
    public String deserializeData(@RequestBody String base64Data) {
        try {
            // 1. 공격자가 보낸 Base64 인코딩된 직렬화 데이터를 디코딩 (사용자 입력값)
            byte[] data = Base64.getDecoder().decode(base64Data);

            // 2. 바이트 배열을 입력 스트림으로 변환
            ByteArrayInputStream bais = new ByteArrayInputStream(data);

            // 취약점: 신뢰할 수 없는 데이터를 검증(Look-ahead) 없이 바로 역직렬화 함 (VULNERABLE)
            ObjectInputStream ois = new ObjectInputStream(bais);
            
            // 이 readObject()가 실행되는 순간, 악성 객체가 메모리에 올라가며 해킹(RCE)이 발생합니다.
            Object obj = ois.readObject(); 
            
            ois.close();

            return "Object deserialized successfully: " + obj.getClass().getName();
        } catch (Exception e) {
            // (참고: 분석기가 여기서 또 CWE-778 로깅 누락을 잡아낼 확률이 높습니다!)
            return "Deserialization failed";
        }
    }
}