
POSITION=../struktor/processor/

all: Yylex.java

Yylex.java: processor.lex
	java -cp .. JLex.Main processor.lex; mv processor.lex.java Yylex.java

install: 
	cp Yylex.java $(POSITION)
	
clean:
	rm Yylex.java processor.lex.java
