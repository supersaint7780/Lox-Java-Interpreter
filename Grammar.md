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
- assignment -> (call ".")? IDENTIFIER "=" assignment | conditional;
- conditional -> logic_or ("?" conditional ":" conditonal)? ;
- logic_or -> logic_and ("or" logic_and)* ;
- logic_and -> equality ("and" equality)* ;
- equality -> comparison ( ( "!=" | "==" ) comparison )* ;
- comparison -> term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
- term -> factor ( ( "-" | "+" ) factor )* ;
- factor -> unary ( ( "/" | "*" ) unary )* ;
- unary -> ( "!" | "-" ) unary | call ;
- call -> primary ( "(" arguments? ")" | "." IDENTIFIER )*;
- arguments -> assignment ("," assignment)*;
- primary ->  "true" | "false" | "nil" | "this" 
            | NUMBER | STRING | IDENTIFIER | "(" expression ")"
            | "super" "." IDENTIFIER ;


### Statement Grammar
- program -> declaration* EOF;
- declaration -> varDecl | statement | funDecl | classDecl;
- classDecl -> "class" IDENTIFIER ("<" IDENTIFIER )? "{" function* "}";
- funDecl -> "fun" function;
- function -> IDENTIFIER "(" parameters? ")" block;
- parameters -> IDENTIFIER ("," IDENTIFIER)* ;
- statement -> exprStmt | printStmt | block | ifStmt | whileStmt | forStmt | breakStmt | returnStmt;
- returnStmt -> "return" expression? ";" ;
- breakStmt -> "break" ";" ;
- forStmt -> "for" "(" (varDecl | exprStmt | ";") expression? ";" expression? ")" statement;
- whileStmt -> "while" "(" expression ")" statement;
- ifStmt -> "if" "(" expression ")" statement ("else" statement)? ;
- block -> "{" declaration* "}";
- varDecl -> "var" IDENTIFIER ("=" expression)? ";" ; 
- exprStmt -> expression ";" ;