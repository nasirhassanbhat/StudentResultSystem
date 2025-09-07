import java.sql.*;

public class DBConnect {
    public static Connection connect() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            return DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe",
                "system",
                "123456789"
            );
        } catch (Exception e) {
            System.out.println("DB Error: " + e);
            return null;
        }
    }
}
