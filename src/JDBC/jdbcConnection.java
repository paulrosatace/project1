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
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/petcare_db?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String url = "jdbc:mysql://localhost:3306/petcareservices?useSSL=false&serverTimezone=UTC";
    private static final String user = "root"; 
    private static final String password = "1234root??"; 

    public static Connection getConnection() {
        Connection conn = null; 
        System.out.println("Connection: " + conn);
        System.out.println("Database connection established successfully!");
         try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Database connection established successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ JDBC Driver not found: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                "JDBC Driver not found. Please ensure the MySQL JDBC driver is included in the project.",
                "Driver Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            System.err.println("❌ SQL Connection Error: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                "Database connection failed.\nCheck server and credentials.\n\n" + e.getMessage(),
                "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
        return conn;
    } 
    
    
    public static void closeConnection(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.err.println("⚠️ Failed to close ResultSet: " + e.getMessage());
        }

        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("⚠️ Failed to close Statement: " + e.getMessage());
        }

        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("⚠️ Failed to close Connection: " + e.getMessage());
        }
    }

    public static void closeConnection(Connection conn, Statement stmt) {
        closeConnection(conn, stmt, null);
    }
    
     public static void main(String[] args) {
        Connection testConn = getConnection();
        PreparedStatement testPstmt = null;
        ResultSet testRs = null;

        if (testConn != null) {
            try {
                testPstmt = testConn.prepareStatement("SELECT 1");
                testRs = testPstmt.executeQuery();
                if (testRs.next()) {
                    System.out.println("✅ Test query executed successfully.");
                }
            } catch (SQLException e) {
                System.err.println("❌ Test query failed: " + e.getMessage());
            } finally {
                closeConnection(testConn, testPstmt, testRs);
            }
        } else {
            System.out.println("❌ Test connection failed.");
        }
    }
    
    
}
    
