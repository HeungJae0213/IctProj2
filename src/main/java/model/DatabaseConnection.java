package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private Connection conn = null;

    public DatabaseConnection() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            this.conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522/XEPDB1", "ICTUSER", "ICT1234");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return this.conn;
    }

    public void closeConnection() {
        if (this.conn != null) {
            try {
                this.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
