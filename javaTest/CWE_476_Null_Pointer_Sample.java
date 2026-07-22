package javaTest;

public class CWE_476_Null_Pointer_Sample {

    // 사용자 정보 객체 내부 클래스
    public static class User {
        private String name;
        public String getName() { return name; }
    }

    // 1. 외부 입력이나 DB 조회 결과가 null일 수 있음을 인지하지 못하고 역참조하는 경우 (CWE-476)
    public String processUserProfileUnsafe(User user) {
        // [취약점 포인트] user 파라미터가 null인지에 대한 사전 검증(Null Check)이 전혀 없는 상태에서 
        // 곧바로 user.getName()을 호출하므로, user가 null로 들어올 경우 즉시 NullPointerException 발생
        String userName = user.getName(); 
        
        return "User processed: " + userName.toUpperCase();
    }

    // 2. 내부 메서드 반환값이 null일 수 있는데 방어 코드가 없는 경우
    public String findAndProcessData(String id) {
        User user = findUserById(id); // 만약 일치하는 사용자가 없어 이 메서드가 null을 리턴한다면?
        
        // [취약점 포인트] 반환된 객체의 속성에 접근하거나 메서드를 호출할 때 
        // null 방어 가드(`if (user == null)`)가 누락되어 시스템 장애로 이어짐
        return user.getName();
    }

    private User findUserById(String id) {
        // DB 조회 로직 가정 (상황에 따라 null 반환 가능)
        return null; 
    }
}