
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Calendar;
import javax.swing.JPanel;


public class Clock extends JPanel{
	
	private static Line [] threeLines;
	private static Point origin = new Point (20,20);
	private static int radix = 15;
	boolean done;
	public Clock (){
		done = false;

		threeLines = new Line[3];
		threeLines[0] = new Line(origin, origin, Color.BLACK);
		threeLines[1] = new Line(origin, origin, Color.darkGray);
		threeLines[2] = new Line(origin, origin, Color.RED);
		this.setOpaque(false);
		RefreshThread ref = new RefreshThread();
		ref.start();

	}
	
	protected void paintComponent (Graphics g){
		super.paintComponent(g);
		origin.x = this.getSize().height/2 -2 > 0 ? this.getSize().height/2 -2 : this.getSize().height/2;
		origin.y = this.getSize().height/2 -2 > 0 ? this.getSize().height/2 -2 : this.getSize().height/2;
		radix = origin.x > 2 ? origin.x-2:origin.x;
		Graphics2D newg = (Graphics2D) g;
		newg.setColor(Color.gray);
		newg.drawArc(origin.x-radix, origin.y - radix, radix * 2, radix * 2, 0, 360);
		int fontSize = radix > 8 ? radix >>3 : 1;
		Font font = new Font("Arial", Font.PLAIN, fontSize);
		newg.setFont(font);
		newg.drawString(" 6", origin.x, origin.y + radix);
		newg.drawString(" 3",  origin.x - fontSize + radix, origin.y);
		newg.drawString("12",  origin.x, origin.y - radix+fontSize);
		newg.drawString(" 9",  origin.x - radix, origin.y);
		
		for (int i = 0 ; i < 3 ;i ++){
			newg.setColor(threeLines[i].color);
			newg.setStroke(new BasicStroke(1+ 3-i*2));
			newg.drawLine(threeLines[i].start.x, threeLines[i].start.y, threeLines[i].end.x, threeLines[i].end.y);
		}
	}
	
	private class RefreshThread extends Thread {
		public void run (){
			while (!done){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {

				}
				refresh();
			
			}
			
		}
		private void refresh (){
			Calendar rightNow = Calendar.getInstance();
			
			ThreeAngles angles = LogicHelper.getAngles(rightNow);
			int temp = radix > 5 ? radix -5 : radix ;
			Point endHour = LogicHelper.findEndPoint(origin, angles.angleHr, temp);
			Point endMin = LogicHelper.findEndPoint(origin, angles.angleMin, temp);
			Point endSec = LogicHelper.findEndPoint(origin, angles.angleSec, temp);
			threeLines[0].end = endHour;
			threeLines[1].end = endMin;
			threeLines[2].end = endSec;

			repaint();
			
		}
	}
	
}
