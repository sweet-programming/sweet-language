grammar Sweet;

program: statement*;

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
    : '(' bindingList ')' '=>' formula                            # partialApplication
    | formula '(' formList? bindingList? ')'                      # functionCall
    | ID '.' ID                                                   # memberOperation
    | ID '=' formula                                              # assignValue
    | formula op=('*'|'/') formula                                # divMulOperation
    | formula op=('+'|'-') formula                                # addSubOperation
    | formula '==' formula                                        # equalsOperation
    | '@' ('(' argList? parametricArgList? ')')? '{' statement* '}'     # functionDefinition
    | IIDD                                                        # isIdDefined
    | STRING                                                      # stringValue
    | INT                                                         # intValue
    | ID                                                          # valueRef
    | '(' formula ')'                                             # parenthesis
    ;

binding: ID ':' formula;
bindingList: binding ( ',' binding )*;

argList: ID argTypeSuffix? ( ',' ID argTypeSuffix? )*;
argTypeSuffix: typeSuffix ( '[' formula '..' formula ']' )?;

parametricArgList: ID argTypeSuffix? '=' formula ( ',' ID argTypeSuffix? '=' formula )*;

typeSuffix: ':' ID;

formList: formula ( ',' formula )*;


ID:     ('a' .. 'z' | 'A' .. 'Z') ('a' .. 'z' | 'A' .. 'Z' | '0' .. '9')*;
IIDD:  ID '?';
INT:   [0-9]+;
WS:    [ \t\n] -> skip;

ADD:  '+';
SUB:  '-';
MUL:  '*';
DIV:  '/';

ESC_SEQ: '\\' ('\"'|'\\'|'/'|'b'|'f'|'n'|'r'|'t');

STRING: '"' (ESC_SEQ | ~('\\'|'"'))* '"';
