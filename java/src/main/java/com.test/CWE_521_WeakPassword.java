public class CWE_521_WeakPassword {

    public String registerUser(String userId, String password) {
        
        // [취약점] 비밀번호의 길이만 체크하고, 영문/숫자/특수문자 조합 등의 복잡도 검증이 없음
        // 분석기가 'password'라는 이름의 변수에 대해 정규식(Pattern.matches) 검사 로직이 없음을 감지하는지 확인
        if (password != null && password.length() >= 4) {
            
            // DB에 사용자 정보 저장 로직 진행...
            return "회원가입 성공";
        }
        
        return "비밀번호가 너무 짧습니다.";
    }
}