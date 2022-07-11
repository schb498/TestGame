package testgame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

	static final int SCREEN_WIDTH = 550;
	static final int SCREEN_HEIGHT = 1000;
	static final int UNIT_SIZE = 50;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
	static final int DELAY = 20;
	int playerX = UNIT_SIZE * ((int) SCREEN_WIDTH / (UNIT_SIZE * 2));
	int playerY = UNIT_SIZE * ((int) SCREEN_HEIGHT / (UNIT_SIZE * 2) + 5);
	int bodyParts = 4;
	int score;
	int appleX[] = new int[GAME_UNITS];
	int appleY[] = new int[GAME_UNITS];
	char direction = 'J';
	boolean running = false;
	Timer timer;
	Random random;
	private boolean newDirection = false;
	private boolean enter = false;
	private boolean lose = false;
	private int clock;

	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}

	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {

		if (running) {
			// Colour box
			g.setColor(Color.white);
			g.fillRect(UNIT_SIZE, 0, SCREEN_WIDTH - (2 * UNIT_SIZE), SCREEN_HEIGHT);
			g.setColor(Color.black);
			// Grid lines
			for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
			}

			// Create player
			g.setColor(new Color(45, 180, 0));
			g.fillOval(playerX, playerY, UNIT_SIZE, UNIT_SIZE);

			// Create square
			for (int i = 0; i < bodyParts; i++) {
				g.setColor(Color.lightGray);
				g.fillRect(appleX[i] + 1, appleY[i] + 1, UNIT_SIZE - 1, UNIT_SIZE - 1);
			}
			// Text
			g.setColor(Color.red);
			g.setFont(new Font("Monospaced", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + score, (SCREEN_WIDTH - metrics.stringWidth("Score: " + score)) / 2,
					g.getFont().getSize());

		} else {
			gameOver(g);
		}

	}

	public void newApple() {
		for (int i = 0; i < bodyParts; i++) {
			if (i == 0) {
				appleX[i] = (random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE) - 2) + 1) * UNIT_SIZE;
				appleY[i] = 0;
			} else {
				appleX[i] = appleX[i - 1];
				appleY[i] = appleY[i - 1] - UNIT_SIZE;
			}
		}
	}

	public void move() {
//		for (int i = bodyParts; i > 0; i--) {
//			playerX[i] = playerX[i - 1];
//			playerY[i] = playerY[i - 1];
//		}
		clock++;
		if ((clock % 2) == 0) {
			for (int i = bodyParts; i > 0; i--) {
				appleY[i] = appleY[i - 1];
			}
			appleY[0] = appleY[0] + UNIT_SIZE;
		}

//		switch (direction) {
//		case 'U':
//			playerY[0] = playerY[0] - UNIT_SIZE;
//			break;
//		case 'D':
//			playerY[0] = playerY[0] + UNIT_SIZE;
//			break;
//		case 'L':
//			playerX[0] = playerX[0] - UNIT_SIZE;
//			break;
//		case 'R':
//			playerX[0] = playerX[0] + UNIT_SIZE;
//			break;
//		}
		if (newDirection) {
			switch (direction) {
			case 'F':
				playerX = playerX - UNIT_SIZE;
				if (playerX == 0) {
					playerX = SCREEN_WIDTH - 2 * UNIT_SIZE;
				}
				newDirection = false;
				break;
			case 'J':
				playerX = playerX + UNIT_SIZE;
				if (playerX == SCREEN_WIDTH - UNIT_SIZE) {
					playerX = UNIT_SIZE;
				}
				newDirection = false;
				break;
			case 'D':
				playerX = playerX - 2 * UNIT_SIZE;
				if (playerX == 0) {
					playerX = SCREEN_WIDTH - 2 * UNIT_SIZE;
				} else if (playerX == -UNIT_SIZE) {
					playerX = SCREEN_WIDTH - 3 * UNIT_SIZE;
				}
				newDirection = false;
				break;
			case 'K':
				playerX = playerX + 2 * UNIT_SIZE;
				if (playerX == SCREEN_WIDTH - UNIT_SIZE) {
					playerX = UNIT_SIZE;
				} else if (playerX == SCREEN_WIDTH) {
					playerX = 2 * UNIT_SIZE;
				}
				newDirection = false;
				break;
			}
		}
	}

	public void checkApple() {
		for (int i = bodyParts; i >= 0; i--) {
			if ((playerX == appleX[i]) && (playerY == appleY[i])) {
				score = score + 2;
				newApple();
			}
		}

	}

	public void checkCollisions() {
//		// checks if head collides with body
//		for (int i = bodyParts; i > 0; i--) {
//			if ((playerX[0] == playerX[i]) && (playerY[0] == playerY[i])) {
//				running = false;
//			}
//		}
		// check if head touches left border
		if (playerX < UNIT_SIZE) {
			running = false;
		}
		// check if head touches right border
		if (playerX >= SCREEN_WIDTH - UNIT_SIZE) {
			running = false;
		}
//		// check if head touches top border
//		if (playerY[0] < 0) {
//			running = false;
//		}
//		// check if head touches bottom border
//		if (playerY[0] > SCREEN_HEIGHT) {
//			running = false;
//		}

		if (appleY[0] > SCREEN_HEIGHT) {
			running = false;
		}
		if (lose) {
			running = false;
			// timer.stop();
		}
	}

	public void gameOver(Graphics g) {
		// Clear variables
		playerX = UNIT_SIZE * ((int) SCREEN_WIDTH / (UNIT_SIZE * 2));
		newApple();
		enter = false;
		// Score
		g.setColor(Color.red);
		g.setFont(new Font("Monospaced", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: " + score, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + score)) / 2,
				g.getFont().getSize());
		// Game Over text
		g.setColor(Color.red);
		g.setFont(new Font("Monospaced", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (running) {
			if (enter) {
				move();
				checkApple();
				checkCollisions();
			}

		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_SPACE:
				enter = true;
				break;
			case KeyEvent.VK_R:
				running = true;
				score = 0;
				break;
			case KeyEvent.VK_F:
				direction = 'F';
				if (running) {
					newDirection = true;
				}
				if (score > 0 && enter) {
					score--;
				}
				break;
			case KeyEvent.VK_J:
				direction = 'J';
				if (running) {
					newDirection = true;
				}
				if (score > 0 && enter) {
					score--;
				}
				break;
			case KeyEvent.VK_D:
				direction = 'D';
				if (running) {
					newDirection = true;
				}
				if (score > 0 && enter) {
					score--;
				}
				break;
			case KeyEvent.VK_K:
				direction = 'K';
				if (running) {
					newDirection = true;
				}
				if (score > 0 && enter) {
					score--;
				}
				break;
			}
		}
	}
}