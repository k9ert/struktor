
POSITION=../struktor/processor/

all: ProcParser.java Psym.java

ProcParser.java Psym.java: procParser.cup
	java -cp .. java_cup.Main -parser ProcParser -symbols Psym < procParser.cup
	
clean:
	rm *.java

install: 
	cp ProcParser.java Psym.java $(POSITION)
