import java.awt.geom.*;
import java.util.*;

public class QuadTreeNode {
	
	public static final int MAX_POINTS = 4;
	
	// define rectangle
	// these are known as NODES
	public double x;
	public double y;
	public double width;
	public double height;
	
	// define quadrants
	public QuadTreeNode topLeft;
	public QuadTreeNode topRight;
	public QuadTreeNode bottomLeft;
	public QuadTreeNode bottomRight;
	
	// contents of each node
	public List<Point> points;
	
	public QuadTreeNode(double x, double y, double width, double height) {
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		points = new ArrayList<>();
		
	}
	
	public void insert(Point point) {
		
		// check if the node actually contains the point. if not, do nothing
		// this is to make sure we put it in the right quadrant
		if(!contains(point)) {
			return;
		}
		
		// if there is still some points we can add and if it has no children, add a point
		if(points.size() < MAX_POINTS && topLeft == null) {
			points.add(point);
		}
		
		else {
			
			// if its at max capacity, but there is no children to put the point in, then
			// subdivide
			if(topLeft == null) {
				subdivide();
			}
			
			// we already do have children. (alr been subdivided). we just need to insert the point
			// in the correct node
			topLeft.insert(point);
			topRight.insert(point);
			bottomLeft.insert(point);
			bottomRight.insert(point);

		}
		
	}
	
	public void subdivide() {
		
		// subdivided size
		double newWidth = width / 2;
		double newHeight = height / 2;
		
		topLeft = new QuadTreeNode(x, y, newWidth, newHeight);
		topRight = new QuadTreeNode(x + newWidth, y, newWidth, newHeight);
		bottomLeft = new QuadTreeNode(x, y + newHeight, newWidth, newHeight);
		bottomRight = new QuadTreeNode(x + newWidth, y + newHeight, newWidth, newHeight);
		
		// now we need to insert the points in the correct nodes, whch is alr handled in the insert method
		for(Point point : points) {
			
			topLeft.insert(point);
			topRight.insert(point);
			bottomLeft.insert(point);
			bottomRight.insert(point);
			
		}
		
		// clear the contents of the parent, since we dont want shit there no more
		points.clear();
		
	}	
	
	public boolean contains(Point point) {
		return point.x >= x &&
				point.y >= y &&
				point.x < x + width &&
				point.y < y + height;
	}
	
}


