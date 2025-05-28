/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement; 

/**
 *
 * @author ADMIN
 */
public class jdbcConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/petcare_db?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String url = "jdbc:mysql://localhost:3306/petcareservices?useSSL=false&serverTimezone=UTC";
    private static final String user = "root"; 
    private static final String password = "1234root??"; 

    public static Connection getConnection() {
        Connection conn = null; 
         try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Database connection established successfully!");
            return conn;
        } catch (SQLException e) {
            System.err.println("Database Connection Error: " + e.getMessage());
            e.printStackTrace(); 
            JOptionPane.showMessageDialog(null, "Database Connection Error: " + e.getMessage() + "\n" +
                                            "Please ensure MySQL server is running and connection details are correct.",
                                            "Connection Error", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (ClassNotFoundException e) {
            // Handle JDBC driver not found error
            System.err.println("JDBC Driver Not Found: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "MySQL JDBC Driver not found. " +
                                            "Please add 'mysql-connector-java-X.X.X.jar' to your project libraries.",
                                            "Driver Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    } 
    
    
    public static void closeConnection(Connection conn, Statement stmt, ResultSet rs) {
        try {
            // Close ResultSet first, if it exists
            if (rs != null) {
                rs.close();
                System.out.println("ResultSet closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing ResultSet: " + e.getMessage());
        }

        try {
            // Close Statement/PreparedStatement, if it exists
            if (stmt != null) {
                stmt.close();
                System.out.println("Statement closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing Statement: " + e.getMessage());
        }

        try {
            // Close Connection last, if it exists
            if (conn != null) {
                conn.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing Connection: " + e.getMessage());
        }
        System.out.println("All database resources attempted to be closed.");
    }
    
     public static void main(String[] args) {
        Connection testConn = getConnection();
        PreparedStatement testPstmt = null;
        ResultSet testRs = null;

        if (testConn != null) {
            System.out.println("Test connection successful!");
            try {
                // Example: Execute a simple query for testing
                String sql = "SELECT 1";
                testPstmt = testConn.prepareStatement(sql);
                testRs = testPstmt.executeQuery();
                if (testRs.next()) {
                    System.out.println("Simple query executed successfully.");
                }
            } catch (SQLException e) {
                System.err.println("Test query failed: " + e.getMessage());
            } finally {
                closeConnection(testConn, testPstmt, testRs);
            }
        } else {
            System.out.println("Test connection failed.");
        }
    }
    
    
}
    
