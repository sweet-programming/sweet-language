grammar Sweet;

program: statement*;

formList: formula ( ',' formula )*;

statement
    : 'if' formula expression         # ifStatement
    | 'unless' formula expression     # unlessStatement
    | expression 'if' formula        # postIfStatement
    | expression 'unless' formula    # postUnlessStatement
    | expression                      # expressionStatement
    ;

expression
    : 'return' formula?                  # returnExpression
    | formula                            # formulaExpression
    ;

formula
    : formula '(' formList? ')'           # functionCall
    | ID '=' formula                      # assignValue
    | formula op=('*'|'/') formula        # divMulOperation
    | formula op=('+'|'-') formula        # addSubOperation
    | formula '==' formula                # equalsOperation
    | '@' argList? '{' statement* '}'     # functionDefinition
    | formula arrayAccessor '=' formula   # assignArray
    | formula '[' formula IAD             # isArrayDefined
    | formula arrayAccessor               # arrayRef
    | IIDD                                # isIdDefined
    | STRING                              # stringValue
    | INT                                 # intValue
    | ID                                  # valueRef
    | '(' formula ')'                     # parenthesis
    ;

argList: '(' ID typeSuffix? ( ',' ID typeSuffix? )* ')';

typeSuffix: ':' ID;

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
