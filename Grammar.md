### Terminology
- ? : optional 
- \* : zero or more
- Values in quotes or block letter indicate terminals
- Values in smallcase indicate non-terminals
- | : or i.e either or the given choice
- The semicolon in the productions/rules is just metasyntax for expressing the grammar

## Language Grammar

### Expression Grammar
- expression -> comma ;
- comma -> assignment ("," assignment)* ;
- assignment -> IDENTIFIER "=" assignment | conditional;
- conditional -> logic_or ("?" conditional ":" conditonal)? ;
- logic_or -> logic_and ("or" logic_and)* ;
- logic_and -> equality ("and" equality)* ;
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