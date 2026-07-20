public class SharedConfig {
    private static SharedConfig instance; // 정규식에 걸림
    private String configData; // 취약점: 인스턴스 생성은 안전해도 내부 데이터 동기화가 없음

    public void updateConfig(String data) {
        this.configData = data; // 멀티스레드 환경에서 데이터 오염 발생 가능
    }
}