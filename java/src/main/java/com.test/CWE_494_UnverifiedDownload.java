import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


public class CWE_494_UnverifiedDownload {
    /**
     * [취약점] 무결성 검증 없이 외부 소스를 다운로드하여 실행
     * 공격자가 중간에서 파일을 변조하면 임의의 코드 실행 가능
     */
    public void downloadAndExecutePlugin() throws Exception {
        String urlString = "https://trusted-server.com/plugin.jar";
        URL url = new URL(urlString);
        Path tempPath = Files.createTempFile("plugin", ".jar");

        // 1. 외부에서 파일 다운로드 (무결성 체크 없음)
        try (InputStream in = url.openStream()) {
            Files.copy(in, tempPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // 2. 검증 단계 없이 바로 실행/로드
        // (예: ClassLoader를 통해 jar 내의 클래스를 동적 로드)
        System.out.println("다운로드 완료. 검증 없이 로드합니다: " + tempPath.toString());
        loadPlugin(tempPath);
    }

    private void loadPlugin(Path path) {
        // 실제로는 여기서 ClassLoader나 Reflection을 사용해 로직 실행
        System.out.println("플러그인 로드 성공!");
    }
}