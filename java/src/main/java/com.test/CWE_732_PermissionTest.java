import java.io.File;
import java.io.IOException;

public class CWE_732_PermissionTest {

    public void createConfigFile(String configData) throws IOException {
        File configFile = new File("/app/config/settings.properties");
        
        // [CWE-732 취약점] 파일에 대한 권한을 너무 넓게 설정함
        // setWritable(true, false) -> false(ownerOnly)이므로 모든 사용자에게 쓰기 권한(O_RWRW_RW_)을 줌
        // 분석기가 setReadable, setWritable, setExecutable의 두 번째 인자가 'false'인 것을 잡아내면 됨
        configFile.setReadable(true, false);
        configFile.setWritable(true, false); 
        configFile.setExecutable(true, false);
        
        System.out.println("설정 파일이 생성되었으나 누구나 수정 가능합니다.");
    }
}