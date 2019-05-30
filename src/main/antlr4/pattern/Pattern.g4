grammar Pattern;

parse
 : pattern EOF
 ;

pattern
 : stream
 | sequence
 | dataparallel 
 | main
 | OTHER {System.err.println("unknown char: " + $OTHER.text);}
 ;
 
 main : stream
  | sequence
  | dataparallel
 ;
 
stream
 : farm
 | pipe
 ;
 
sequence
 : seq
 | comp
 ;
 
dataparallel : map ;

seq : ID ASSIGN INT SCOL ;

comp : COMP block ;

farm : FARM block ;

pipe : PIPE block ;
 
map : MAP block ;
 
block : OBRACE pattern CBRACE ;

SCOL : ';';
ASSIGN : '=';
OPAR : '(';
CPAR : ')';

ID
 : [a-zA-Z_] [a-zA-Z_0-9]*
 ;

INT
 : [0-9]+
 ;

COMMENT
 : '#' ~[\r\n]* -> skip
 ;

SPACE
 : [ \t\r\n] -> skip
 ;

OTHER
 : . 
 ;
