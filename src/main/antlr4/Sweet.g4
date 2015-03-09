grammar Sweet;

program: expression*;

expression: functionDefinition | statement;

formList: formula ( ',' formula )*;

statement: assign                  # assignStatement
         | formula                 # formulaSatement
         | 'return' formula?       # returnStatement
         | statement 'if' formula  # postIfStatement
         ;

functionDefinition: '@' argList? '{' statement* '}';

argList: '(' ID typeSuffix? ( ',' ID typeSuffix? )* ')';

typeSuffix: ':' ID;

assign: ID '=' formula
      | ID '=' functionDefinition;

formula: ID '(' formList? ')'      # functionCall
       | ID formList               # functionCall2
       | STRING                    # stringValue
       | INT                       # intValue
       | ID                        # valueRef
       | formula op=('*'|'/') formula  # divMulOperation
       | formula op=('+'|'-') formula  # addSubOperation
       | formula '==' formula      # equalsOperation
       ;

ID:    [a-zA-z_] [a-zA-Z_0-9]*;
INT:   [0-9]+;
WS:    [ \t\n] -> skip;

ADD:  '+';
SUB:  '-';
MUL:  '*';
DIV:  '/';

ESC_SEQ: '\\' ('\"'|'\\'|'/'|'b'|'f'|'n'|'r'|'t');

STRING: '"' (ESC_SEQ | ~('\\'|'"'))* '"';
