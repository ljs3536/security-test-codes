@RestController
@RequestMapping("/api/proxy")
public class ProxyController {

    @GetMapping("/fetch")
    public String fetchUrl(@RequestParam String url) {
        RestTemplate restTemplate = new RestTemplate();
        // 🔍 테스트 포인트: 사용자가 입력한 url을 검증 없이 그대로 사용
        // 공격자가 "http://localhost:8080/admin"이나 "http://169.254.169.254/..." 입력 가능
        return restTemplate.getForObject(url, String.class);
    }
}