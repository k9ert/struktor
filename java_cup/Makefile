objects=action_part.class action_production.class assoc.class emit.class internal_error.class lalr_item.class lalr_item_set.class lalr_state.class lalr_transition.class lexer.class lr_item_core.class Main.class nonassoc_action.class non_terminal.class parse_action.class parse_action_row.class parse_action_table.class parser.class parse_reduce_row.class parse_reduce_table.class production.class production_part.class reduce_action.class shift_action.class symbol.class symbol_part.class symbol_set.class sym.class terminal.class terminal_set.class version.class 

.PHONY: all clean

all: $(objects)

$(objects): 
	javac -classpath .. $(subst .class,.java,$@)

clean:
	rm *.class
