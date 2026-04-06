@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public UserDTO getUserProfile(@PathVariable String userId) {
        // 어떠한 권한 체크도 없이 바로 DB에서 조회하여 반환함
        return userService.findUserById(userId);
    }
}