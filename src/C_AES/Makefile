all: AES.Lib.o pipe PipeController.class


AES.Lib.o : AES.Lib.c AES.Lib.h
	gcc -c AES.Lib.c


pipe: pipe.c AES.Lib.o
	gcc pipe.c -o pipe AES.Lib.o


PipeController.class: PipeController.java
	javac PipeController.java