import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

// 사용자가 매우 큰 값을 입력하면 OutOfMemoryError 발생 가능
public class ResourceAllocationServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String countParam = request.getParameter("count");
        int count = Integer.parseInt(countParam);

        List<String> dataList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            dataList.add("Data item " + i);
        }

        response.getWriter().println("Successfully allocated " + count + " items.");
    }
}