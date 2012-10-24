<%@page contentType="text/plain" pageEncoding="UTF-8"%>
<%-- 
MercadoPago SDK
Receive IPN
@date 2012/03/29
@author hcasatti
--%>
<%-- Include Mercadopago library --%>
<%@page import="mercadopago.MP"%>
<%@page import="org.codehaus.jettison.json.JSONObject"%>
<%
// Create an instance with your MercadoPago credentials (CLIENT_ID and CLIENT_SECRET): 
// Argentina: https://www.mercadopago.com/mla/herramientas/aplicaciones 
// Brasil: https://www.mercadopago.com/mlb/ferramentas/aplicacoes
		String clientId = "2872";
		String clientSecret = "MpVkF902XmJ6HnU6YsgZOkNlbCKRML99";
		MP mp = new MP (clientId, clientSecret);
//MP mp = new MP ("CLIENT_ID", "CLIENT_SECRET");

// Get the payment reported by the IPN. Glossary of attributes response in https://developers.mercadopago.com
JSONObject payment_info = mp.getPaymentInfo(request.getParameter("id"));

// Show payment information
if (Integer.parseInt (payment_info.get("status").toString()) == 200) {
    out.print(payment_info.get("response"));
}
%>