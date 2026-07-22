package javaTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class CWE_89_SQL_Injection_Sample {

    @Autowired
    private DataSource dataSource;

    // 1. 문자열 결합(Concatenation) 방식으로 동적 쿼리를 생성하여 Statement로 실행하는 전형적인 SQL Injection (CWE-89)
    public String searchUserUnsafe(String userId) {
        String resultName = "";
        
        // [취약점 포인트] PreparedStatement의 파라미터 바인딩(?)을 사용하지 않고, 
        // 외부 입력값(userId)을 문자열 연산으로 쿼리에 곧바로 이어 붙임
        // 공격자가 'admin' OR '1'='1 같은 페이로드를 주입하면 인증 우회나 정보 탈취 발생
        String query = "SELECT username FROM users WHERE user_id = '" + userId + "'";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                resultName = rs.getString("username");
            }
        } catch (SQLException e) {
            System.err.println("Database query error: " + e.getMessage());
        }

        return resultName;
    }
}