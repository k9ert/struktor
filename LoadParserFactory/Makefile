
POSITION=../struktor/processor/

all: LoadParser.java Lsym.java

LoadParser.java Lsym.java: loadParser.cup
	java -cp .. java_cup.Main -parser LoadParser -symbols Lsym < loadParser.cup

clean:
	rm *.java

install: 
	cp LoadParser.java Lsym.java $(POSITION)
