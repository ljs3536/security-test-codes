@RestController
public class ConfigController {

    // 시스템 설정(System Property)을 바꾸는 기능임.
    // 권한이 없는 일반 사용자나 공격자가 시스템을 마비시킬 수 있음.
    // AI가 이 엔드포인트의 '위험성'을 인지하고 인증 필요성을 제안하는가?
    @PostMapping("/system/config/update")
    public void updateSystemConfig(@RequestBody ConfigDTO config) {
        System.setProperty(config.getKey(), config.getValue());
    }
}