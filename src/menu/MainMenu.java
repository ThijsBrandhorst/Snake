package menu;

import domain.GamePanel;
import domain.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {

    public MainMenu() {
        this.setTitle("Snake Game Menu");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300, 200);
        this.setLayout(new GridLayout(2, 1));

        JButton startButton = new JButton("Start Game");
        JButton exitButton = new JButton("Exit");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        this.add(startButton);
        this.add(exitButton);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private void startGame() {
        GamePanel panel = new GamePanel();
        GameFrame frame = new GameFrame(panel);
        this.dispose();
    }
}
