import java.text.ParseException;
import java.util.Date;


public class MessageHelper {
	private static Message resolveOneMessage (String receivedMessage){
		//System.out.println("???"+receivedMessage);
		int start = receivedMessage.indexOf("<date>=") +"<date>=".length() ;
		int end = receivedMessage.indexOf("<edate>");
		String dataString = receivedMessage.substring(start, end);


		Date parsed;
        try {
            parsed = Message.format.parse(dataString);
        }
        catch(ParseException pe) {
        	System.err.println("unrecognized date");
            return null;
        }
        
        
        Message m = new Message();
        m.setDate(parsed);
        
        start = receivedMessage.indexOf("<name>=") + "<name>=".length();
        end = receivedMessage.indexOf("<ename>");
        
        String name = receivedMessage.substring(start, end);
        m.setName(name);
        
        
        start = receivedMessage.indexOf("<cnt>=") + "<cnt>=".length();
        end = receivedMessage.indexOf("<ecnt>");
        
        String message = receivedMessage.substring(start, end);
        m.setMessage(message);
        
        return m;
	}
	
	public static Message [] resolveAllMessage (String receivedMessage){
		if (receivedMessage.length() < 10){
			return null;
		}
		//System.out.println("11 "+receivedMessage);
		String [] splited = receivedMessage.split("<Msg>");
		Message [] msgs = new Message [splited.length];
		for (int i = 0 ; i < splited.length; i++){
			
			msgs[i] = resolveOneMessage (splited[i]);
		}
		return msgs;
	}
	
	public static String packing (String name ,String rawMessage){
		StringBuffer sb = new StringBuffer ();
		Date date= new Date();

		String temp = Message.format.format(date);
		sb.append("<date>="+temp + "<edate>\n");
		sb.append("<name>="+name + "<ename>\n");
		sb.append("<cnt>="+rawMessage + "<ecnt>\n");
		return sb.toString();
	}
}
