package javaTest;

public class CWE_457_Uninitialized_Variable_Sample {

    // 1. 조건 분기(if-else)에서 모든 경로가 커버되지 않아 초기화되지 않을 수 있는 변수 패턴 (CWE-457)
    public int calculateDiscountRateUnsafe(String userGrade) {
        int discountRate; // 선언만 하고 초기값을 주지 않음
        
        if ("VIP".equals(userGrade)) {
            discountRate = 20;
        } else if ("GOLD".equals(userGrade)) {
            discountRate = 10;
        }
        // [취약점 포인트] 만약 user가 "SILVER"나 "GENERAL"처럼 다른 등급일 경우, 
        // 위 if-else 타지 않아 discountRate가 초기화되지 않은 상태로 아래에 도달함
        
        // 초기화되지 않았을 수 있는 변수를 그대로 연산에 사용하여 오류 유발
        return 100 - discountRate; 
    }

    // 2. 객체 내부 필드(멤버 변수)가 생성자나 메서드에서 초기화 누락된 패턴
    private static class Transaction {
        private String transactionId;
        private double amount; // 초기화되지 않으면 기본값 0.0으로 설정됨
        
        public void setTransactionId(String id) {
            this.transactionId = id;
            // amount 값을 설정하는 로직이 누락됨
        }
        
        public double getAmount() {
            return amount;
        }
    }

    public void processTransactionUnsafe() {
        Transaction tx = new Transaction();
        tx.setTransactionId("TX-1004");
        
        // [취약점 포인트] amount 필드가 초기화되지 않은 상태(0.0)임을 인지하지 못하고 
        // 결제 승인 로직 등에 그대로 사용하여 비즈니스 로직 왜곡 발생
        double paymentAmount = tx.getAmount();
        if (paymentAmount > 0) {
            System.out.println("Processing payment...");
        } else {
            System.out.println("Invalid payment amount due to uninitialized state!");
        }
    }
}