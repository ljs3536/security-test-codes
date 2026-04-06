import java.io.IOException;
import java.nio.ByteBuffer;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// allocate 메서드에 외부 입력값이 그대로 들어감
public class NioResourceServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String inputSize = request.getParameter("size");
        int size = Integer.parseInt(inputSize);
        ByteBuffer buffer = ByteBuffer.allocate(size);

        response.getWriter().println("size: " + size);
    }
}