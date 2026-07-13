import java.io.FileWriter;
import java.io.IOException;

public class CWE_312_CleartextStorage {

    // [CWE-312 취약점] 중요 정보를 암호화 없이 파일에 직접 기록
    public void saveUserCredential(String username, String password) throws IOException {
        // 민감한 데이터인 비밀번호를 평문으로 저장
        FileWriter writer = new FileWriter("user_db.txt", true);
        
        // 분석기가 'password'라는 변수가 파일 시스템(FileWriter, FileOutputStream 등)의 
        // Sink로 직접 흘러들어가는지 Taint 분석으로 감지해야 함
        writer.write("User: " + username + ", Password: " + password + "\n");
        writer.close();
    }
}