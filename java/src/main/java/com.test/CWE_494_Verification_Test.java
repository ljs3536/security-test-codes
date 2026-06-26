import java.io.*;
import java.nio.file.*;
import java.net.URL;
import java.net.URLClassLoader;

public class CWE_494_Verification_Test {
    // 1. SSRF 탐지 회피: 파라미터가 아닌, 내부적으로 생성된 경로 사용
    // 분석기는 이제 URL을 "사용자 입력"으로 보지 않으므로 SSRF 경고를 하지 않습니다.
    private String getUnsafeDownloadPath() {
        return "https://trusted-server.com/plugin.jar"; 
    }

    public void loadUpdate() throws Exception {
        Path targetPath = Paths.get("/tmp/plugin.jar");
        String urlString = getUnsafeDownloadPath(); // 상수가 아닌 메서드 호출로 SSRF 룰을 우회

        // 2. 다운로드: 여기서 SSRF가 안 걸리는지 확인
        try (InputStream in = new URL(urlString).openStream()) {
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // 3. CWE-494 유도: '다운로드된 파일'을 '검증 없이' 로드
        // 이 지점이 핵심입니다. 분석기가 ClassLoader 로직을 타게 만듭니다.
        File jarFile = targetPath.toFile();
        URLClassLoader loader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()});
        
        // 4. 리플렉션을 통해 동적 실행 (분석기가 탐지하는 Sink)
        Class<?> clazz = loader.loadClass("com.plugin.DynamicPlugin");
        clazz.getDeclaredMethod("execute").invoke(clazz.getDeclaredConstructor().newInstance());
    }
}
