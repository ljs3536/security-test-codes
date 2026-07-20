@RestController
@RequestMapping("/api/admin")
public class CWE_489_DebugCode {
 
    @Autowired
    private UserService userService;

    // 취약점: 상용 환경에 그대로 노출되면 위험한 디버그용 전용 엔드포인트
    @GetMapping("/debug/reset-password")
    public ResponseEntity<String> debugResetPassword(@RequestParam String username, @RequestParam String newPassword) {
        // [위험] 인증 및 권한 검증 과정 없이, 디버그 편의를 위해 만들어 둔 백도어성 코드
        boolean isReset = userService.forceResetPassword(username, newPassword);
        
        if (isReset) {
            return ResponseEntity.ok("[DEBUG] Password reset successfully for: " + username);
        }
        return ResponseEntity.badRequest().body("[DEBUG] Failed to reset password.");
    }
}
