package film_service;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;
import static org.mockito.Mockito.*;

import org.json.*;
import org.skyscreamer.jsonassert.JSONAssert;

public class DfFilmServletTest extends TestCase {



	public void testServlet() throws Exception{
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		DfFilmServlet servlet = new DfFilmServlet();
		DfFilmServlet spyServlet = spy(servlet);
		
		String input = "[{\"actor_1\":\"Siddarth\",\"actor_2\":\"Nithya Menon\",\"actor_3\":\"Priya Anand\",\"director\":\"Jayendra\",\"locations\":\"Epic Roasthouse (399 Embarcadero)\",\"production_company\":\"SPI Cinemas\",\"release_year\":\"2011\",\"title\":\"180\",\"writer\":\"Umarji Anuradha, Jayendra, Aarthi Sriram, & Suba \"}]";
		doReturn(input).when(spyServlet).readStringFromURL(any());
		
		StringWriter writer = new StringWriter();
		
		when(response.getWriter()).thenReturn(new PrintWriter(writer));
		String expected = "[{\"locations\":\"Epic Roasthouse (399 Embarcadero)\",\"title\":\"180\"}]";
		
		spyServlet.doGet(request, response);
		
		writer.flush();
		
		String result = writer.toString();
		assertEquals(expected,result);
		
		
		
	}
	
	public void testJsonFilter() throws JSONException{
		JSONObject testObject = new JSONObject("{\"actor_1\":\"Siddarth\",\"actor_2\":\"Nithya Menon\",\"actor_3\":\"Priya Anand\",\"director\":\"Jayendra\",\"locations\":\"Epic Roasthouse (399 Embarcadero)\",\"production_company\":\"SPI Cinemas\",\"release_year\":\"2011\",\"title\":\"180\",\"writer\":\"Umarji Anuradha, Jayendra, Aarthi Sriram, & Suba \"}");
		JSONArray input = new JSONArray();
		input.put(testObject);
		
		DfFilmServlet servlet = new DfFilmServlet();
		String[] properties = {"locations","title"};
		String titleQuery = "";
		JSONArray result = servlet.reduceProperties(input, titleQuery, properties);
		
		JSONObject expectedObject = new JSONObject("{\"locations\":\"Epic Roasthouse (399 Embarcadero)\",\"title\":\"180\"}");
		JSONArray expected = new JSONArray();
		expected.put(expectedObject);
		
		JSONAssert.assertEquals(expected, result, true);
	}
	
	
	
	public void testJsonFilterWithTitleQuery() throws JSONException{
		JSONObject testObject = new JSONObject("{\"actor_1\":\"Siddarth\",\"actor_2\":\"Nithya Menon\",\"actor_3\":\"Priya Anand\",\"director\":\"Jayendra\",\"locations\":\"Epic Roasthouse (399 Embarcadero)\",\"production_company\":\"SPI Cinemas\",\"release_year\":\"2011\",\"title\":\"180\",\"writer\":\"Umarji Anuradha, Jayendra, Aarthi Sriram, & Suba \"}");
		JSONArray input = new JSONArray();
		input.put(testObject);
		
		DfFilmServlet servlet = new DfFilmServlet();
		String[] properties = {"locations","title"};
		
		String titleQueryOK = "18";
		JSONArray resultOK = servlet.reduceProperties(input, titleQueryOK, properties);
		
		String titleQueryNOK = "78";
		JSONArray resultNOK = servlet.reduceProperties(input, titleQueryNOK, properties);
		
		JSONObject expectedObject = new JSONObject("{\"locations\":\"Epic Roasthouse (399 Embarcadero)\",\"title\":\"180\"}");
		JSONArray expected = new JSONArray();
		expected.put(expectedObject);
		
		JSONAssert.assertEquals(expected, resultOK, true);
		JSONAssert.assertNotEquals(expected, resultNOK, true);
	}

}
