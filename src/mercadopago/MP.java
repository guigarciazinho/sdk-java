package mercadopago;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.client.apache.ApacheHttpClient;

/**
 * MercadoPago Integration Library
 * Access MercadoPago for payments integration
 *
 * @date 2012/03/29
 * @author hcasatti
 *
 */
public class MP {
	public static final String version = "0.1.5";

	private final String client_id;
	private final String client_secret;
	private JSONObject access_data = null;
	
	public MP (final String client_id, final String client_secret) {
		this.client_id = client_id;
		this.client_secret = client_secret;
	}
	
	/**
	 * Get Access Token for API use
	 * @throws JSONException 
	 */
	public String getAccessToken () throws JSONException, Exception {
		HashMap<String, Object> appClientValues = new HashMap<String, Object>();
		appClientValues.put("grant_type", "client_credentials");
		appClientValues.put("client_id", this.client_id);
		appClientValues.put("client_secret", this.client_secret);
		
        	String appClientValuesQuery = this.buildQuery(appClientValues);

		JSONObject access_data = RestClient.post ("/oauth/token", appClientValuesQuery, RestClient.MIME_FORM);

		if(access_data.getInt("status") == 200) {
			this.access_data = access_data.getJSONObject("response");
			return this.access_data.optString("access_token");
		} else {
			throw new Exception(access_data.toString());
		}
	}
	
	/**
	 * Get information for specific payment
	 * @param id
	 * @return
	 * @throws JSONException
	 */
	public JSONObject getPaymentInfo (String id) throws JSONException, Exception {
		String accessToken;
		try {
			accessToken = this.getAccessToken ();
		} catch (Exception e) {
			JSONObject result = new JSONObject(e.getMessage());
			return result;
		}
		
		JSONObject paymentInfo = RestClient.get ("/collections/notifications/"+id+"?access_token="+accessToken);
		
		return paymentInfo;
	}
	
	/**
	 * Refund accredited payment
	 * @param id
	 * @return
	 * @throws JSONException
	 */
	public JSONObject refundPayment (String id) throws JSONException, Exception {
		String accessToken;
		try {
			accessToken = this.getAccessToken ();
		} catch (Exception e) {
			JSONObject result = new JSONObject(e.getMessage());
			return result;
		}

		JSONObject refundStatus = new JSONObject ();
		refundStatus.put("status", "refunded");
		
		JSONObject response = RestClient.put ("/collections/"+id+"?access_token="+accessToken, refundStatus);
		
		return response;
	}
	
	/**
	 * Cancel pending payment
	 * @param id
	 * @return
	 * @throws JSONException
	 */
	public JSONObject cancelPayment (String id) throws JSONException, Exception {
		String accessToken;
		try {
			accessToken = this.getAccessToken ();
		} catch (Exception e) {
			JSONObject result = new JSONObject(e.getMessage());
			return result;
		}

		JSONObject cancelStatus = new JSONObject ();
		cancelStatus.put("status", "cancelled");
		
		JSONObject response = RestClient.put ("/collections/"+id+"?access_token="+accessToken, cancelStatus);
		
		return response;
	}
	
	/**
	 * Search payments according to filters, with pagination
	 * @param filters
	 * @param offset
	 * @param limit
	 * @return
	 * @throws JSONException
	 */
	public JSONObject searchPayment (Map<String, Object> filters) throws JSONException, Exception {
		return this.searchPayment(filters, 0, 0);
	}
	public JSONObject searchPayment (Map<String, Object> filters, int offset, int limit) throws JSONException {
		return this.searchPayment(filters, Long.valueOf(offset), Long.valueOf(limit));
	}
	public JSONObject searchPayment (Map<String, Object> filters, Long offset, Long limit) throws JSONException {
		String accessToken;
		try {
			accessToken = this.getAccessToken ();
		} catch (Exception e) {
			JSONObject result = new JSONObject(e.getMessage());
			return result;
		}
		
		filters.put("offset", offset);
		filters.put("limit", limit);
		
		String filtersQuery = this.buildQuery (filters);
		
		JSONObject collectionResult = RestClient.get ("/collections/search?"+filtersQuery+"&access_token="+accessToken);
		return collectionResult;
	}

	/**
	 * Create a checkout preference
	 * @param preference
	 * @return
	 * @throws JSONException
	 */
	public JSONObject createPreference (String preference) throws JSONException, Exception {
		JSONObject preferenceJSON = new JSONObject (preference);
		return this.createPreference(preferenceJSON);
	}
	public JSONObject createPreference (Map<?, ?> preference) throws JSONException, Exception {
		JSONObject preferenceJSON = map2json (preference);
		return this.createPreference(preferenceJSON);
	}
	public JSONObject createPreference (JSONObject preference) throws JSONException, Exception {
		String accessToken;
		try {
			accessToken = this.getAccessToken ();
		} catch (Exception e) {
			JSONObject result = new JSONObject(e.getMessage());
			return result;
		}
		
		JSONObject preferenceResult = RestClient.post ("/checkout/preferences?access_token="+accessToken, preference);
		return preferenceResult;
	}
	
