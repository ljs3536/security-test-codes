package javaTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CWE_404_Resource_Leak_Sample {

    // 1. 파일 스트림을 열고 finally 블록이나 close 처리가 누락된 전형적인 자원 누수 (CWE-404)
    public void readFileUnsafe(String filePath) {
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            
            // 파일 읽기 로직 처리...
            int data = fis.read();
            System.out.println("Data: " + data);
            
            // [취약점 포인트] 정상 수행되든 예외가 발생하든 fis.close()를 호출해야 하지만,
            // 중간에 로직이 끝나거나 예외 발생 시 스트림 객체가 운영체제/JVM에 반환되지 않고 누수됨
            fis.close(); 
            
        } catch (IOException e) {
            // 예외 발생 시 fis.close()가 실행되지 않고 곧바로 catch로 빠져버림
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    // 2. 다중 자원(파일 및 DB 등)을 열었을 때 개별 해제 실패 패턴
    public void processMultipleResourcesUnsafe(String path1, String path2) {
        FileInputStream fis1 = null;
        FileInputStream fis2 = null;
        try {
            fis1 = new FileInputStream(path1);
            fis2 = new FileInputStream(path2);
            
            // 처리 로직...
            
        } catch (IOException e) {
            // [취약점 포인트] 에러를 잡았으나 fis1과 fis2에 대한 안전한 자원 해제(null 체크 및 close)가 
            // 누락되어 자원이 그대로 점유된 채 방치됨
        }
    }
}