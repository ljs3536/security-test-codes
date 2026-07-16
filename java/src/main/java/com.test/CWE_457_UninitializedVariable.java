public class CWE_457_UninitializedVariable {
    public void bad(int input) {
        int x; // 변수 선언, 초기화 없음

        if (input > 10) {
            x = 20;
        }
        
        System.out.println("Value of x: " + x); 
    }

    public void definitelyBad() {
        int y;
        
        // 명확한 취약점: 어떤 분기 조건도 없이 바로 사용
        System.out.println("Value of y: " + y);
    }
}
