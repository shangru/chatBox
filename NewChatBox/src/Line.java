import java.awt.Color;
import java.awt.Graphics;


public class Line {
	Point start;
	Point end;
	Color color;
	public Line (Point start, Point end, Color color){
		this.start  = start;
		this.end = end;
		this.color = color;
		
	}
	
	public void paint(Graphics g){
		g.drawLine(start.x, start.y, end.x, end.y);
	}
}
