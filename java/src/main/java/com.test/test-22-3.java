import java.util.Arrays;
import java.util.List;

public class SecureImageFetcher {
    private static final List<String> ALLOWED_DOMAINS = Arrays.asList("trusted-s3.amazonaws.com", "my-cdn.com");

    public String fetchImage(String imageUrl) throws Exception {
        URL url = new URL(imageUrl);
        String host = url.getHost();

        // 허용된 도메인 리스트에 있는지 검증
        if (!ALLOWED_DOMAINS.contains(host)) {
            throw new SecurityException("허용되지 않은 도메인 접근입니다.");
        }

        // 이후 로직 진행...
        return "Image fetched safely from " + host;
    }
}