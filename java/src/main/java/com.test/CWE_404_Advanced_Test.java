import java.io.*;

public class CWE_404_Advanced_Test {
    public void resourceLeakTest() throws IOException {

        FileInputStream fis = new FileInputStream("test.txt");
        
        processComplexLogic();
        processComplexLogic();
        processComplexLogic();
        processComplexLogic();
        processComplexLogic();
        processComplexLogic();
        
        // 자원 해제 시점
        fis.close(); 
    }
    
    private void processComplexLogic() {}
}