	/**
	 * Update a checkout preference
	 * @param string $id
	 * @param array $preference
	 * @return array(json)
	 * @throws JSONException 
	 */
	public JSONObject updatePreference (String id, String preference) throws JSONException, Exception {
		JSONObject preferenceJSON = new JSONObject (preference);
		return this.updatePreference(id, preferenceJSON);
	}
	public JSONObject updatePreference (String id, Map<?, ?> preference) throws JSONException, Exception {
		JSONObject preferenceJSON = map2json (preference);
		return this.updatePreference(id, preferenceJSON);
	}
	public JSONObject updatePreference (String id, JSONObject preference) throws JSONException, Exception {
		String accessToken;
		try {
			accessToken = this.getAccessToken ();
		} catch (Exception e) {
			JSONObject result = new JSONObject(e.getMessage());
			return result;
		}
		
		JSONObject preferenceResult = RestClient.put ("/checkout/preferences/"+id+"?access_token="+accessToken, preference);
		return preferenceResult;
	}
	
	/**
	 * Get a checkout preference
	 * @param id
	 * @return
	 * @throws JSONException 
	 */
	public JSONObject getPreference (String id) throws JSONException, Exception {
		String accessToken;
		try {
			accessToken = this.getAccessToken ();
		} catch (Exception e) {
			JSONObject result = new JSONObject(e.getMessage());
			return result;
		}
		
		JSONObject preferenceResult = RestClient.get ("/checkout/preferences/"+id+"?access_token="+accessToken);
		return preferenceResult;
	}
	
	/*****************************************************************************************************/
	private String buildQuery (Map<String, Object> params) {
		String[] query = new String[params.size()];
		int index = 0;
		for (String key : params.keySet()) {
			String val = String.valueOf(params.get(key) != null ? params.get(key) : "");
			try {
				val = URLEncoder.encode(val, "UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
			query[index++] = key+"="+val;
		}
			
		return StringUtils.join(query, "&");
	}
	
	private static class Util {
		public static <T> Object get (Map<T, T> map, T key, Object def) {
			return map.containsKey(key) ? map.get(key) : def;
		}
	}
	
	private static JSONObject map2json (Map<?, ?> preference) throws JSONException, Exception {
		JSONObject result = new JSONObject();

        for (Entry<?, ?> entry : preference.entrySet()) {
        	if (entry.getValue () instanceof Collection) {
        		result.put((String) entry.getKey(), map2json((Collection<?>)entry.getValue()));
        	} else if (entry.getValue() instanceof Map) {
        		result.put((String) entry.getKey(), map2json((Map<?, ?>)entry.getValue()));
        	} else {
        		result.put((String) entry.getKey(), entry.getValue());
        	}
        }

        return result;
	}

	private static JSONArray map2json (Collection<?> collection) throws JSONException, Exception {
		JSONArray result = new JSONArray();

        for (Object object : collection) {
        	if (object instanceof Map) {
        		result.put(map2json((Map<?, ?>)object));
        	} else {
        		result.put(object);
        	}
        }
        
        return result;
    }
	
	private static class RestClient {
		private static final String API_BASE_URL = "https://api.mercadolibre.com";
		public static final String MIME_JSON = "application/json";
		public static final String MIME_FORM = "application/x-www-form-urlencoded";
		
		private static JSONObject exec (String method, String uri, Object data, String contentType) throws JSONException {
			ClientResponse apiResult = buildRequest(API_BASE_URL+uri, contentType).method(method, ClientResponse.class, data);
			int apiHttpCode = apiResult.getStatus();
			
			JSONObject response = new JSONObject ();
			response.put("status", apiHttpCode);
			response.put("response", apiResult.getEntity(JSONObject.class));
			
			return response;
		}
		
		public static JSONObject get (String uri) throws JSONException {
			return get(uri, MIME_JSON);
		}
		
		public static JSONObject get (String uri, String contentType) throws JSONException {
			return exec ("GET", uri, null, contentType);
		}
		
		public static JSONObject post (String uri, Object data) throws JSONException {
			return post(uri, data, MIME_JSON);
		}
		
		public static JSONObject post (String uri, Object data, String contentType) throws JSONException {
			return exec ("POST", uri, data, contentType);
		}
		
		public static JSONObject put (String uri, Object data) throws JSONException {
			return put(uri, data, MIME_JSON);
		}
		
		public static JSONObject put (String uri, Object data, String contentType) throws JSONException {
			return exec ("PUT", uri, data, contentType);
		}
		
		private static Builder buildRequest (String resourceUrl, String contentType) {
			// Obtenemos cliente Http de Apache
			Client client = ApacheHttpClient.create();

			WebResource resource = client.resource(resourceUrl);
			Builder req = resource.type(contentType).accept("application/json");
			req.header("User-Agent", "MercadoPago Java SDK v"+MP.version);
			return req;
		}
	}
}
