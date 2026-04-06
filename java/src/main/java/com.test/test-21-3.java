import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    // @PreAuthorize를 통해 인증(Authenticated) 및 권한(ADMIN) 체크 강제
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/purge-logs")
    public String purgeLogs() {
        return "Logs purged successfully.";
    }
}