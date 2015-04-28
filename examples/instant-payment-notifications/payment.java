package util;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
/**MercadoPago SDK
Receive IPN
@date 2012/03/29
@author hcasatti

Include Mercadopago library*/
import com.mercadopago.MP;

import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONArray;

public class ReceiveFeed extends HttpServlet{
 
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// Create an instance with your MercadoPago credentials (CLIENT_ID and CLIENT_SECRET): 
		// Argentina: https://www.mercadopago.com/mla/herramientas/aplicaciones 
		// Brasil: https://www.mercadopago.com/mlb/ferramentas/aplicacoes
		MP mp = new MP ("CLIENT_ID", "CLIENT_SECRET");
		try{
			//Get your access token
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("access_token", mp.getAccessToken());
			JSONObject merchantOrderInfo=null;
			 
			// Get the merchant_order reported by the IPN. Glossary of attributes response in https://developers.mercadopago.com
			if(request.getParameter("topic").equals("merchant_order")){
				merchantOrderInfo = mp.get("/merchant_orders/" + request.getParameter("id"), params, false);
				// Get the payment reported by the IPN. Glossary of attributes response in https://developers.mercadopago.com
			}else if(request.getParameter("topic").equals("payment")){
				JSONObject paymentInfo = mp.get("/collections/notifications/" + request.getParameter("id"), params, false);
				merchantOrderInfo = mp.get("/merchant_orders/" + paymentInfo.getJSONObject("response").getJSONObject("collection").getString("merchant_order_id"), params, false);
			}
			//If the payment's transaction amount is equal (or bigger) than the merchant order's amount you can release your items 
			if (merchantOrderInfo.getInt("status") == 200) {
				Double transactionAmountPayments = 0D;
				Double transactionAmountOrder = merchantOrderInfo.getJSONObject("response").getDouble("total_amount");
				JSONArray payments=merchantOrderInfo.getJSONObject("response").getJSONArray("payments");
			    for (int i=0; i < payments.length(); i++) {
			    	JSONObject payment = payments.getJSONObject(i);
			    	if(payment.getString("status").equals("approved")){
			    		transactionAmountPayments += payment.getDouble("transaction_amount");
				    }	
			    }
			    if(transactionAmountPayments >= transactionAmountOrder){
			    	System.out.print("release your items");
			    }else{
					System.out.print("dont release your items");
				}
			}
		}catch(Exception e){
			throw new ServletException(e);
		}
	}
}