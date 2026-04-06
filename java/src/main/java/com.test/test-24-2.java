@RestController
@RequestMapping("/api/user")
public class UserUpdateController {

    @Autowired
    private UserService userService;

    @PostMapping("/update")
    public String updateUserInfo(@RequestBody UserUpdateDTO updateDto, HttpSession session) {
        // URL에는 ID가 없지만, UserUpdateDTO 내부의 id 필드를 공격자가 조작할 수 있음
        userService.updateUser(updateDto);
        return "Update Successful";
    }
}

class UserUpdateDTO {
    private String id;
    private String email;
    private String address;
}