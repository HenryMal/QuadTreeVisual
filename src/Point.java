import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Point {
	
	public double x;
	public double y;
	
	public double xVelocity;
	public double yVelocity;
	
	public double size;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
		
		this.xVelocity = 0.1;
		this.yVelocity = 0.1;
		
		if (Math.random() > 0.5) xVelocity *= -1;
		if (Math.random() > 0.5) yVelocity *= -1;
		
		this.size = 10.0;
	}
	
	public void render(Graphics graphics) {
		
		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		Ellipse2D point = new Ellipse2D.Double(x - size / 2, y - size / 2, size, size);
		
		graphics2D.setColor(new Color(255, 255, 225));
		graphics2D.fill(point);
		
	}
	
	public void update() {
		x += xVelocity;
		y += yVelocity;
	}
	

}
