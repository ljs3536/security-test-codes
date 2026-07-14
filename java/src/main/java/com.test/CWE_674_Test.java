public class CWE_674_Test {
    /**
     * [현실적 취약점]
     * 재귀 호출은 하지만, 'n'이 변하지 않거나 
     * 탈출 조건이 논리적으로 도달 불가능할 때 발생합니다.
     */
    public int calculateFactorial(int n) {
        // 실제로는 여기 n > 0 같은 탈출 조건이 있어야 하지만 누락됨
        if (n == 100) { 
            return 1;
        }
        // [취약점] n이 100이 아닐 경우, n은 변하지 않고 계속 자기 자신을 호출함
        // 분석기의 정규식 패턴: return calculateFactorial(...) 를 만족함
        return calculateFactorial(n); 
    }
}
