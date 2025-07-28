package views;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Index index = new Index();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
