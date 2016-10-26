package film_service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.json.*;
import org.skyscreamer.jsonassert.JSONAssert;

public class DfFilmServletTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testServlet() throws Exception{
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		fail("Not yet implemented");
		
		
	}
	
	public void testJsonFilter() throws JSONException{
		JSONObject testObject = new JSONObject("{\"actor_1\":\"Siddarth\",\"actor_2\":\"Nithya Menon\",\"actor_3\":\"Priya Anand\",\"director\":\"Jayendra\",\"locations\":\"Epic Roasthouse (399 Embarcadero)\",\"production_company\":\"SPI Cinemas\",\"release_year\":\"2011\",\"title\":\"180\",\"writer\":\"Umarji Anuradha, Jayendra, Aarthi Sriram, & Suba \"}");
		JSONArray input = new JSONArray();
		input.put(testObject);
		
		DfFilmServlet servlet = new DfFilmServlet();
		String[] properties = {"locations","title"};
		JSONArray result = servlet.reduceProperties(input, properties);
		
		JSONObject expectedObject = new JSONObject("{\"locations\":\"Epic Roasthouse (399 Embarcadero)\",\"title\":\"180\"}");
		JSONArray expected = new JSONArray();
		expected.put(expectedObject);
		
		JSONAssert.assertEquals(expected, result, true);
	}

}
