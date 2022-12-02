JAVAHOME=/opt/java/current
JAVAOPT=-d bin -sourcepath
COPT=-W -Wall -Wextra -g -O0

all: \
	bin/PipeController.class \
	bin/ClientLauncher.class \
	bin/WormholeServer.class \
	bin/C_AES bin/AESTest

bin/ClientLauncher.class: src/client/ClientLauncher.java src/client/insertController.java src/client/FileUploadController.java src/client/PipeController.java
	mkdir -p bin
	${JAVAHOME}/bin/javac ${JAVAOPT} src/client src/client/insertController.java
	${JAVAHOME}/bin/javac ${JAVAOPT} src/client src/client/ClientLauncher.java
	cp -r src/client/res bin/

bin/WormholeServer.class: src/server/WormholeServer.java src/server/PipeController.java
	${JAVAHOME}/bin/javac ${JAVAOPT} src/server src/server/WormholeServer.java

bin/PipeController.class: src/C_AES/PipeController.java
	${JAVAHOME}/bin/javac ${JAVAOPT} src/C_AES src/C_AES/PipeController.java
	cp src/C_AES/PipeController.java src/client/
	cp src/C_AES/PipeController.java src/server/

bin/C_AES: src/C_AES/pipe.c obj/AES.Lib.o
	mkdir -p bin
	gcc ${COPT} src/C_AES/pipe.c -o bin/C_AES obj/AES.Lib.o

bin/AESTest: src/C_AES/AESTest.c obj/AES.Lib.o
	mkdir -p bin
	gcc ${COPT} src/C_AES/AESTest.c -o bin/AESTest obj/AES.Lib.o

obj/AES.Lib.o : src/C_AES/AES.Lib.c src/C_AES/AES.Lib.h
	mkdir -p obj/
	gcc ${COPT} -c src/C_AES/AES.Lib.c -o obj/AES.Lib.o


#bin/pipe_in.class: pipe_in.java
#	javac src/C_AES/pipe_in.java

.PHONY: all
