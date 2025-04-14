package main.java.com.chess;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import main.java.com.chess.ui.ChessUI;


public class ChessApplication {

    /**
     * Main method to start the application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the main frame
            JFrame frame = new JFrame("Chess Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create the chess UI and add it to the frame
            ChessUI chessUI = new ChessUI();
            frame.getContentPane().add(chessUI);

            // Set up the frame
            frame.pack();
            frame.setLocationRelativeTo(null); // Center the frame
            frame.setVisible(true);
        });
    }
}