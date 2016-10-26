package film_service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.*;

/**
 * Servlet implementation class DfFilmServlet
 */
@WebServlet("/DfFilmServlet")
public class DfFilmServlet extends HttpServlet {
	
	private static final String SF_FILM_DB_URL = "https://data.sfgov.org/resource/wwmu-gmzc.json";
	private static final long serialVersionUID = 1L;


	/**
	 * Reads from a prespecified url, parses and filters the content and responds with a JSON compatible string 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		URL url = new URL(SF_FILM_DB_URL);
		
		String readString = readStringFromURL(url);
		String outputString = new String();
		try{
			JSONArray jsonInput = new JSONArray(readString);
			String[] properties = {"locations","title"};
			JSONArray jsonOutput = reduceProperties(jsonInput, properties);
			outputString = jsonOutput.toString();
			
		}catch(JSONException e){
			System.err.println("Error parsing JSONArray: " + e.getMessage());
			outputString = "There was an error reading from the JSON source";
		}
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.print(outputString);
		out.flush();
		out.close();
		
		
	}

	/**
	 * Reads a string from an URL and returns the string
	 * @param url URL from which to read
	 * @return String read from the URL
	 */
	private String readStringFromURL(URL url){
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[2048];
			while((read = reader.read(chars)) != -1){
				buffer.append(chars, 0, read);
			}
			return buffer.toString();
		} catch (IOException e) {
			System.err.println("IOException while trying to read from url: " + e.getMessage());
			return new String();
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					System.err.println("Could not close reader: " + e.getMessage());
				}
			}
		}
	}
	
	/**
	 * Reduces the JSONArray to contain only the desired properties
	 * @param inputArray A JSONArray
	 * @param properties A String Array listing all the desired properties, which should not be removed from the array.
	 * @return Returns a JSONArray containing only the required properties
	 */
	public JSONArray reduceProperties(JSONArray inputArray, String... properties ){
		JSONArray result = new JSONArray();
		if(inputArray != null){
			for(int i = 0; i < inputArray.length(); i++){
				JSONObject obj;
				try {
					obj = inputArray.getJSONObject(i);
					Iterator keyIterator = obj.keys();
					while(keyIterator.hasNext()){
						String key = (String)keyIterator.next();
						boolean toDelete = true;
						if(!Arrays.asList(properties).contains(key)){
							keyIterator.remove();
						}
						
					}
					result.put(obj);
				} catch (JSONException e) {
					System.err.println("JSON input was not correct. Error: " + e.getMessage());
				}
				
			}
		}
		
		return result;
	}

}
