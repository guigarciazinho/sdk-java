<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html>
    <head>
        <title>Search funded payments with 12 installments and product name "product_name"</title>
    </head>
    <body>
        <%-- Include Mercadopago library --%>
        <%@page import="com.mercadopago.MP"%>
        <%@page import="java.util.Map"%>
        <%@page import="java.util.HashMap"%>
        <%@page import="org.codehaus.jettison.json.JSONObject"%>
        <%@page import="org.codehaus.jettison.json.JSONArray"%>
        <%
        /**
         * MercadoPago SDK
         * Search funded payments with 12 installments and product name "product_name"
         * @date 2012/03/29
         * @author hcasatti
         */
        
        // Create an instance with your MercadoPago credentials (CLIENT_ID and CLIENT_SECRET): 
        // Argentina: https://www.mercadopago.com/mla/herramientas/aplicaciones 
        // Brasil: https://www.mercadopago.com/mlb/ferramentas/aplicacoes
        MP mp = new MP ("CLIENT_ID", "CLIENT_SECRET");
      
        // Sets the filters you want
        Map<String, Object> filters = new HashMap<String, Object> ();
            filters.put("installments", "12");
            filters.put("reason", "product_name");
            filters.put("operation_type", "regular_payment");
      
        // Search payment data according to filters
        JSONObject searchResult = mp.searchPayment (filters);
        JSONArray results = searchResult.getJSONObject("response").getJSONArray("results");
        
        // Show payment information
        %>
        <table border='1'>
            <tr><th>id</th><th>site_id</th><th>external_reference</th><th>status</th></tr>
            <%
            for (int i = 0; i < results.length(); i++) {
                %>
                <tr>
                    <td><%=results.getJSONObject(i).getJSONObject("collection").getString("id")%></td>
                    <td><%=results.getJSONObject(i).getJSONObject("collection").getString("external_reference")%></td>
                    <td><%=results.getJSONObject(i).getJSONObject("collection").getString("status")%></td>
                </tr>
                <%
            }
            %>
        </table>
    </body>
</html>