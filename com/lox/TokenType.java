package com.lox;

enum TokenType {
    // Single Character tokesn
    LEFT_PAREN, RIGHT_PAREN, 
    LEFT_BRACE, RIGHT_BRACE, 
    LEFT_BRACKET, RIGHT_BRACKET, 
    COMMA, DOT, MINUS, PLUS, SEMICOLON, 
    SLASH, STAR,


    // One or two character tokens
    BANG, BANG_EQUAL, 
    EQUAL, EQUAL_EQUAL, 
    LESS, LESS_EQUAL, 
    GREATER, GREATER_EQUAL, 

    // Literals
    IDENTIFIER, STRING, NUMBER, 

    // Keywords
    IF, ELSE, OR, AND, CLASS, 
    THIS, SUPER, FUN, TRUE, FALSE, 
    VAR, FOR, WHILE, NIL, PRINT, 
    RETURN,

    EOF
}
