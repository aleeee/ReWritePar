grammar Skel4;

skeletonProgram
 : programPart+ EOF
 | OTHER {System.err.println("unknown char: " + $OTHER.text);}
 ;
programPart : (statement)* mainExpr
 			;

statement : assignment ';'
         ;
mainExpr : mainMethod=main '='  expr=patternExpr ';'
		;
assignment : varName=IDENTIFIER '=' expr=patternExpr;

patternExpr : sType='Seq' sequence | sType='Comp' composition|sType='Pipe' pipeSkel| sType='Map' mapSkel| sType='Farm' farmSkel|varName=IDENTIFIER ;

main : 'main';

block : '(' expr=patternExpr ')' ;

sequence :   '(' ts= NUMBER ')';

composition :  stages;

pipeSkel : stages;
mapSkel :   block;
farmSkel :  block;
stages: '(' expr+=patternExpr  ',' expr+=patternExpr (',' expr+=patternExpr)* ')' ;

IDENTIFIER: [a-zA-Z][a-zA-Z0-9]* ;
NUMBER: [0-9]+;

COMMENT
 : '#' ~[\r\n]* -> skip
 ;

SPACE
 : [ \t\r\n] -> skip
 ;

OTHER : . 
 ;