package javaTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class CWE_367_Bypass_Demo {

    // 1. 우회 포인트: 'if (!file.exists())' 형태 (부정 연산자 사용으로 정규식 매칭 실패)
    // 2. 우회 포인트: 생성자(new File)와 선언부 분리 또는 파라미터로 객체를 받아옴
    public void bypassTestWithNegativeCheck(File targetFile, String content) throws IOException {
        
        // 분석기 룰은 'if (file.exists())'만 찾기 때문에 이 라인은 아예 무시됨
        if (!targetFile.exists()) {
            
            // 주석이나 약간의 로직 코드가 사이에 끼어 8줄 윈도우 범위를 벗어남
            System.out.println("Target file does not exist. Preparing to create new one...");
            System.out.println("Checking system permissions...");
            System.out.println("Allocating buffer memory...");
            
            // 실제 파일 쓰기(Use)가 일어나지만 분석기는 이미 놓쳤음
            try (FileOutputStream fos = new FileOutputStream(targetFile)) {
                fos.write(content.getBytes());
            }
        }
    }
}