
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
	Date date;
	String name;
	String message;
	public static final SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	
	

	public void setDate (Date date){
		this.date = date;
	}
	
	public void setName (String name){
		this.name = name;
	}
	
	public void setMessage (String message){
		this.message =message;
	}
	
	public String getMessage (){
		return message;
	}
	
	public Date getDate (){
		return this.date;
	}
	public String getDateString (){
		return format.format(date);
	}
	public String getName(){
		return this.name;
	}
}
