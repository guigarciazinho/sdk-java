release:
	@echo "Building Java SDK"
	rm -rf ../lib
	mkdir ../lib

	mkdir build
	javac -cp lib/commons-codec-1.5.jar:lib/commons-httpclient-3.0.1.jar:lib/commons-lang-2.3.jar:lib/commons-logging-1.1.1.jar:lib/javax.servlet.jar:lib/jersey-apache-client-1.0.3.jar:lib/jersey-client-1.0.3.jar:lib/jersey-core-1.0.3.jar:lib/jersey-json-1.0.3.jar:lib/jettison-1.0.1.jar:lib/joda-time.jar:lib/jsr311-api-1.0.jar mercadopago/MP.java -d build

	cd build;jar -cf ../../lib/mercadopago.jar *

	rm -fr build
	rm -f mercadopago/*.class
	
	cp -r lib/jettison-1.0.1.jar ../lib/
	cp -fr ../lib ../tests
