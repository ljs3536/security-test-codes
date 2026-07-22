package javaTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CWE_22_Path_Traversal_Sample {

    private static final String BASE_DIR = "/var/app/uploads/";

    // 1. 외부 입력 파일명을 기본 경로와 단순 문자열 결합하여 파일 객체를 생성하는 전형적인 Path Traversal (CWE-22)
    public FileInputStream downloadFileUnsafe(String fileName) throws FileNotFoundException {
        
        // [취약점 포인트] 외부에서 전달된 fileName(예: "../../etc/passwd")이 
        // 입력값 검증 없이 BASE_DIR과 문자열로 곧바로 결합됨
        // 상위 디렉토리로 이동하는 경로 순회 문자가 필터링되지 않아 시스템 중요 파일에 접근 가능
        File targetFile = new File(BASE_DIR + fileName);

        return new FileInputStream(targetFile);
    }
}