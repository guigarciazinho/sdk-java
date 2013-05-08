# MercadoPago´s Java SDK

This is the official Java SDK for MercadoPago's Platform.

## How do I install it?:

How do I install it using maven:

Just add to your pom.xml the following repository

```XML
<repositories>
	...
	    <repository>
	        <id>mercadopago</id>
	        <url>https://github.com/mercadopago/sdk-java/raw/master/snapshots</url>
	    </repository>
	...
	</repositories>	 
```

And then add your dependencies

```XML
 <dependencies>
 	...
  	<dependency>
  		<groupId>org.codehaus.jettison</groupId>
  		<artifactId>jettison</artifactId>
  		<version>1.3.3</version>
  	</dependency>
   	<dependency>
        <groupId>com.mercadopago</groupId>
 		<artifactId>sdk</artifactId>
 		<version>0.0.1</version>
  	</dependency>
  	...
   </dependencies>
```
And that's it!

#### Another way to integrate

1. Copy **lib/mercadopago.jar**, **lib/jettison-1.0.1.jar**, **lib/commons-codec-1.6.jar**, **lib/commons-logging-1.1.1.jar**, **lib/fluent-hc-4.2.5.jar**, **lib/httpclient-4.2.5.jar**, **lib/httpclient-cache-4.2.5.jar**, **lib/httpcore-4.2.4.jar** and **lib/httpmime-4.2.5.jar** to your project desired folder.
2. Add these libs in your build path project.

And that's it!

## How can use MercadoPago´s Java SDK ?

<a href="https://github.com/mercadopago/sdk-java/blob/master/README.md" name="usage">Usage</a>


