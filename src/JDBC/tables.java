/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JDBC;

//import java.sql.Connection;
//import java.sql.Statement;
//import javax.swing.JOptionPane;

import java.sql.Connection;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException; 

/**
 *
 * @author ADMIN
 */
public class tables {
      public static void main(String[] args) {
        Connection con = null;
        Statement st = null;
         try {
            con = jdbcConnection.getConnection();

            if (con == null) { 
                JOptionPane.showMessageDialog(null, "Failed to connect to the database. Cannot perform table operations.",
                                                "Database Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            st = con.createStatement();

            st.executeUpdate("CREATE TABLE IF NOT EXISTS reports (" +
                             "user INT AUTO_INCREMENT PRIMARY KEY," +
                             "userRole VARCHAR(50)," +
                             "name VARCHAR(200)," +
                             "contactNumber VARCHAR(50)," +
                             "email VARCHAR(200) UNIQUE," +
                             "password VARCHAR(100)," +
                             "address VARCHAR(200)," +
                             "status VARCHAR(50)" +
                             ")");
            System.out.println("Table 'reports' checked/created successfully.");

            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM reports WHERE email='superadmin@testemail.com'")) {
                rs.next();
                if (rs.getInt(1) == 0) { // If count is 0, superadmin does not exist
                    st.executeUpdate("INSERT INTO reports(userRole, name, contactNumber, email, password, address, status) " +
                                     "VALUES('SuperAdmin', 'Super Admin', '12345', 'superadmin@testemail.com', 'admin', 'philippines', 'true')");
                    System.out.println("SuperAdmin user inserted successfully.");
                } else {
                    System.out.println("SuperAdmin user already exists. Skipping insertion.");
                }
            }

            // Insert a Staff member if not exists
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM reports WHERE email='john.doe@example.com'")) {
                rs.next();
                if (rs.getInt(1) == 0) {
                    st.executeUpdate("INSERT INTO reports(userRole, name, contactNumber, email, password, address, status) " +
                                     "VALUES('Staff', 'John Doe', '987654321', 'john.doe@example.com', 'staffpass', '123 Staff St', 'true')");
                    System.out.println("Staff user 'John Doe' inserted successfully.");
                } else {
                    System.out.println("Staff user 'John Doe' already exists. Skipping insertion.");
                }
            }


            JOptionPane.showMessageDialog(null, "Database setup (table creation/check and initial data) completed successfully!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error during table operations: " + e.getMessage(),
                                            "SQL Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("SQL Error during table operations: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage(),
                                            "General Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("General Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
             
            try {
                if (st != null) st.close();
            } catch (SQLException e) {
                System.err.println("Error closing Statement: " + e.getMessage());
            }
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing Connection: " + e.getMessage());
            }
        }
    }
}
