# MercadoPago SDK module for Payments integration

* [Install](#install)
* [Usage](#usage)
* [Using MercadoPago Checkout](#checkout)
* [Using MercadoPago Payment collection](#payments)

<a name="install"></a>
## Install:

Install it using maven:

Just add to your pom.xml the following repository

```XML
<repositories>
    ...
    <repository>
        <id>mercadopago</id>
        <url>https://github.com/mercadopago/sdk-java/raw/master/releases</url>
    </repository>
    ...
</repositories>  
```

Then add the dependency

```XML
 <dependencies>
    ...
    <dependency>
        <groupId>com.mercadopago</groupId>
        <artifactId>sdk</artifactId>
        <version>0.3.4</version>
    </dependency>
    ...
</dependencies>
```
And that's it!

<a name="usage"></a>
## Usage:

### ...with your credentials:

* Get your **CLIENT_ID** and **CLIENT_SECRET** in the following address:
    * Argentina: [https://www.mercadopago.com/mla/herramientas/aplicaciones](https://www.mercadopago.com/mla/herramientas/aplicaciones)
    * Brazil: [https://www.mercadopago.com/mlb/ferramentas/aplicacoes](https://www.mercadopago.com/mlb/ferramentas/aplicacoes)
    * México: [https://www.mercadopago.com/mlm/herramientas/aplicaciones](https://www.mercadopago.com/mlm/herramientas/aplicaciones)
    * Venezuela: [https://www.mercadopago.com/mlv/herramientas/aplicaciones](https://www.mercadopago.com/mlv/herramientas/aplicaciones)
    * Colombia: [https://www.mercadopago.com/mco/herramientas/aplicaciones](https://www.mercadopago.com/mco/herramientas/aplicaciones)

```JAVA
import mercadopago.MP;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

MP mp = new MP ("CLIENT_ID", "CLIENT_SECRET");

```

### ...with your long live access token:

```JAVA
import mercadopago.MP;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

MP mp = new MP ("LL_ACCESS_TOKEN");

```

### Get your Access Token:

```JAVA
String accessToken = mp.getAccessToken();

System.out.println(accessToken);
```

<a name="checkout"></a>
## Using MercadoPago Checkout

### Get an existent Checkout preference:

```JAVA
JSONObject preference = mp.getPreference("PREFERENCE_ID");

System.out.println(preference.toString());
```

### Create a Checkout preference:

```JAVA
JSONObject createPreferenceResult = mp.createPreference("{'items':[{'title':'Prueba','quantity':1,'currency_id':'ARS','unit_price':10.5}]}");
System.out.println(createPreferenceResult.toString());
```
<a href="http://developers.mercadopago.com/documentacion/recibir-pagos#glossary">Others items to use</a>

### Update an existent Checkout preference:

```JAVA
JSONObject updatePreferenceResult = mp.updatePreference("PREFERENCE_ID", "{'items':[{'title':'Prueba','quantity':1,'currency_id':'USD','unit_price':2}]}");
System.out.println(updatePreferenceResult.toString());
```

<a name="payments"></a>
## Using MercadoPago Payment

### Searching:

```JAVA
// Sets the filters you want
Map<String, Object> filters = new HashMap<String, Object> ();
   filters.put("site_id", "MLA"); // Argentina: MLA; Brasil: MLB; Mexico: MLM; Venezuela: MLV; Colombia: MCO
   filters.put("external_reference", "Bill001");
        
// Search payment data according to filters
JSONObject searchResult = mp.searchPayment (filters);
JSONArray results = searchResult.getJSONObject("response").getJSONArray("results");

for (int i = 0; i < results.length(); i++) {
    System.out.println(results.getJSONObject(i).getJSONObject("collection").getString("id"));
    System.out.println(results.getJSONObject(i).getJSONObject("collection").getString("external_reference"));
    System.out.println(results.getJSONObject(i).getJSONObject("collection").getString("status"));
}
```

<a href="http://developers.mercadopago.com/documentacion/busqueda-de-pagos-recibidos">More search examples</a>

### Receiving IPN notification:

* Go to **Mercadopago IPN configuration**:
    * Argentina: [https://www.mercadopago.com/mla/herramientas/notificaciones](https://www.mercadopago.com/mla/herramientas/notificaciones)
    * Brasil: [https://www.mercadopago.com/mlb/ferramentas/notificacoes](https://www.mercadopago.com/mlb/ferramentas/notificacoes)
    * México: [https://www.mercadopago.com/mlm/herramientas/notificaciones](https://www.mercadopago.com/mlm/herramientas/notificaciones)
    * Venezuela: [https://www.mercadopago.com/mlv/herramientas/notificaciones](https://www.mercadopago.com/mlv/herramientas/notificaciones)
    * Colombia: [https://www.mercadopago.com/mco/herramientas/notificaciones](https://www.mercadopago.com/mco/herramientas/notificaciones)<br />

```JAVA
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
```

### Cancel (only for pending payments):

```JAVA
JSONObject result = mp.cancelPayment(request.getParameter("ID"));

// Show result
out.print(result);
```

### Refund (only for accredited payments):

```JAVA
JSONObject result = mp.refundPayment(request.getParameter("ID"));

// Show result
out.print(result);
```
<a href=http://developers.mercadopago.com/documentacion/devolucion-y-cancelacion> About Cancel & Refund </a>

### Generic resources methods

You can access any other resource from the MercadoPago API using the generic methods:

```JAVA
// Get a resource, with optional URL params. Also you can disable authentication for public APIs
mp.get ("/resource/uri", [params], [authenticate=true]);

// Create a resource with "data" and optional URL params.
mp.post ("/resource/uri", data, [params]);

// Update a resource with "data" and optional URL params.
mp.put ("/resource/uri", data, [params]);

// Delete a resource with optional URL params.
mp.delete ("/resource/uri", [params]);
```

 For example, if you want to get the Sites list (no params and no authentication):

```JAVA
JSONObject result = mp.get ("/sites", null, false);

out.print(result);
```