import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 매우 큰 숫자가 들어오면 바로 OutOfMemoryError 발생
public class DirectMemoryAllocationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String sizeParam = request.getParameter("bufferSize");
        int size = Integer.parseInt(sizeParam);

        byte[] memoryBuffer = new byte[size]; 
        
        response.getWriter().println("size : " + size );
    }
}