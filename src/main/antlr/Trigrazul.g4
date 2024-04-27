grammar Trigrazul;



application
    : statement*
    ;

statement
    : declaration
    ;

declaration
    : module=(RATAIL|GETS|CONSEQUENCE) identifier ':' atom ';' #termDeclaration
    | module=(GETS|CONSEQUENCE) identifier (':' TYPE)? ':=' '{' constructor* '}' ';' #constructorDeclaration
    | module=(RATAIL|GETS|CONSEQUENCE) assignmentAtom ';' #assignmentDeclaration
    ;

atom
    : assignmentAtom
    | functionAtom
    ;
primaryAtom
    : '(' atom ')' #parenAtom
    | identifier #identifierAtom
    | TYPE #typeAtom
    | STRING PIPE IMPORT #moduleAtom
    | '{' branch* '}'  #patternAtom
    ;

typedAtom : (left+=primaryAtom ':')* right=primaryAtom;
appliedAtom : (left+=typedAtom PIPE)* right=typedAtom;
functionAtom : (left+=appliedAtom op+=(TO|MAPSTO))* right=appliedAtom;
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
RATAIL: '>-'|'⤚';
GETS: '\\leftarrow'|'\\gets'|'<-'|'←';
TO : '\\rightarrow'|'\\to' | '->' | '→' ;
MAPSTO: '\\mapsto' | '|->' | '↦' ;
PIPE: '\\triangleright'|'\\rhd' | '|>' | '▷';
TYPE: 'Type';
IMPORT: 'Import';

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

STRING: '"' CHAR*  '"';
fragment CHAR
    : ~["\\\r\n]
    | ESCAPE_SEQUENCE
    ;
fragment ESCAPE_SEQUENCE: '\\' ["\\];

LINE_COMMENT
    : '#'  ~[\r\n]* -> skip
    ;