package kb;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main extends JFrame {
    private JButton button;
    
    public Main() {
        initUI();
    }

    private void initUI() {
        JLabel statusBar = new JLabel("");
        add(statusBar, BorderLayout.SOUTH);
        JLabel northBar = new JLabel("");
        northBar.setHorizontalAlignment(JLabel.CENTER);
        add(northBar, BorderLayout.NORTH);
        
        button = new JButton("Click here!");
        add(button);
        
        add(new maze(statusBar, northBar));

        setResizable(false);
        pack();

        setTitle("Maze Runner");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var ex = new Main();
            ex.setVisible(true);
        });
    }
}