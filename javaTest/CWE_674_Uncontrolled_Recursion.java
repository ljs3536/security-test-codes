package javaTest;

public class CWE_674_Uncontrolled_Recursion {

    // 1. 종료 조건이 없는 전형적인 무한 재귀 메서드 (CWE-674)
    public int calculateFactorialUnsafe(int n) {
        // [취약점 포인트] n이 0이 되거나 작아질 때 멈추는 탈출 조건(Base Case)이 누락됨
        // 혹은 잘못된 조건 설정으로 인해 계속해서 자신을 호출하게 됨
        System.out.println("Current n: " + n);
        
        // 의도치 않게 조건이 맞지 않거나 누락되어 무한 스택 적재 발생
        return n * calculateFactorialUnsafe(n - 1); 
    }

    // 2. 동적 트리 구조나 경로 탐색 시 탈출 조건이 부실한 재귀 메서드
    public void traverseDirectoryRecursive(String path, int depth) {
        // [취약점 포인트] 최대 깊이(Max Depth) 제한이나 순환 참조(Symbolic Link 등)를 
        // 방어할 검증 로직이 없어 스택 오버플로우를 유발할 수 있는 구조
        traverseDirectoryRecursive(path + "/sub", depth + 1);
    }
}