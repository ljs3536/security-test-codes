public class CWE_476_NullPointerTest {

    // [CWE-476 취약점] 객체가 null일 수 있음에도 별도의 체크 없이 호출함
    public void processUser(String username) {
        // 만약 username이 null이면 아래 코드에서 즉시 NullPointerException 발생
        int length = username.length(); 
        
        System.out.println("User name length: " + length);
    }

    // [참고] 안전한 코드 예시 (비교용)
    public void processUserSafe(String username) {
        if (username != null) {
            int length = username.length();
            System.out.println("User name length: " + length);
        }
    }
}