<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="com.mercadopago.MP"%>
<%@page import="org.codehaus.jettison.json.JSONObject"%>

<%
    String clientId = "CLIENT_ID";
    String clientSecret = "CLIENT_SECRET";

    MP mp = new MP (clientId, clientSecret);

    JSONObject preapprovalPayment = mp.createPreapprovalPayment("{'payer_email':'my_customer@my_site.com','back_url':'http://www.my_site.com','reason':'Monthly subscription to premium package','external_reference':'OP-1234','auto_recurring':{'frequency':1,'frequency_type':'months','transaction_amount':60,'currency_id':'BRL','start_date':'2012-12-10T14:58:11.778-03:00','end_date':'2013-06-10T14:58:11.778-03:00'}}");

    String preapprovalPaymentURL = preapprovalPayment.getJSONObject("response").getString("init_point");
%>

<!doctype html>
<html>
    <head>
        <title>MercadoPago SDK - Create Preapproval Payment and Show Subscription Example</title>
    </head>
    <body>
        <a href="<%= preapprovalPaymentURL %>" name="MP-Checkout" class="blue-l-arall-rn">Pay</a>
        <script type="text/javascript" src="//resources.mlstatic.com/mptools/render.js"></script>
    </body>
</html>
