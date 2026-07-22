package javaTest;

import java.io.FileInputStream;
import java.io.IOException;

public class CWE_416_Use_After_Free_Sample {

    // 1. 이미 close()되어 해제된 파일 스트림을 다시 읽으려고 시도하는 패턴 (CWE-416)
    public void processClosedStreamUnsafe(String filePath) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            
            // 첫 번째 정상 작업 수행
            int firstData = fis.read();
            System.out.println("First read: " + firstData);
            
            // 자원을 의도적으로 혹은 실수로 중간에 닫음 (Release)
            fis.close();
            
            // [취약점 포인트] 이미 닫혀서(해제되어) 사용할 수 없는 스트림 객체임에도 불구하고,
            // 이후 로직에서 이를 인지하지 못하고 다시 메서드를 호출하여 자원 재참조 시도
            int secondData = fis.read(); // 여기서 IOException 또는 비정상 동작 발생
            System.out.println("Second read: " + secondData);
            
        } catch (IOException e) {
            System.err.println("Stream error: " + e.getMessage());
        }
    }

    // 2. 객체 상태를 무효화(Invalidate/Clear)한 뒤 재사용하는 비즈니스 로직 패턴
    public static class SessionContext {
        private boolean active = true;
        public void invalidate() { this.active = false; }
        public boolean isActive() { return active; }
        public void executeCommand() { System.out.println("Executing command..."); }
    }

    public void processSessionUnsafe() {
        SessionContext session = new SessionContext();
        
        // 세션 무효화 (자원 및 상태 해제)
        session.invalidate();
        
        // [취약점 포인트] 이미 해제된(무효화된) 세션 객체임을 검증(Active Check)하지 않고 
        // 곧바로 기능을 수행하여 논리적 결함 유발
        if (!session.isActive()) {
            // 방어 로직이 있어야 하나, 아래처럼 곧바로 재사용하는 경우
        }
        session.executeCommand(); 
    }
}