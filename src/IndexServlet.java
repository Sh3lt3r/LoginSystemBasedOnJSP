import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serial;

@WebServlet(urlPatterns = {"/blog"})
public class IndexServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 检查session中是否存在用户信息
        HttpSession session = request.getSession(false);
        /*if (session != null) {
            System.out.println("User: " + session.getAttribute("username") + " entered blog page");
        }else{
            System.out.println("User: unknown entered blog page");
        }
         */
        RequestDispatcher dispatcher = request.getRequestDispatcher("/blog/index.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 检查是否包含名为 "logoutButton" 的参数，表示用户点击了登出按钮
        if (request.getParameter("logoutButton") != null) {
            // 执行登出逻辑
            HttpSession session = request.getSession();
            session.invalidate();
            // 刷新页面
            RequestDispatcher dispatcher = request.getRequestDispatcher("/blog/index.jsp");
            dispatcher.forward(request, response);
        }
    }
}
