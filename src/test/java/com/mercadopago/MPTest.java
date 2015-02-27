package com.mercadopago;

import org.codehaus.jettison.json.JSONObject;

import org.junit.Test;
import static org.junit.Assert.*;

public class MPTest {

	private final String CLIENT_ID  = "CLIENT_ID";
	private final String CLIENT_SECRET = "CLIENT_SECRET";
	private final String LL_ACCESS_TOKEN = "LONG_LIVE_ACCESS_TOKEN";

	@Test
	public void configCredentials() throws Exception {
		assertNotEquals("You must configure a valid CLIENT_ID for tests", CLIENT_ID, "CLIENT_ID");
		assertNotEquals("You must configure a valid CLIENT_SECRET for tests", CLIENT_SECRET, "CLIENT_SECRET");
	}

	@Test
	public void accessToken() throws Exception {
		MP mp = new MP(CLIENT_ID, CLIENT_SECRET);

		String at = mp.getAccessToken();

		assertNotNull(at);
		assertNotEquals(at, "");
	}

	@Test
	public void llAccessToken() throws Exception {
		MP mp = new MP(LL_ACCESS_TOKEN);

		assertEquals(mp.getAccessToken(), LL_ACCESS_TOKEN);
	}

	@Test
	public void createPreference() throws Exception {
		MP mp = new MP (CLIENT_ID, CLIENT_SECRET);

		JSONObject preference = mp.createPreference("{'items':[{'title':'sdk-java','quantity':1,'currency_id':'ARS','unit_price':10.5}]}");

		assertEquals (preference.getInt("status"), 201);
	}

	@Test
	public void proxy() throws Exception {
		MP mp = new MP (CLIENT_ID, CLIENT_SECRET);

		JSONObject sites = mp.get("/sites");

		assertEquals (sites.getInt("status"), 200);

		mp.setProxy("http://127.0.0.1", "80");

		sites = mp.get("/sites");

		assertEquals (sites.getInt("status"), 500);

		mp.setProxy(null);
	}
}