import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class Window implements Runnable {
	
	public JFrame window;
	public Canvas canvas;
	public JButton button;
	public BufferStrategy bufferStrategy;
	
	public int width;
	public int height;
	
	public double xMouse;
	public double yMouse;
	
	public boolean running;
	public boolean freeze;
	
	List<Point> points = new ArrayList<Point>();
	List<Point> toBeRendered = new ArrayList<Point>();
	
	public QuadTree quadtree;
	
	public Window(Dimension dimension) {
		
		width = (int) dimension.getWidth();
		height = (int) dimension.getHeight();
		window = new JFrame("Quadtree visualization");
		
		freeze = false;
		
		
		// to prevent lag on all machines when drawing shit.
		// images can be stored in different formats on different machines. 
		// if the formats matches how the screen draws the image, it will be fast
		// if not, the it needs to do a conversion before drawing and makes it slow as shit
		
		// taken from: https://stackoverflow.com/questions/658059/graphics-drawimage-in-java-is-extremely-slow-on-some-computers-yet-much-faster/659533#659533
		GraphicsConfiguration graphicsConfiguration = GraphicsEnvironment
				.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice()
				.getDefaultConfiguration();
		
		canvas = new Canvas(graphicsConfiguration);
		canvas.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
            	xMouse = MouseInfo.getPointerInfo().getLocation().getX() - canvas.getLocationOnScreen().getX();
            	yMouse = MouseInfo.getPointerInfo().getLocation().getY() - canvas.getLocationOnScreen().getY();
            	
                Point point = new Point(xMouse, yMouse);
                points.add(point);

    			quadtree.insert(point);
            }
            
		});
		
		button = new JButton("toggle freeze");
		button.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
            	freeze = !freeze;
            }
		});
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().setPreferredSize(new Dimension(width, height));
		
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setSize(new Dimension(width, height));
		
		window.setLayout(new BorderLayout());
		window.add(canvas, BorderLayout.CENTER);
		window.add(button, BorderLayout.NORTH);
		window.pack();
		window.setVisible(true);
		canvas.setVisible(true);
		
		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();
		
		quadtree = new QuadTree(width, height);
		
	}
	
	public void render() {
		
		Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
		
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, width, height);
		
		quadtree.render(graphics);
		
		for(Point point : toBeRendered) {
			point.render(graphics);
		}
		
		graphics.dispose();
		
		if(!bufferStrategy.contentsLost()) {
			bufferStrategy.show();
		}
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(running) {
			transfer();
			movePoints();
			bouncePoints();
			render();
		}
		
	}
	
	public void start() {
		running = true;
		new Thread(this).start();
	}
	
	// prevents concurrent modification exception error
	// an error that happens when you modifying an element in a list while iterating it
	public void transfer() {
		if(points.size() == 0) {
			return;
		}
		
		toBeRendered.clear();
		
		for(int i = 0; i < points.size(); i++) {
			toBeRendered.add(points.get(i));
		}
	}
	
	public void bouncePoints() {
		
		// checking if they ever go outta bounds
		for (Point point : toBeRendered) {
			
			if(point.x <= 0 || point.x >= width) {
				point.xVelocity *= -1;
			}
			
			if(point.y <= 0 || point.y >= height) {
				point.yVelocity *= -1;
			}
		}
		
	}
	
	public void movePoints() {
		
		if(freeze) {
			return;
		}
		
		// clear the quadtree
		quadtree = new QuadTree(width, height);
		
		// move the points, then insert to the tree
		for (Point point : toBeRendered) {
			point.update();
			quadtree.insert(point);
		}
	}


}
