import java.io.*;
import java.net.Socket;

public class CWE_404_Test {
    public void resourceLeakTest() throws IOException {
        // [CWE-404 탐지 대상] 

        FileInputStream fis = new FileInputStream("test.txt");
        

        int data = fis.read();
        System.out.println(data);
    }
}