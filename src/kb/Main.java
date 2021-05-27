package kb;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Java Minesweeper Game
 *
 * Author: Jan Bodnar
 * Website: http://zetcode.com
 */

public class Main extends JFrame {

    private JLabel statusbar;
    private JLabel northbar;

    private JButton button;
    
    public Main() {

        initUI();
    }

    private void initUI() {

        statusbar = new JLabel("");
        add(statusbar, BorderLayout.SOUTH);
        northbar = new JLabel("");
        northbar.setHorizontalAlignment(JLabel.CENTER);
        add(northbar, BorderLayout.NORTH);
        
        button = new JButton("Click here!");
        add(button);
        
        add(new maze(statusbar,northbar));

        setResizable(false);
        pack();

        setTitle("Maze Runner");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
    	System.out.println("Working Directory = " + System.getProperty("user.dir"));

        EventQueue.invokeLater(() -> {

            var ex = new Main();
            ex.setVisible(true);
        });
    }
}