package javaTest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@RestController
public class CWE_312_Cleartext_Storage_Sample {

    // 1. 사용자의 비밀번호나 민감 정보를 암호화 없이 평문으로 데이터베이스에 저장하는 취약한 패턴 (CWE-312)
    @PostMapping("/api/user/register")
    public String registerUserUnsafe(
            @RequestParam("username") String username, 
            @RequestParam("password") String password,
            @RequestParam("phone") String phone) {
        
        try {
            // [취약점 포인트] 사용자의 핵심 인증 정보인 비밀번호(password)나 
            // 민감한 개인정보(phone)를 아무런 단방향 해시(BCrypt 등)나 암호화 과정 없이
            // 그대로(Plaintext) SQL 쿼리에 담아 데이터베이스에 저장함
            // DB가 탈취되거나 내부 사용자에 의해 노출될 경우 치명적인 보안 사고로 이어짐
            
            String dbUrl = "jdbc:h2:mem:testdb";
            Connection conn = DriverManager.getConnection(dbUrl, "sa", "");
            
            String query = "INSERT INTO users (username, password, phone) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password); // 평문 비밀번호 바인딩 (CWE-312)
            pstmt.setString(3, phone);     // 평문 개인정보 바인딩 (CWE-312)
            
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();

            return "User registered successfully with cleartext storage (Unsafe).";
        } catch (Exception e) {
            return "Registration Error: " + e.getMessage();
        }
    }
}