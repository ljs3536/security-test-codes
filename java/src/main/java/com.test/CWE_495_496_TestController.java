import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_495_496_TestController {
    // [중요 데이터] private으로 선언되어 외부 접근이 차단되어야 하는 배열
    private String[] adminRoles = {"SUPER_ADMIN", "SYSTEM_ADMIN"};

    /**
     * [CWE-496 취약점] Private 배열에 Public 데이터 할당
     * 외부에서 전달된 배열의 참조(Reference)를 그대로 내부 private 필드에 덮어씁니다.
     * 외부 객체가 배열 내용을 바꾸면 내부 adminRoles 데이터도 함께 변경됩니다.
     */
    public void setAdminRoles(String[] newRoles) {
        // 안전한 코드: this.adminRoles = newRoles.clone();
        this.adminRoles = newRoles; 
    }

    /**
     * [CWE-495 취약점] Public 메소드로 반환된 Private 배열
     * 내부 private 배열의 참조를 그대로 반환합니다.
     * 이 메서드를 호출한 쪽에서 반환받은 배열의 요소를 수정하면, 객체 내부의 adminRoles도 변조됩니다.
     */
    public String[] getAdminRoles() {
        // 안전한 코드: return this.adminRoles.clone();
        return this.adminRoles; 
    }
}
