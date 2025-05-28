/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package petcareservicesystem;

import frames.homeFrame; // Assuming homeFrame is in the 'frames' package
import com.formdev.flatlaf.FlatLightLaf; // Import the FlatLaf theme you are using
import frames.logInFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager; // For fallback L&F

public class PetCareServiceSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            FlatLightLaf.setup(); // Ensure this is the first UI-related call
        } catch (Exception ex) {
            // Log the error if FlatLaf setup fails
            Logger.getLogger(PetCareServiceSystem.class.getName()).log(Level.SEVERE, "Failed to set FlatLaf Look and Feel", ex);
            // Fallback to default L&F if FlatLaf fails
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception fallbackEx) {
                Logger.getLogger(PetCareServiceSystem.class.getName()).log(Level.SEVERE, "Failed to set cross-platform L&F", fallbackEx);
            }
        }

        /*
         * Create and display the main application frame on the Event Dispatch Thread (EDT).
         * This ensures all UI updates are handled safely.
         */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Instantiate and make your main frame visible.
                // This will internally call initComponents() which creates
                // your ComboBoxMultiSelection.
                new logInFrame().setVisible(true);
            }
        });
    }
}
