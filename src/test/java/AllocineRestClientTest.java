import javax.json.JsonValue;

import org.junit.Assert;
import org.junit.Test;

import info.toyonos.AllocineRestClient;

public class AllocineRestClientTest
{
	private AllocineRestClient client = new AllocineRestClient("100ED1DA33EB", "1a1ed8c1bed24d60ae3472eed1da33eb");
	
	@Test
	public void searchMoviesTest()
	{
		Assert.assertEquals(
			"Inception",
			client.searchMovies("inception")
				.getJsonObject("feed").getJsonArray("movie").getJsonObject(0).getString("originalTitle")
		);
	}
	
	@Test
	public void searchTheatersTest()
	{
		Assert.assertTrue(
			client.searchTheaters("44000", 5)
				.getJsonObject("feed").getJsonArray("theaterShowtimes")
				.stream()
				.map(JsonValue::asJsonObject)
				.map(o -> o.getJsonObject("place"))
				.map(o -> o.getJsonObject("theater"))
				.map(o -> o.getString("name"))
				.anyMatch(n -> n.equals("Katorza"))
		);
	}
}
