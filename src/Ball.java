import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Ball {
	
	// Draw support
	private int width = 50;
	private int height = 50;
	private Rectangle drawRectangle;
	
	// Move support
	public int velocityX, velocityY;
	private int centerX, centerY;
	private int windowWidth, windowHeight;
	private int speed = 0;

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Ball(int windowWidth, int windowHeight, int level) {
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		centerX = windowWidth/10;
		centerY = windowHeight/2;
		speed = 3 + level;
		velocityX = -speed;
		velocityY = -speed;
		drawRectangle = new Rectangle(centerX-width/2, centerY-height/2, width, height);
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillOval(centerX - width / 2, centerY - height/2, width, height);
	}
	
	public void update() {
		
		// Check if the ball reaches sides of the screen and change the direction if it does
		if (drawRectangle.getMinX()<=0 || drawRectangle.getMaxX()>=windowWidth) {
			velocityX *= -1;
		}
		if (drawRectangle.getMinY()<=0 || drawRectangle.getMaxY()>=windowHeight) {
			velocityY *= -1;
		}
		
		// Update the position
		centerX += velocityX;
		drawRectangle.x += velocityX;
		centerY += velocityY;
		drawRectangle.y += velocityY;
		
	}
	
	public Rectangle getRectangle() {
		return drawRectangle;
	}

}
