grammar Sweet;

program: statement*;

formList: formula ( ',' formula )*;

statement
    : 'return' formula?                  # returnStatement
    | 'if' '(' formula ')' statement     # ifStatement
    | 'unless' '(' formula ')' statement # unlessStatement
    | formula                            # formulaStatement
    ;

argList: '(' ID typeSuffix? ( ',' ID typeSuffix? )* ')';

typeSuffix: ':' ID;

formula
    : formula '(' formList? ')'           # functionCall
    | ID '=' formula                      # assignValue
    | formula op=('*'|'/') formula        # divMulOperation
    | formula op=('+'|'-') formula        # addSubOperation
    | formula '==' formula                # equalsOperation
    | formula arrayAccessor '=' formula   # assignArray
    | '@' argList? '{' statement* '}'     # functionDefinition
    | formula '[' formula IAD             # isArrayDefined
    | formula arrayAccessor               # arrayRef
    | IIDD                                # isIdDefined
    | STRING                              # stringValue
    | INT                                 # intValue
    | ID                                  # valueRef
    | '(' formula ')'                     # parenthesis
    ;

arrayAccessor: '[' formula ']';


ID:     ('a' .. 'z' | 'A' .. 'Z') ('a' .. 'z' | 'A' .. 'Z' | '0' .. '9')*;
IIDD:  ID '?';
IAD:   ']' '?';
INT:   [0-9]+;
WS:    [ \t\n] -> skip;

ADD:  '+';
SUB:  '-';
MUL:  '*';
DIV:  '/';

ESC_SEQ: '\\' ('\"'|'\\'|'/'|'b'|'f'|'n'|'r'|'t');

STRING: '"' (ESC_SEQ | ~('\\'|'"'))* '"';
