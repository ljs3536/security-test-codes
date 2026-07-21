package javaTest;

public class CWE_835_Infinite_Loop_Sample {

    // 1. 반복문 내에서 탈출 변수가 잘못 갱신되는 전형적인 무한 루프 (CWE-835)
    public void processPaginationUnsafe(int totalCount) {
        int currentIdx = 0;
        
        // [취약점 포인트] 조건문은 currentIdx가 totalCount에 도달하길 바라지만,
        // 내부 로직에서 인덱스가 오히려 감소하거나 갱신 누락이 발생할 경우 무한 루프 유발
        while (currentIdx < totalCount) {
            System.out.println("Processing index: " + currentIdx);
            
            // 만약 실무 로직 오타나 조건 분기 실수로 인해 currentIdx가 감소하거나 멈춘다면?
            currentIdx--; // 혹은 갱신 누락
            
            // 정상적인 증가 코드가 누락되거나 잘못된 경우 영원히 탈출 불가
        }
    }

    // 2. 외부 상태나 네트워크 응답을 대기하는 while 루프에서 탈출 조건 부재
    public void waitForExternalServiceState(boolean isReady) {
        // [취약점 포인트] 상태 값을 갱신하는 로직(폴링 등) 없이 외부 변수만 그대로 검사하면
        // 한 번 false로 들어온 경우 영원히 블로킹되는 무한 루프 발생
        while (!isReady) {
            System.out.println("Waiting for service to be ready...");
            // isReady 값을 다시 받아오거나 갱신하는 로직이 누락됨
        }
    }
}