import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class Racket {

	// Draw support
	// private Image sprite;
	private Rectangle drawRectangle;

	// Move support
	private int windowWidth;
	private int windowHeight;
	private int velocity = 0;
	private int speed = 0;
	private int centerX, centerY;
	private boolean movingLeft, movingRight;

	public boolean isMovingLeft() {
		return movingLeft;
	}

	public void setMovingLeft(boolean movingLeft) {
		this.movingLeft = movingLeft;
	}

	public boolean isMovingRight() {
		return movingRight;
	}

	public void setMovingRight(boolean movingRight) {
		this.movingRight = movingRight;
	}

	public int getCenterX() {
		return centerX;
	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}

	private int height = 25;
	private int width = 100;

	public Racket(int windowWidth, int windowHeight) {
		// this.sprite = sprite;
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		centerX = windowWidth / 2;
		centerY = windowHeight;
		drawRectangle = new Rectangle(centerX - width / 2, centerY - height,
				width, height);
		speed = 6;
	}

	public void paint(Graphics g) {
		g.fillRect(centerX - width / 2, centerY - height, width, height);
	}

	public void update() {
		if (isMovingLeft() && isMovingRight()) {
			velocity = 0;
		} else if (isMovingLeft()) {
			velocity = -1*speed;
		} else if (isMovingRight()) {
			velocity = speed;
		} else {
			velocity = 0;
		}
		
		// Check if the racket reaches the sides of the screen and stop it if it does
		if ((isMovingLeft() && drawRectangle.getMinX()<=0)||
				(isMovingRight() && drawRectangle.getMaxX()>=windowWidth)){
			velocity = 0;
		}
		centerX += velocity;
		drawRectangle.x = centerX - width / 2;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public Rectangle getRectangle() {
		return drawRectangle;
	}

}
