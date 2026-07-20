public class SafeConfig {
    // Initialization-on-demand holder idiom (가장 안전한 싱글톤 방식)
    private static class Holder {
        static final SafeConfig INSTANCE = new SafeConfig();
    }
    public static SafeConfig getInstance() {
        return Holder.INSTANCE;
    }
}