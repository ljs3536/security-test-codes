package javaTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CWE_367_TOCTOU_Sample {

    public void processConfigFile(String filePath) {
        File file = new File(filePath);

        // 1. Check (검사 시점) : 파일이 존재하는지, 읽을 수 있는지 권한/존재 여부 확인
        if (file.exists() && file.canRead()) {
            
            // [취약점 노출 구간] 
            // if문 검사 직후, 실제 파일을 열어 읽기 직전의 미세한 시간 차이(TOCTOU) 동안 
            // 공격자가 해당 파일을 악성 심볼릭 링크나 다른 파일로 바꿔치기할 수 있음.
            try {
                Thread.sleep(100); // 레이스 컨디션 유도를 위한 의도적 대기 (개념 증명용)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // 2. Use (사용 시점) : 검사 결과를 신뢰하고 파일을 실제로 읽거나 조작
            try (java.util.Scanner scanner = new java.util.Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    // 설정 파일 데이터 처리 로직
                    System.out.println("Processing line: " + line);
                }
            } catch (IOException e) {
                System.err.println("Failed to read file: " + e.getMessage());
            }
        } else {
            System.out.println("File does not exist or cannot be read.");
        }
    }
}