<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<%--
MercadoPago SDK
Checkout button with MD5 hash
@date 2012/03/29
@author hcasatti
--%>
<%-- Include Mercadopago library --%>
<%@page import="mercadopago.MP"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.security.MessageDigest"%>
<%
// Get your Mercadopago credentials (CLIENT_ID and CLIENT_SECRET): 
// Argentina: https://www.mercadopago.com/mla/herramientas/aplicaciones 
// Brasil: https://www.mercadopago.com/mlb/ferramentas/aplicacoes

// Define item data according to form
Map<String, String> data = new HashMap<String, String> ();
    // Required
    data.put ("item_title", "Title");
    data.put ("item_quantity", "1"); // Obtained from form input
    data.put ("item_unit_price", "10.00");
    data.put ("item_currency_id", "ARS"); //Argentina: ARS, Brasil: BRL

    // Optional
    data.put ("item_id", "CODE_012");
    data.put ("item_picture_url", "Image URL");
    data.put ("external_reference", "BILL_001");
    data.put ("payer_name", "");
    data.put ("payer_surname", "");
    data.put ("payer_email", "");
    data.put ("back_url_success", "https://www.success.com");
    data.put ("back_url_pending", "");

string md5String =   "CLIENT_ID"+                    
                "CLIENT_SECRET"+                
                data.get("item_quantity")+                  // item_quantity
                data.get("item_currency_id")+               // item_currency_id
                data.get("item_unit_price")+                // item_unit_price

                data.get("item_id")+                        // item_id
                data.get("external_reference");             // external_reference
        
// Get md5 hash
String md5 = "";
MessageDigest md = MessageDigest.getInstance("MD5");
md.reset();
for (byte b: md.digest(md5String.getBytes())) {
    md5 += String.format("%02x", 0xFF&b);
}
%>
<html>
    <head>
        <title>Checkout button with MD5 hash, using AJAX - Form</title>
    </head>
	<body>
		<form action="https://www.mercadopago.com/checkout/init" method="post" enctype="application/x-www-form-urlencoded" target="">
			<!--Required authentication. Get the CLIENT_ID: 
			Argentina: https://www.mercadopago.com/mla/herramientas/aplicaciones 
			Brasil: https://www.mercadopago.com/mlb/ferramentas/aplicacoes -->	
			<input type="hidden" name="client_id" value="CLIENT_ID"/>
		
			<!-- Hash MD5 -->
			<input type="hidden" name="md5" value="<%=md5%>"/>
		   
			<!-- Required -->
			<input type="hidden" name="item_title" value="<%=data.get("item_title")%> "/>
			<input type="hidden" name="item_quantity" value="<%=data.get("item_quantity")%>"/>
			<input type="hidden" name="item_currency_id" value="<%=data.get("item_currency_id")%>"/>
			<input type="hidden" name="item_unit_price" value="<%=data.get("item_unit_price")%>"/>
		   
			<!-- Optional -->
			<input type="hidden" name="item_id" value="<%=data.get("item_id")%>"/>
			<input type="hidden" name="external_reference" value="<%=data.get("external_reference")%>"/>
			<input type="hidden" name="item_picture_url" value="<%=data.get("item_picture_url")%>"/>
			<input type="hidden" name="payer_name" value="<%=data.get("payer_name")%>"/>
			<input type="hidden" name="payer_surname" value="<%=data.get("payer_surname")%>"/>
			<input type="hidden" name="payer_email" value="<%=data.get("payer_email")%>"/>
			<input type="hidden" name="back_url_success" value="<%=data.get("back_url_success")%>"/>
			<input type="hidden" name="back_url_pending" value="<%=data.get("back_url_pending")%>"/>
		   
			<!-- Checkout Button -->
			<button type="submit" class="lightblue-rn-m-tr-arall" name="MP-payButton">Pagar</button>
		</form>
		
		<!-- More info about render.js: https://developers.mercadopago.com -->
		<script type="text/javascript" src="http://mp-tools.mlstatic.com/buttons/render.js"></script>
	</body>
</html>
