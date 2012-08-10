import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class Connector {
	//public static final String urlString = "http://localhost:8888/chatservernew";
	public static final String urlString = "http://ting-yun.appspot.com/chatservernew";
	public static long getTimeStamp (){
        try{
            
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);

            OutputStreamWriter out = new OutputStreamWriter(
                                             connection.getOutputStream());
            out.write("t=t");
            out.close();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //System.out.println("well connected");
            } else {
                // Server returned HTTP error code.
            }
            BufferedReader in = new BufferedReader(
                                        new InputStreamReader(
                                        connection.getInputStream()));
            
            String decodedString = in.readLine();

            in.close();
            return Long.parseLong(decodedString);
        } catch (Exception e){
        	System.out.println(e.getMessage());
        }
		return 0;
	}
	
	public static String getMessage (){
        try{

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);

            OutputStreamWriter out = new OutputStreamWriter(
                                             connection.getOutputStream());
            out.write("t=r");
            out.close();

            BufferedReader in = new BufferedReader(
                                        new InputStreamReader(
                                        connection.getInputStream()));
            StringBuffer sb = new StringBuffer ();
            String decodedString;
            while ((decodedString = in.readLine()) != null) {
            	sb.append (decodedString);
            }
            in.close();
            return sb.toString();
        } catch (Exception e){
        	System.out.println(e.getMessage());
        }
        return null;
	}
	
	public static boolean sendMessage (String message){
        try{
            String toSend = URLEncoder.encode(message, "UTF-8");

            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);

            OutputStreamWriter out = new OutputStreamWriter(
                                             connection.getOutputStream());
            out.write("t=m" + " message="+toSend);
            out.close();

            if (((HttpURLConnection) connection).getResponseCode()!=HttpURLConnection.HTTP_OK){
            	return false;
            }
            	
        } catch (Exception e){
        	System.out.println(e.getMessage());
        }
        return true;
	}
}
