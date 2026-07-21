package javaTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CWE_209_Information_Exposure {

    // 1. 데이터베이스 연결 에러 메시지 직접 노출 (CWE-209)
    public String loginUserUnsafe(String username, String password) {
        try {
            // DB 연결 시도 중 예외 발생 가정
            Connection conn = DriverManager.getConnection("jdbc:mysql://internal-db.local:3306/mydb", username, password);
        } catch (SQLException e) {
            // [취약점 포인트] DB 내부 접속 정보나 상세한 SQL 에러 원문(e.getMessage())을
            // 그대로 클라이언트/사용자 응답으로 반환함
            return "Login Failed due to DB Error: " + e.getMessage();
        }
        return "Success";
    }

    // 2. 파일 시스템 경로 및 상세 스택 트레이스 노출
    public String readConfigFileUnsafe(String path) {
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            // [취약점 포인트] 서버의 절대 경로 시스템 구조가 담긴 예외 메시지를 그대로 노출
            // 예: "C:\Program Files\Apache Software Foundation\Tomcat\...\config.xml (시스템이 지정된 파일을 찾을 수 없습니다)"
            return "File Error: " + e.toString();
        }
        return "Read Success";
    }
}