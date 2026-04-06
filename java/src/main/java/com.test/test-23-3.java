import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SafeCommandExecutor {
    // 미리 정의된 안전한 파일 목록
    private static final List<String> ALLOWED_FILES = Arrays.asList("log.txt", "config.json");

    public void executeSafe(String filename) throws IOException {
        // 입력값이 허용된 목록에 있는지 철저히 검증
        if (!ALLOWED_FILES.contains(filename)) {
            throw new IllegalArgumentException("Invalid filename");
        }

        // 입력값을 명령어 자체가 아닌 '인자(Argument)'로만 전달
        ProcessBuilder pb = new ProcessBuilder("ls", "-l", filename);
        pb.start();
    }
}