package domain;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

import utils.Sound;

//Author: Thijs Brandhorst 

public class GamePanel extends JPanel implements ActionListener {

	private static final int screenWidth = 600;
	private static final int screenHeight = 600;
	private static final int unitSize = 25;
	private static final int gameUnits = (screenWidth * screenHeight) / unitSize;
	private static final int delay = 75;

	private final int x[] = new int[gameUnits];
	private final int y[] = new int[gameUnits];

	private int bodyParts = 6;
	private int applesEaten;
	private int appleX;
	private int appleY;
	private char direction = 'R'; // U = Up, D = Down, L = Left, R = Right
	private boolean running = false;
	private Timer timer;
	private Random random;

	public Color snakeColor = Color.green;
	public Color appleColor = Color.red;

	private JButton restartButton; 

	public GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());

        this.setLayout(new BorderLayout());
		
		// Restart button
        restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
        restartButton.setBackground(Color.red);
        restartButton.setForeground(Color.white);
        restartButton.setFocusPainted(false);
        restartButton.setContentAreaFilled(false);
        restartButton.setOpaque(true); 
        restartButton.addActionListener(e -> restartGame());
        restartButton.setVisible(false); 

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(restartButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
        
		startGame();
	}

	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(delay, this);
		timer.start();
		restartButton.setVisible(false);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		if (running) {
			// Drawing apple
			g.setColor(appleColor);
			g.fillOval(appleX, appleY, unitSize, unitSize);

			// Drawing snake
			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(snakeColor);
					g.fillRect(x[i], y[i], unitSize, unitSize);
				} else {
					g.setColor(new Color(0, 100, 0));
					g.fillRect(x[i], y[i], unitSize, unitSize);
				}
			}

			// Drawing score
			g.setColor(appleColor);
			g.setFont(new Font("Arial", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (screenWidth - metrics.stringWidth("Score: " + applesEaten)) / 2,
					g.getFont().getSize());
		} else {
			gameOver(g);
		}
	}

	public void newApple() {
		appleX = random.nextInt((int) (screenWidth / unitSize)) * unitSize;
		appleY = random.nextInt((int) (screenHeight / unitSize)) * unitSize;
	}

	public void move() {
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}

		switch (direction) {
		case 'U':
			y[0] = y[0] - unitSize;
			break;
		case 'D':
			y[0] = y[0] + unitSize;
			break;
		case 'L':
			x[0] = x[0] - unitSize;
			break;
		case 'R':
			x[0] = x[0] + unitSize;
			break;
		}
	}

	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			Sound.playEatSound();
			newApple();
		}
	}

	public void checkCollisions() {
		// Check if head touches body
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}

		// Check if head touches borders
		if (x[0] < 0 || x[0] > screenWidth || y[0] < 0 || y[0] > screenHeight) {
			running = false;
		}

		if (!running) {
			timer.stop();
			Sound.playGameOverSound();
			restartButton.setVisible(true);
		}
	}

	public void gameOver(Graphics g) {
		// Game over
		g.setColor(Color.red);
		g.setFont(new Font("Arial", Font.BOLD, 75));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (screenWidth - metrics1.stringWidth("Game Over")) / 2, screenHeight / 2);

		// Score
		g.setColor(Color.red);
		g.setFont(new Font("Arial", Font.BOLD, 40));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (screenWidth - metrics2.stringWidth("Score: " + applesEaten)) / 2,
				g.getFont().getSize());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_D:
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_W:
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_S:
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}

	public void restartGame() {
		// Reset stats
		bodyParts = 6;
		applesEaten = 0;
		direction = 'R';

		// Reset pos
		x[0] = 0;
		y[0] = 0;
		for (int i = 1; i < bodyParts; i++) {
			x[i] = -unitSize;
			y[i] = -unitSize;
		}

		running = true;
		startGame();
	}
}
