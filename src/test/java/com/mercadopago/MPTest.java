package com.mercadopago;

import static org.junit.Assert.*;
import org.junit.Test;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class MPTest {

	MP mp = new MP("CLIENT_ID", "CLIENT_SECRET");

	@Test
	public void testGetPreference() throws JSONException, Exception {

		JSONObject createPreferenceResult = mp.createPreference("{'items':[{'title':'Prueba','quantity':1,'currency_id':'ARS','unit_price':10.5}]}");

		String createdPreferenceId = createPreferenceResult.getJSONObject("response").getString("id");

		JSONObject getPreferenceResult = mp.getPreference(createdPreferenceId);

		assertEquals(getPreferenceResult.getInt("status"), 200);

		JSONObject obtainedPreference = (JSONObject) getPreferenceResult.getJSONObject("response").getJSONArray("items").get(0);

		assertEquals(obtainedPreference.getString("title"), "Prueba");
		assertEquals(obtainedPreference.getInt("quantity"), 1);
		assertEquals(obtainedPreference.getString("currency_id"), "ARS");
		assertEquals(obtainedPreference.getDouble("unit_price"), 10,5d);
	}

	@Test
	public void testCreatePreference() throws JSONException, Exception {

		JSONObject createPreferenceResult = mp.createPreference("{'items':[{'title':'Prueba','quantity':1,'currency_id':'ARS','unit_price':10.5}]}");

		assertEquals(createPreferenceResult.getInt("status"), 201);
		
		JSONObject createdPreference = (JSONObject) createPreferenceResult.getJSONObject("response").getJSONArray("items").get(0);

		assertEquals(createdPreference.getString("title"), "Prueba");
		assertEquals(createdPreference.getInt("quantity"), 1);
		assertEquals(createdPreference.getString("currency_id"), "ARS");
		assertEquals(createdPreference.getDouble("unit_price"), 10,5d);
	}

	@Test
	public void testUpdatePreference() throws JSONException, Exception {

		JSONObject createPreferenceResult = mp.createPreference("{'items':[{'title':'Prueba','quantity':1,'currency_id':'ARS','unit_price':10.5}]}");

		String createdPreferenceId = createPreferenceResult.getJSONObject("response").getString("id");

		JSONObject updatePreferenceResult = mp.updatePreference(createdPreferenceId, "{'items':[{'title':'Modified','quantity':2,'unit_price':2.2}]}");

		assertEquals(updatePreferenceResult.getInt("status"), 200);

		JSONObject getPreferenceResult = mp.getPreference(createdPreferenceId);

		assertEquals(getPreferenceResult.getInt("status"), 200);

		JSONObject obtainedPreference = (JSONObject) getPreferenceResult.getJSONObject("response").getJSONArray("items").get(0);

		assertEquals(obtainedPreference.getString("title"), "Modified");
		assertEquals(obtainedPreference.getInt("quantity"), 2);
		assertEquals(obtainedPreference.getDouble("unit_price"), 2,2d);
	}
}
