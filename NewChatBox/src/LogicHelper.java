import java.util.Calendar;

public class LogicHelper {
	
	public static Point findEndPoint (Point origin, double angle, int radix){
		int v = (int)(Math.cos(angle - Math.PI/2) * radix);
		int h = (int)(Math.sin(angle - Math.PI/2) * radix);
		
		Point end = new Point (origin.x + v, origin.y+h);
		
		return end;
	}
	
	public static ThreeAngles getAngles (Calendar rightNow){

		int hour = rightNow.get(Calendar.HOUR);
		int min  = rightNow.get(Calendar.MINUTE);
		int sec  = rightNow.get(Calendar.SECOND);
		double angSec = ((double) sec) * 2 * Math.PI /60;
		
		System.out.println(((double) sec) * 360 /60);
		System.out.println(sec);
		
		double angMin = ((double) min) * 2 * Math.PI /60 + angSec/60;
		
		double angHr = ((double) hour * 5) * 2 * Math.PI /60 + angMin /60;
		
		
		ThreeAngles angles = new ThreeAngles (angHr, angMin, angSec);
		return angles;
	}
}
