import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.sql.*;

@WebServlet("/login/menu")
public class LoginServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    /**
     CREATE DATABASE user_info_db;

     USE user_info_db;

     CREATE TABLE users (
     id INT AUTO_INCREMENT PRIMARY KEY,
     username VARCHAR(255) NOT NULL,
     password VARCHAR(255) NOT NULL
     );
     **/
    public static final String JDBC_URL = "jdbc:mysql://localhost:3306/user_info_db";
    public static final String JDBC_USER = "root";
    public static final String JDBC_PASSWORD = "Eudei6oh";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (!userExists(username)) {
            request.setAttribute("LogPrompt", "账号不存在");
            try {
                request.getRequestDispatcher("menu.jsp").forward(request, response);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        if (validateUser(username, password)) {
            //重定向至登陆成功页面
            response.sendRedirect("../blog/index.jsp");
        } else {
            request.setAttribute("LogPrompt", "密码错误");
            try {
                request.getRequestDispatcher("menu.jsp").forward(request, response);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean validateUser(String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
                String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, username);
                    statement.setString(2, password);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        return resultSet.next();
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean userExists(String username) {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (
                Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username=?")
        ) {
            preparedStatement.setString(1, username);
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
