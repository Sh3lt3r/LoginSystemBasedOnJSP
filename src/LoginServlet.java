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
    /**TODO 请根据以下指令输入在数据库的命令提示行进行创建数据库 (MySQL)
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
    //TODO 请在下方修改你的数据库密码
    public static final String JDBC_PASSWORD = "Eudei6oh";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (validateUser(username, password)) {
            //重定向至登陆成功页面
            response.sendRedirect("success.jsp");
        } else {
            response.sendRedirect("failure.jsp");
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
}
