import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Game extends Applet implements KeyListener, Runnable {
	enum GameState {
		Running, Lost, LevelUp, Pause
	}

	GameState state; //= GameState.Running;
	private Font font = new Font(null, Font.BOLD, 30);

	private Racket racket;
	private Ball ball;
	private ArrayList<Brick> bricks;
	private int numOfBricks = 36;
	private Image image;
	private Graphics second;
	private Thread thread;
	public final int WINDOW_WIDTH = 600;
	public final int WINDOW_HEIGHT = 600;
	private static int level = 0;

	public Game() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setBackground(Color.LIGHT_GRAY);
		setFocusable(true);
		addKeyListener(this);
		Frame frame = (Frame) this.getParent().getParent();
		frame.setTitle("Arkanoid");
		frame.setResizable(false);
		startNewGame();
	}

	private void startNewGame() {
		racket = new Racket(WINDOW_WIDTH, WINDOW_HEIGHT);
		ball = new Ball(WINDOW_WIDTH, WINDOW_HEIGHT, level);
		bricks = new ArrayList<Brick>();
		numOfBricks = 36;
		// Create 36 bricks
		int x = WINDOW_WIDTH / 10;
		int y = WINDOW_HEIGHT / 10;
		for (int i = 0; i < numOfBricks; i++) {
			if (i % 9 == 0 && i != 0) {
				x = WINDOW_WIDTH / 10;
				y += WINDOW_HEIGHT / 10;
			}
			bricks.add(new Brick(x, y));
			x += WINDOW_WIDTH / 10;
		}
	}

	@Override
	public synchronized void start() {
		if (state == GameState.Running)return;
		state = GameState.Running;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public synchronized void stop() {
		if (state != GameState.Running)return;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void destroy() {

	}

	public void run() {
		if (state == GameState.Running){
			while (true) {
				racket.update();
				ball.update();

				if (ball.getRectangle().getMaxY() >= WINDOW_HEIGHT) {
					state = GameState.Lost;
					level = 0;
				}

				if (ball.getRectangle().intersects(racket.getRectangle())) {
					ball.velocityY *= -1;
					ball.velocityX += racket.getVelocity() / 2;
				}
				for (Brick brick : bricks) {
					if (ball.getRectangle().intersects(brick.getRectangle())) {

						// check for vertical collision
						if ((ball.getRectangle().getMinY() < brick
								.getRectangle().getMaxY() && ball
								.getRectangle().getMinY() >= brick
								.getRectangle().getMaxY() + ball.velocityY)
								|| (ball.getRectangle().getMaxY() > brick
										.getRectangle().getMinY() && ball
										.getRectangle().getMaxY() <= brick
										.getRectangle().getMinY()
										+ ball.velocityY)) {
							// vertical collision, reverse vertical velocity
							ball.velocityY *= -1;
						}

						// check for horizontal collision
						if ((ball.getRectangle().getMaxX() > brick
								.getRectangle().getMinX() && ball
								.getRectangle().getMaxX() <= brick
								.getRectangle().getMinX() + ball.velocityX)
								|| (ball.getRectangle().getMinX() < brick
										.getRectangle().getMaxX() && ball
										.getRectangle().getMinX() >= brick
										.getRectangle().getMaxX()
										+ ball.velocityX)) {
							// horizontal collision, reverse horizontal velocity
							ball.velocityX *= -1;
						}
						brick.destroy();

					}

				}
				// Remove deactivated bricks
				int n = numOfBricks;
				for (int i = n - 1; i >= 0; i--) {
					if (!bricks.get(i).isActive()) {
						bricks.remove(i);
						numOfBricks--;
					}
				}
				
				// Level up if all the bricks are destroyed
				if (numOfBricks == 0){
					state = GameState.LevelUp;
					//level++;
				}

				repaint();
				try {
					Thread.sleep(17);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public void update(Graphics g) {
		if (image == null) {
			image = createImage(this.getWidth(), this.getHeight());
			second = image.getGraphics();
		}

		second.setColor(getBackground());
		second.fillRect(0, 0, getWidth(), getHeight());
		second.setColor(getForeground());
		paint(second);

		g.drawImage(image, 0, 0, this);

	}

	@Override
	public synchronized void paint(Graphics g) {
		switch(state) {
		case LevelUp:
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
			g.setColor(Color.WHITE);
			g.setFont(font);
			g.drawString("Level up!", WINDOW_WIDTH/2-20, WINDOW_HEIGHT/2);
			g.drawString("Press Space to continue", WINDOW_WIDTH/2-120, WINDOW_HEIGHT/2+40);
			break;
		case Lost:
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
			g.setColor(Color.WHITE);
			g.setFont(font);
			g.drawString("Lost", WINDOW_WIDTH/2-20, WINDOW_HEIGHT/2);
			g.drawString("Press Space to restart", WINDOW_WIDTH/2-120, WINDOW_HEIGHT/2+40);
			break;
		case Running:
			racket.paint(g);
			ball.paint(g);
			for (Brick brick : bricks) {
				brick.paint(g);
			}
			break;
		case Pause:
			racket.paint(g);
			ball.paint(g);
			for (Brick brick : bricks) {
				brick.paint(g);
			}
			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			racket.setMovingLeft(true);
			break;
		case KeyEvent.VK_RIGHT:
			racket.setMovingRight(true);
			break;
		case KeyEvent.VK_SPACE:
			if (state == GameState.Running) {
				state = GameState.Pause;
			} else if (state == GameState.Pause) {
				System.out.println("resume");
				state = GameState.Running;
				System.out.println(state);
			}
			if (state == GameState.Lost || state == GameState.LevelUp) {
				if (state == GameState.LevelUp) {
					level++;
				}
				startNewGame();
				state = GameState.Running;
			}
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			racket.setMovingLeft(false);
			break;
		case KeyEvent.VK_RIGHT:
			racket.setMovingRight(false);
			break;
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
