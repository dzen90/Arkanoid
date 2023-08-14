import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Brick {
	
	// Draw support
	private int width = 50;
	private int height = 50;
	private int centerX, centerY;
	private Rectangle drawRectangle;
	private boolean active;
	

	public boolean isActive() {
		return active;
	}

	public void destroy() {
		active = false;
	}

	public Brick(int x, int y) {
		this.centerX = x;
		this.centerY = y;
		active = true;
		drawRectangle = new Rectangle(centerX - width/2, centerY - height/2, width, height);
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(centerX - width/2, centerY - height/2, width, height);
	}
	
	public Rectangle getRectangle() {
		return drawRectangle;
	}

}
