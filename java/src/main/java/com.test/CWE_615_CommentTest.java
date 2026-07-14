public class CWE_615_CommentTest {
    public void setup() {
        // [CWE-615 테스트] 정규식 패턴에 정확히 부합하는 주석
        // password = "super_secret_password_12345678"
        String dbPass = "hidden";
        
        // [테스트] api_key와 할당 기호가 붙어있는 경우
        // api_key: AI_KEY_987654321
        String apiKey = "hidden";
    }
}