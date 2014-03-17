<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%-- MercadoPago SDK - Create Preference and Show Checkout Example --%>

<%@page import="com.mercadopago.MP"%>
<%@page import="org.codehaus.jettison.json.JSONObject"%>

<%
	String clientId = "CLIENT_ID";
	String clientSecret = "CLIENT_SECRET";

	MP mp = new MP (clientId, clientSecret);

	JSONObject preference = mp.createPreference("{'items':[{'title':'sdk-java','quantity':1,'currency_id':'ARS','unit_price':10.5}]}");

	String checkoutURL = preference.getJSONObject("response").getString("init_point");
%>

<!doctype html>
<html>
    <head>
        <title>MercadoPago SDK - Create Preference and Show Checkout Example</title>
    </head>
	<body>
		<a href="<%= checkoutURL %>" name="MP-Checkout" class="blue-l-arall-rn">Pagar</a>
		<script type="text/javascript" src="http://mp-tools.mlstatic.com/buttons/render.js"></script>
	</body>
</html>
