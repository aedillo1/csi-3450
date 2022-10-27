package utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtils {

    Connection conn = null;
    PreparedStatement pst;
    ResultSet rs;

    private static String db = "library";
    private static String url = "jdbc:mariadb://localhost/" + db;
    private static String user = "root";
    private static String pwd = "alpine";

    public static Boolean login = false;

    public static Connection conDB()
    {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            Connection conn = DriverManager.getConnection(url, user, pwd);
            return conn;

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("DBUtil Failed: " + e.getMessage());
            return null;
        }
    }

    public Boolean checkLogin(String id, String pass) throws IOException {
        try {
            conn = conDB();
            pst = conn.prepareStatement("SELECT * FROM employees WHERE EMP_ID=? AND EMP_PASS=?");
            pst.setString(1, id);
            pst.setString(2, pass);

            rs = pst.executeQuery();

            if (rs.next())
            {
                return true;
            }
            else
            {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
