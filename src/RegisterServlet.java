import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

@WebServlet("/register/table")
public class RegisterServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String repassword = request.getParameter("repassword");
        // 处理注册逻辑
        if (!Pattern.checkAC(username)) {
            request.setAttribute("RegPrompt", "账号不符合格式，请以首字母开头，并由至少五个大小写字母与数字组成");
            try {
                request.getRequestDispatcher("table.jsp").forward(request, response);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if (!Pattern.checkPW(password)) {
            request.setAttribute("RegPrompt", "密码不符合格式，请以首字母开头，并由至少六个大小写字母与数字组成");
            try {
                request.getRequestDispatcher("table.jsp").forward(request, response);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if (!Objects.equals(repassword, password)) {
            request.setAttribute("RegPrompt", "两次密码不一致");
            try {
                request.getRequestDispatcher("table.jsp").forward(request, response);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if (userExists(username)) {
            request.setAttribute("RegPrompt", "账号已存在");
            try {
                request.getRequestDispatcher("table.jsp").forward(request, response);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if (registerUser(username, password)) {
            // 重定向到注册成功页面
            response.sendRedirect("../blog/index.jsp");
        } else {
            // 重定向到注册失败页面
            request.setAttribute("RegPrompt", "遇到了未知的错误，账号注册失败");
            try {
                request.getRequestDispatcher("table.jsp").forward(request, response);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }
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
