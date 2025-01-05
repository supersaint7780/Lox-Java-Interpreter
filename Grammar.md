### Terminology
- ? : optional 
- \* : zero or more
- Values in quotes or block letter indicate terminals
- Values in smallcase indicate non-terminals
- | : or i.e either or the given choice

## Language Grammar

### Expression Grammar
- expression -> comma ;
- comma -> assignment ("," assignment)* ;
- assignment -> IDENTIFIER "=" assignment | conditional;
- conditional -> equality ("?" conditional ":" conditonal)? ;
- equality -> comparison ( ( "!=" | "==" ) comparison )* ;
- comparison -> term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
- term -> factor ( ( "-" | "+" ) factor )* ;
- factor -> unary ( ( "/" | "*" ) unary )* ;
- unary -> ( "!" | "-" ) unary | primary ;
- primary -> NUMBER | STRING | "true" | "false" | "nil" | "(" expression ")" | IDENTIFIER;


### Statement Grammar
- program -> declaration* EOF;
- declaration -> varDecl | statement;
- statement -> exprStmt | printStmt | block | ifStmt;
- ifStmt -> "if" "(" expression ")" statement ("else" statement)? ;
- block -> "{" declaration* "}";
- varDecl -> "var" IDENTIFIER ("=" expression)? ";" ; 