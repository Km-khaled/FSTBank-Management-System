import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDB {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:postgresql://localhost:5433/fstbank";
        String username = "admin";
        String password = "admin";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                System.out.println("Connection successful!");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}