import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommandInjectionServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 사용자가 파일명을 입력 (예: "test.txt; rm -rf /")
        String filename = request.getParameter("filename");

        // 문자열 연결을 통해 직접 명령어를 생성
        // 'Runtime.exec' 함수에 외부 입력값이 전달되는 것을 잡는지 확인
        try {
            Process process = Runtime.getRuntime().exec("ls -l " + filename);
            process.waitFor();
            response.getWriter().println("Command executed for: " + filename);
        } catch (InterruptedException e) {
            response.sendError(500, "Error executing command");
        }
    }
}