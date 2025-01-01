### Terminology
- ? : optional 
- \* : zero or more
- Values in quotes or block letter indicate terminals
- Values in smallcase indicate non-terminals
- | : or i.e either or the given choice

### Language Grammar
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

- program -> declaration* EOF;
- declaration -> varDecl | statement;
- statement -> exprStmt | printStmt;
- varDecl -> "var" IDENTIFIER ("=" expression)? ";" ; 