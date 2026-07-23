package javaTest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CWE_285_Improper_Authorization_Sample {

    // 1. 로그인된 사용자의 ID와 조회하려는 대상 리소스의 소유자 ID를 대조하는 인가 검증이 누락된 취약한 패턴 (CWE-285)
    @GetMapping("/api/user/{userId}/profile")
    public String getUserProfileUnsafe(
            @PathVariable("userId") String targetUserId, 
            @RequestParam("currentUserId") String currentUserId) {
        
        // [취약점 포인트] 사용자가 로그인을 했더라도(인증 완료), 
        // 현재 요청을 보낸 사용자(currentUserId)가 타인의 정보(targetUserId)를 조회할 권한이 있는지 
        // 소유권 검증(Authorization) 로직(예: if (!targetUserId.equals(currentUserId)))이 누락되어 있음
        // 공격자가 URL 경로의 userId만 다른 사람의 ID로 변경하면 타인의 민감한 개인정보를 무단으로 열람할 수 있음 (IDOR 취약점 연계)
        
        return "Profile Data for User: " + targetUserId + " (Requested by: " + currentUserId + ")";
    }
}