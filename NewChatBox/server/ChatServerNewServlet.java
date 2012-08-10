package com.miao.chatserver;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.util.logging.Level;

import javax.servlet.ServletInputStream;
import javax.servlet.http.*;

import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;




@SuppressWarnings("serial")
public class ChatServerNewServlet extends HttpServlet {
	
	
	private long timeStamp = 0;

	private static String message = "Error during Servlet processing";
	private final static int LIMIT = 50;
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doPost(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException{
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		
		try {
            int len = req.getContentLength();
            if (len < 1){
                //resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().print("<html> <body>");
                resp.getWriter().print(message);
                resp.getWriter().print("</body> </html>");
                resp.getWriter().close();
                return;
            }
            byte[] input = new byte[len];
            
            ServletInputStream sin = req.getInputStream();
            int c, count = 0 ;
            while ((c = sin.read(input, count, input.length-count)) != -1) {
                count +=c;
            }
            sin.close();
         
            String inString = new String(input);
            int index = inString.indexOf("t=");
            if (index == -1) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().print("<html> <body>");
                resp.getWriter().print(message);
                resp.getWriter().print("</body> </html>");
                resp.getWriter().close();
                return;
            }
            char type = inString.charAt(index+"t=".length());
            String value = "";
            switch (type){
            case 't':
            	value += timeStamp;
            	break;
            
            case 'm':
            	int start = inString.indexOf("message=");
            	if (start == -1){
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().print(message);
                    resp.getWriter().close();
                    return;
            	} else {
            		start += "message=".length();
            		value = inString.substring(start)+"<Msg>";
            		timeStamp ++;
            		syncCache.put(timeStamp, value);
            		if (syncCache.contains(timeStamp - LIMIT)){
            			syncCache.delete(timeStamp - LIMIT);
            		}
            		return ;
            	}
 
            
            case 'r':
            	
            	StringBuffer sb = new StringBuffer ();
            	for (long i = timeStamp - 49; i <= timeStamp; i++){
            		Object temp =  syncCache.get(i);
            		if (temp == null){
            			continue;
            		}
            		sb.append ((String)temp);
            	}
            	
            	value = sb.toString();
            	break;
            
            default:
            	break;
            }
             
            //decode application/x-www-form-urlencoded string
            String decodedString = URLDecoder.decode(value, "UTF-8");
             
            //reverse the String
            
             
            // set the response code and write the response data
            resp.setStatus(HttpServletResponse.SC_OK);
            OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream());
             
            writer.write(decodedString);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            try{
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().print(e.getMessage());
                resp.getWriter().close();
            } catch (IOException ioe) {
            }
        }
		
	}
}
