public class CWE_674_835_Test {
    // [CWE-674 테스트] 로직상 "return 메서드명(" 패턴을 찾아야 함
    public int infiniteRecursion(int n) {
        return infiniteRecursion(n); // 이 라인이 한 줄에 포함되어야 탐지됨
    }

    // [CWE-835 테스트] 로직상 "while(true){}" 패턴을 찾아야 함
    public void infiniteLoop() {
        while (true) {} // 이 라인이 한 줄에 포함되어야 탐지됨
    }
}
