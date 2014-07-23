grammar Sweet;

program: expression*;

expression: functionDefinition | statement;

statement: assign | formula;

functionDefinition: '@' '{' statement* '}';

assign: ID '=' formula | ID '=' functionDefinition;

formula: functionCall | stringLiteral | ID;

stringLiteral: '"' (ESC_SEQ | ~('\\'|'"'))* '"';

functionCall: ID formula+;

ID:    [a-zA-z_] [a-zA-Z_0-9]*;
INT:   [0-9]+;
WS:    [ \t\n] -> skip;

ESC_SEQ: '\\' ('\"'|'\\'|'/'|'b'|'f'|'n'|'r'|'t');
