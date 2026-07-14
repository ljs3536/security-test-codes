public class CWE_390_754_Extended_Test {
    
    // [CWE-390 고도화 테스트] 로그를 안 남기고 예외를 그냥 무시함 (현재 로직은 잡을 수도 있음)
    public void testLogging() {
        try {
            int result = 10 / 0;
        } catch (Exception e) {
            // 아무것도 안 함 (Empty Catch)
        }
    }

    // [CWE-754 고도화 테스트] 예외를 잡았지만 로직은 성공인 것처럼 진행함
    public void testFlowControl() {
        try {
            saveToDatabase();
        } catch (Exception e) {
            // [위험] 로그도 없고, 무조건 성공인 것처럼 상태를 반환함
        }
    }
}