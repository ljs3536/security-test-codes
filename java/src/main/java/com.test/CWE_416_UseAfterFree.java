import java.io.FileInputStream;
import java.io.IOException;

public class CWE_416_UseAfterFree {
    public void bad() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("test.txt");
            

            int data = fis.read();
            System.out.println("Data: " + data);
            

            fis.close();
            
            int nextData = fis.read(); 
            System.out.println("Next Data: " + nextData);
            
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
