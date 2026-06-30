public class CWE_390_EmptyCatch {
    public void performAction() {
        try {
            // 외부 리소스 접근 등 예외 발생 가능성이 높은 코드
            int result = 10 / 0; 
        } catch (Exception e) {
            // [취약점] 빈 Catch 블록: 예외를 그냥 삼켜버려 원인을 알 수 없게 만듦
            // 분석기가 catch 블록의 body가 비어있는 것을 반드시 잡아내야 함
        }
    }
}