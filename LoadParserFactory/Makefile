
POSITION=../struktor/processor/

all: parser.java sym.java

parser.java sym.java: loadParser.cup
	java -cp .. java_cup.Main < loadParser.cup
	mv parser.java LoadParser.java
	mv sym.java Lsym.java
	sed -i -e "s/public class sym {/public class Lsym {/" Lsym.java

clean:
	rm *.java

install: 
	cp LoadParser.java Lsym.java $(POSITION)
