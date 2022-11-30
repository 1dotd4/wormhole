all: AES.Lib.o pipe pipe_in.class AESTest

AES.Lib.o : AES.Lib.c AES.Lib.h
	gcc -c AES.Lib.c

pipe: pipe.c AES.Lib.o
	gcc pipe.c -o pipe AES.Lib.o

pipe_in.class: pipe_in.java
	javac pipe_in.java

AESTest: AESTest.c AES.Lib.o
	gcc AESTest.c -o AESTest AES.Lib.o
