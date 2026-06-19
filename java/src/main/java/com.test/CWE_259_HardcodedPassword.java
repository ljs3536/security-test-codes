import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_259_HardcodedPassword {

    // 분석기 테스트 포인트:
    // Source: "SuperSecretPassword123!" 라는 하드코딩된 리터럴 문자열이
    // Sink: DriverManager.getConnection() 의 비밀번호 파라미터로 흘러가는지 추적
    @GetMapping("/db-connect")
    public String connectToDatabase() {
        
        String dbUrl = "jdbc:mysql://localhost:3306/mydb";
        String dbUser = "admin";
        
        // 취약점: 소스코드에 데이터베이스 비밀번호가 평문으로 하드코딩되어 있음 (VULNERABLE)
        String dbPassword = "SuperSecretPassword123!";

        try {
            // 하드코딩된 비밀번호 변수가 중요 인증 Sink로 전달됨
            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            return "Database connected successfully";
        } catch (SQLException e) {
            return "Database connection failed";
        }
    }
}