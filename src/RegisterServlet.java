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

@WebServlet("/register/table")
public class RegisterServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 处理注册逻辑
        if (registerUser(username, password)) {
            // 重定向到注册成功页面
            response.sendRedirect("success.jsp");
        } else {
            // 重定向到注册失败页面
            response.sendRedirect("failure.jsp");
        }
    }

    private boolean registerUser(String username, String password) {
        Connection connection = null;
        PreparedStatement statement = null;

        if(userExists(username)){
            return false;
        }

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
