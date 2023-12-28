import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        // 检查session中是否存在用户信息
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            // 用户已登录，显示主页
            response.sendRedirect("/blog");
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/register/table.jsp");
            dispatcher.forward(request, response);
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String repassword = request.getParameter("repassword");
        // 处理注册逻辑
        if (!Pattern.checkAC(username)) {
            forwardToTableJSP(request, response, "账号不符合格式，请以首字母开头，并由至少五个大小写字母与数字组成");
            return;
        }

        if (!Pattern.checkPW(password)) {
            forwardToTableJSP(request, response, "密码不符合格式，请以首字母开头，并由至少六个大小写字母与数字组成");
            return;
        }

        if (!Objects.equals(repassword, password)) {
            forwardToTableJSP(request, response, "两次密码不一致");
            return;
        }

        if (userExists(username)) {
            forwardToTableJSP(request, response, "账号已存在");
            return;
        }

        if (registerUser(username, password)) {
            HttpSession session = request.getSession();
            session.setAttribute("loggedIn", true);
            session.setAttribute("username", username);
            // 重定向到注册成功页面
            response.sendRedirect("/blog");
        } else {
            forwardToTableJSP(request, response, "遇到了未知的错误，账号注册失败");
        }
    }

    private void forwardToTableJSP(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
        request.setAttribute("RegPrompt", message);
        request.getRequestDispatcher("/register/table.jsp").forward(request, response);
    }

    private boolean registerUser(String username, String password) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // 注册 JDBC 驱动
            Class.forName(LoginServlet.JDBC_DRIVER);

            // 打开数据库连接
            connection = DriverManager.getConnection(LoginServlet.JDBC_URL, LoginServlet.JDBC_USER, LoginServlet.JDBC_PASSWORD);

            // 插入用户信息
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            // 执行 SQL
            int rowsAffected = statement.executeUpdate();

            // 注册成功，返回 true
            return rowsAffected > 0;
        } catch (ClassNotFoundException | SQLException e) {
            // 异常处理
            e.printStackTrace();
            return false;
        } finally {
            // 关闭数据库连接
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean userExists(String username) {
        try (Connection connection = DriverManager.getConnection(LoginServlet.JDBC_URL, LoginServlet.JDBC_USER, LoginServlet.JDBC_PASSWORD)) {
            String query = "SELECT * FROM users WHERE username=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                return preparedStatement.executeQuery().next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
