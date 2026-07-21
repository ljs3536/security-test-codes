package javaTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CWE_390_Error_Without_Action {

    // 1. 예외가 발생했음에도 아무런 조치(로그, 예외 재전파 등)를 하지 않는 전형적인 빈 catch 블록 (CWE-390)
    public void processFileSilently(String filePath) {
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            // 파일 처리 로직...
            fis.close();
        } catch (IOException e) {
            // [취약점 포인트] 예외 객체를 잡았으나 내부가 완전히 비어있거나 
            // 단순히 주석만 남겨두어 에러 상황을 완전히 무시함 (Detection Without Action)
        }
    }

    // 2. 반환값이나 상태 코드 에러를 감지하고도 후속 대응을 누락한 경우
    public boolean validateAndExecute(int status) {
        if (status < 0) {
            // [취약점 포인트] 에러 상태임을 인지(if문 진입)했으나, 
            // 에러 로그나 예외 던지기(throw), 리턴 값 처리 등 적절한 조치 없이 그대로 넘어감
        }
        
        // 정상 비즈니스 로직 진행
        return true;
    }
}