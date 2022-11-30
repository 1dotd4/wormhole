JAVAHOME=/opt/java/current
JAVAOPT=-d bin -sourcepath

all: bin/ClientLauncher.class bin/WormholeServer.class

bin/ClientLauncher.class: src/client/ClientLauncher.java src/client/insertController.java src/client/FileUploadController.java
	${JAVAHOME}/bin/javac ${JAVAOPT} src/client src/client/insertController.java
	${JAVAHOME}/bin/javac ${JAVAOPT} src/client src/client/ClientLauncher.java
	cp -r src/client/res bin/

bin/WormholeServer.class: src/server/WormholeServer.java
	${JAVAHOME}/bin/javac ${JAVAOPT} src/server src/server/WormholeServer.java

# all: AES.Lib.o pipe pipe_in.class AESTest

AES.Lib.o : AES.Lib.c AES.Lib.h
	gcc -c AES.Lib.c

pipe: pipe.c AES.Lib.o
	gcc pipe.c -o pipe AES.Lib.o

pipe_in.class: pipe_in.java
	javac pipe_in.java

AESTest: AESTest.c AES.Lib.o
	gcc AESTest.c -o AESTest AES.Lib.o

.PHONY: all
