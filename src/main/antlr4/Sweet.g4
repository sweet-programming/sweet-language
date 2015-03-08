grammar Sweet;

program: expression*;

expression: functionDefinition | statement;

formList: formula ( ',' formula )*;

statement: assign | formula;

functionDefinition: '@' argList? '{' statement* '}';

argList: '(' ID typeSuffix? ( ',' ID typeSuffix? )* ')';

typeSuffix: ':' ID;

assign: ID '=' formula
      | ID '=' functionDefinition;

formula: ID '(' formList? ')'      # functionCall
       | ID formList               # functionCall2
       | STRING           # string
       | ID               # var
       ;

ID:    [a-zA-z_] [a-zA-Z_0-9]*;
INT:   [0-9]+;
WS:    [ \t\n] -> skip;

ESC_SEQ: '\\' ('\"'|'\\'|'/'|'b'|'f'|'n'|'r'|'t');

STRING: '"' (ESC_SEQ | ~('\\'|'"'))* '"';
