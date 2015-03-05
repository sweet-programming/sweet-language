grammar Sweet;

program: expression*;

expression: functionDefinition | statement;

statement: assign | formula;

functionDefinition: '@' '{' statement* '}';

assign: ID '=' formula
      | ID '=' functionDefinition;

formula: ID formula+      # functionCall
       | STRING           # string
       | ID               # id
       ;

ID:    [a-zA-z_] [a-zA-Z_0-9]*;
INT:   [0-9]+;
WS:    [ \t\n] -> skip;

ESC_SEQ: '\\' ('\"'|'\\'|'/'|'b'|'f'|'n'|'r'|'t');

STRING: '"' (ESC_SEQ | ~('\\'|'"'))* '"';
