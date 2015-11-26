import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Process {
	
	public static void main(String [] args){
		getResultPage("bone%20disease", "10");

		//double rwi = 

		//
	}

	static void getResultPage(String query, String numberOfDocs){
        
		try{
		    
			String line = "http://136.206.115.117:8080/IRModelGenerator/SearchServlet?query="+query+"&simf=BM25&k=1.2&b=0.75&numwanted="+numberOfDocs+"";
		    
			URL url = new URL(line);

			URLConnection connection = url.openConnection();
	       	BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String inputLine;

			while ((inputLine = in.readLine()) != null) 
				System.out.println(inputLine);

		in.close();
		
		}catch(MalformedURLException malformedurlexception){
		    malformedurlexception.printStackTrace();
		}catch(IOException ioexception){
		    ioexception.printStackTrace();
		}catch(Exception ioexception){
		    ioexception.printStackTrace();
		}
    	}
}
