package ut.university.projectAI.networking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.net.URL;

public class RequestPerformerImpl implements RequestPerformer{
	
	private static RequestPerformerImpl requestSender = new RequestPerformerImpl();
	
	private RequestPerformerImpl(){}
	
	public static RequestPerformerImpl instance(){
		return requestSender;
	}
	
	
	public String performRequest(String uri){
		String response = "";
		try {
			URL url = new URL(uri);
			URLConnection connection = url.openConnection();
			try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				String line;
				while((line = br.readLine()) != null){
					response += line;
				}
			}
		} catch (Exception skip) {
			//This is mostly ok
			skip.printStackTrace();
		}
		return response;
	}
}
