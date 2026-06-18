@RestController
@RequestMapping("/api/users")
public class UserManagementController {

    @Autowired
    private UserService userService;

    // 사용자 삭제는 매우 민감한 'Critical Function'임.
    // 하지만 Spring Security의 @PreAuthorize나 별도의 세션 체크 로직이 전혀 없음.
    // AI가 'delete'라는 단어와 '인증 부재'의 상관관계를 읽는지 확인.
    @GetMapping("/delete/{userId}")
    public String deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "User " + userId + " has been deleted.";
    }
}