
POSITION=../struktor/strukelements/

all: Yylex.java

Yylex.java: loader.lex
	java -cp .. JLex.Main loader.lex; mv loader.lex.java Yylex.java

install: 
	cp Yylex.java $(POSITION)

clean:
	rm Yylex.java loader.lex.java
