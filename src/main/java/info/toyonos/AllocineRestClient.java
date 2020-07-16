package info.toyonos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.json.JsonObject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.glassfish.jersey.jsonp.JsonProcessingFeature;

public class AllocineRestClient
{
	private static final String ENDPOINT = "http://api.allocine.fr/rest/v3";
	
	private String partnerId;
	private String secretKey;
	private WebTarget webTarget;
	
	public AllocineRestClient(String partnerId, String secretKey)
	{
		this.partnerId = partnerId;
		this.secretKey = secretKey;
		webTarget = ClientBuilder.newClient().register(JsonProcessingFeature.class).target(ENDPOINT);
	}

	private JsonObject doGet(String method, BasicNameValuePair... params)
	{
		WebTarget wt = webTarget.path(method);

		List<BasicNameValuePair> paramsList = new ArrayList<>(Arrays.asList(params));
		paramsList.add(new BasicNameValuePair("partner", partnerId));
		paramsList.add(new BasicNameValuePair("format", "json"));
		paramsList.add(new BasicNameValuePair("sed", new SimpleDateFormat("yyyyMMdd").format(new Date())));
		paramsList.add(new BasicNameValuePair("sig",
			new String(Base64.getEncoder().encode(DigestUtils.sha1(
				String.format("%s%s%s", method, URLEncodedUtils.format(paramsList, "UTF-8"), secretKey)
			)))
		));

		for (BasicNameValuePair p : paramsList) wt = wt.queryParam(p.getName(), p.getValue());

		return wt.request(MediaType.APPLICATION_JSON_TYPE).get(JsonObject.class);
	}
	
	public JsonObject searchMovies(String query)
	{
		return doGet(
			"search",
			new BasicNameValuePair("q", query),
			new BasicNameValuePair("filter", "movie")
		);
	}
	
	public JsonObject searchTheaters(String zip, int radius)
	{
		return doGet(
			"showtimelist",
			new BasicNameValuePair("zip", zip),
			new BasicNameValuePair("radius", String.valueOf(radius)),
			new BasicNameValuePair("profile", "medium")
		);
	}
	
	// TODO more methods
}
