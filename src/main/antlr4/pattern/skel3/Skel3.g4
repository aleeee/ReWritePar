grammar Skel3;

skeletonProgram
 : programPart+ EOF
 | OTHER {System.err.println("unknown char: " + $OTHER.text);}
 ;
programPart : (statement)* mainExpr
 			;

statement : assignment ';'
         ;
mainExpr : mainMethod=main '=' type=varType expr=patternExpr ';'
		;
assignment : varName=IDENTIFIER '=' type=varType expr=patternExpr;

patternExpr : stream=streamPattern
            | seq=sequential
            | dataParallel=dataParallelPattern
            |type=varType expr=patternExpr
            | varName=IDENTIFIER
            ;
        
varType : Seq
		| Comp
		| Farm
		| Pipe
		| Map
		;
streamPattern : farm=farmSkel
       | pipe=pipeSkel
       ;

sequential  : sec=sequence
            | comp=composition
            ;

dataParallelPattern  : map=mapSkel
              ;
main : 'main';

block : '(' expr=patternExpr ')' ;

sequence :   '(' ts= NUMBER ')';

composition :   block;

pipeSkel : stages;

farmSkel :  block;

mapSkel :   block;

stages: '(' expr+=patternExpr  ',' expr+=patternExpr (',' expr+=patternExpr)* ')' ;

Seq : 'Seq';
Comp: 'Comp';
Farm: 'Farm';
Pipe: 'Pipe';
Map: 'Map';

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