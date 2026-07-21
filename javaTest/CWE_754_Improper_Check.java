package javaTest;

public class CWE_754_Improper_Check {

    // 1. 예외 발생 가능 조건을 사전에 검증(Check)하지 않아 발생하는 부적절한 예외 처리 (CWE-754)
    public int divideNumbersUnsafe(String numStr1, String numStr2) {
        int val1 = Integer.parseInt(numStr1);
        int val2 = Integer.parseInt(numStr2);
        
        // [취약점 포인트] numStr2가 "0"이거나 숫자가 아닐 경우 발생할 수 있는 
        // NumberFormatException이나 ArithmeticException을 사전에 조건문으로 방어하지 않고,
        // 무작정 실행하다가 catch 문으로 때우거나 예외를 방치함
        return val1 / val2;
    }

    // 2. 외부 입력값의 유효성 검증(Bounds/Null Check) 누락 패턴
    public char getFirstCharacterUnsafe(String text) {
        // [취약점 포인트] text가 null이거나 빈 문자열("")일 경우 
        // StringIndexOutOfBoundsException 등이 터질 수 있음을 인지하면서도 사전 체크 로직이 부실함
        return text.charAt(0);
    }
}