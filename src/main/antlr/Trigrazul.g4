grammar Trigrazul;



application
    : statement*
    ;

statement
    : declaration
    ;

declaration
    : CONSEQUENCE identifier ':' atom ';' #termDeclaration
    | CONSEQUENCE identifier (':' TYPE)? ':=' '{' constructor* '}' ';' #constructorDeclaration
    | CONSEQUENCE assignmentAtom ';' #assignmentDeclaration
    ;

atom
    : assignmentAtom
    | functionAtom
    ;
primaryAtom
    : '(' atom ')' #parenAtom
    | identifier #identifierAtom
    | TYPE #typeAtom
    | '{' branch* '}'  #patternAtom
    ;

typedAtom : (left+=primaryAtom ':')* right=primaryAtom;
appliedAtom : (left+=typedAtom PIPE)* right=typedAtom;
functionAtom : (left+=appliedAtom op+=(ARROW|MAPSTO))* right=appliedAtom;
assignmentAtom :  identifier (':' type=functionAtom)? ':=' value=functionAtom;

branch
    : '|' atom ';'
    ;

constructor
    : '.' identifier ';'
    | '.' identifier ':' atom ';'
    ;

//peanoIdentifier
//    : allIdentifier  '_' '{' NATURAL_NUMBER '}'
//    ;

identifier
    : VARIABLE
    ;

CONSEQUENCE: '\\vdash' | '|-' | '⊢';
ARROW : '\\rightarrow' | '->' | '→' ;
MAPSTO: '\\mapsto' | '|->' | '↦' ;
PIPE: '\\triangleright' | '|>' | '▷';
TYPE: 'Type';

VARIABLE
    : [A-Za-z] [a-zA-Z0-9_\-]* [a-zA-Z0-9]
    | [A-Za-z]
    ;

NATURAL_NUMBER
    : [1-9][0-9]*
    | '0'
    ;
WS
    : [ \r\n] -> skip
    ;


LineComment
    : '#'  ~[\r\n]* -> skip
    ;