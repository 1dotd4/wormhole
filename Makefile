JAVAHOME=/opt/java/current
JAVAOPT=-d bin

bin/ClientLauncher.class: src/client/ClientLauncher.java src/client/insertController.java
	${JAVAHOME}/bin/javac ${JAVAOPT} src/client/insertController.java
	${JAVAHOME}/bin/javac ${JAVAOPT} src/client/ClientLauncher.java
	cp -r src/client/res bin/
