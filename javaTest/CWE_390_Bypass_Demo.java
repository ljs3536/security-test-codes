package javaTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class CWE_390_Bypass_Demo {

    private static final Logger logger = Logger.getLogger(CWE_390_Bypass_Demo.class.getName());

    // CWE-390(오류 대응 부재)을 발생시키는 실무형 코드
    public void processFileWithLogOnly(String filePath) {
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            // 파일 처리 로직 중 예외 발생 가정
            fis.close();
        } catch (IOException e) {
            logger.warning("File processing failed.");
        }
    }
}