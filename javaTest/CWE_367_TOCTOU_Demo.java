package javaTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CWE_367_TOCTOU_Demo {

    // 분석기 룰셋에 정확히 매칭되는 시연용 메서드 (파일 존재 확인 후 즉시 삭제)
    public void demoFileDeleteCheck(String targetPath) {
        // 1. 분석기가 추적하는 패턴인 'File 변수명 = new File(...)' 형태 준수
        File targetFile = new File(targetPath);

        // 2. 분석기가 타겟팅하는 'if (file.exists())' 조건문 사용 (부정 연산자 제외)
        if (targetFile.exists()) {
            
            // --- [경쟁 상태 발생 구간 (TOCTOU)] ---
            // 체크 시점과 사용 시점 사이의 찰나에 다른 프로세스가 파일을 조작할 수 있음
            
            // 3. 윈도우 범위(8줄 이내) 내에 위치하는 취약한 파일 연산(delete) 수행
            targetFile.delete();
        }
    }

    // 파일 쓰기 버전의 시연용 메서드
    public void demoFileWriteCheck(String targetPath, String data) {
        File dataFile = new File(targetPath);

        // 파일 존재 여부 확인 후 즉시 스트림 생성 및 쓰기 수행
        if (dataFile.exists()) {
            try (FileOutputStream fos = new FileOutputStream(dataFile)) {
                fos.write(data.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}