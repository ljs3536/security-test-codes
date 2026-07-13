import java.util.regex.Pattern;

public class CWE_521_TargetTest {

    // 분석기가 이 메서드의 파라미터가 'password'임을 인지하고,
    // 정규식 체크 로직(Pattern.matches)이 내부에서 호출되는지 검사하게 만듭니다.
    public void updatePassword(String password) {
        
        // [취약점] 
        // 1. 단순 길이 체크만 함 (복잡도 검증 부재)
        // 2. 이 지점에서 'password' 파라미터가 사용되지만,
        //    보안 필수 로직인 정규식 패턴 검사가 전혀 없음.
        if (password.length() < 8) {
            throw new IllegalArgumentException("너무 짧음");
        }
        
        System.out.println("비밀번호 업데이트 완료");
    }
}