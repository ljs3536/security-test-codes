public class CWE_835_Test {
    // [현실적 취약점] 탈출 조건이 상황에 따라 도달 불가능해지는 무한 루프
    public void processData(int value) {
        int i = 0;
        // i가 음수일 경우 i--로 인해 0보다 계속 작아져 루프를 절대 빠져나올 수 없음
        while (i < 10) {
            if (value > 0) {
                i++;
            } else {
                i--; // [위험] 음수일 때 무한 루프 발생
            }
        }
    }
}
