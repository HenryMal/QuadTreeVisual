import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Path2D.Double;
import java.util.ArrayList;
import java.util.List;

// the class has no code cuz its a wrapper for the entire tree.
// the complicated shit is already handled in the node class

// idea taken from: 
// https://en.wikipedia.org/wiki/Quadtree
public class QuadTree {
	
	QuadTreeNode root;
	
	public double width;
	public double height;
	
	public QuadTree(double width, double height) {
		
		this.width = width;
		this.height = width;
		root = new QuadTreeNode(0, 0, width, height);
	}
	
	public void insert(Point point) {
		root.insert(point);
	}
	
	public void render(Graphics graphics) {
		
		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setColor(Color.RED);
		graphics2D.setStroke(new BasicStroke(2));
		drawQuadTree(graphics2D, root);
		
	}
	
	public void drawQuadTree(Graphics2D graphics, QuadTreeNode node) {
		
		if(node == null) {
			return;
		}
		
		graphics.draw(new Rectangle2D.Double(node.x, node.y, node.width, node.height));
		
		drawQuadTree(graphics, node.topLeft);
		drawQuadTree(graphics, node.topRight);
		drawQuadTree(graphics, node.bottomLeft);
		drawQuadTree(graphics, node.bottomRight);
		
	}

	
}
