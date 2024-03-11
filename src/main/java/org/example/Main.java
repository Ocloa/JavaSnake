package org.example;

import javax.swing.*;

public class Main {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    public static void main(String[] args) {
        final JFrame frame = new JFrame("Snake Game");
        ImageIcon img = new ImageIcon("resources/application_icon_149973.png");
        frame.setSize(WIDTH, HEIGHT);
        final SnakeGame game = new SnakeGame(WIDTH, HEIGHT);
        frame.add(game);
        frame.setIconImage(img.getImage());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
        game.startGame();
    }
}
