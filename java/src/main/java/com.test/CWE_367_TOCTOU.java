import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;

public class CWE_367_TOCTOU {
    /**
     * 정적 분석 도구가 CWE-367을 탐지하게 만드는 취약한 메서드
     * 1. 파일 존재 여부 확인 (Check)
     * 2. 파일 쓰기/삭제 수행 (Use)
     * 이 두 단계 사이에 경쟁 상태(Race Condition)가 발생할 수 있음
     */
    public void writeToFileSafely(String filePath, String content) throws IOException {
        File file = new File(filePath);

        // [취약점 지점] 
        // 1. 체크 (Check): 파일이 없는 것을 확인
        if (!file.exists()) {
            
            // --- 공격자가 이 사이 찰나에 심볼릭 링크나 동일 파일명으로 파일을 생성할 수 있음 ---
            
            // 2. 사용 (Use): 파일이 없다고 가정하고 쓰기 작업 수행
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(content.getBytes());
            }
        }
    }

    public void deleteFileSafely(String filePath) {
        File file = new File(filePath);

        // [취약점 지점]
        // 1. 체크 (Check)
        if (file.exists()) {
            
            // --- 경쟁 상태 발생 구간 ---
            
            // 2. 사용 (Use)
            file.delete();
        }
    }
}
