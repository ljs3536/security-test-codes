@Service
public class CWE_488_WrongSession {
    // 보안 취약점: 싱글톤 빈 내부에 사용자 세션 데이터를 담는 필드 선언
    private User currentUser; 

    // 생성자 주입 (제외해야 함)
    private final PaymentRepository paymentRepo;
    public OrderProcessor(PaymentRepository paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    // 문제의 메서드: 멀티스레드 환경에서 A사용자 데이터가 B사용자 요청에 노출됨
    public void processOrder(User user, OrderRequest req) {
        this.currentUser = user; // 1. 세션 정보 할당
        
        saveOrder(this.currentUser, req); 
    }
}