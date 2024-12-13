package com.lox;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import static com.lox.TokenType.*;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    public Scanner(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            // We are at beginning of the next lexemme
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(' -> addToken(LEFT_PAREN);
            case ')' -> addToken(RIGHT_PAREN);
            case '{' -> addToken(LEFT_BRACE);
            case '}' -> addToken(RIGHT_BRACE);
            case '[' -> addToken(LEFT_BRACKET);
            case ']' -> addToken(RIGHT_BRACKET);
            case '.' -> addToken(DOT);
            case ',' -> addToken(COMMA);
            case '-' -> addToken(MINUS);
            case '+' -> addToken(PLUS);
            case '*' -> addToken(STAR);
            case ';' -> addToken(SEMICOLON);
            case '=' -> addToken(match('=') ? EQUAL_EQUAL : EQUAL);
            case '!' -> addToken(match('=') ? BANG_EQUAL : BANG);
            case '>' -> addToken(match('=') ? GREATER_EQUAL : GREATER);
            case '<' -> addToken(match('=') ? LESS_EQUAL : LESS);
            case '\n' -> ++line;
            case ' ', '\r', '\t' -> {
                // ignore whitespace
            }
            case '/' -> {
                if (match('/')) {
                    // We keep moving and scanning until the comment finished
                    // The end of comment is marked by the end of the line
                    while (peek() != '\n' && !isAtEnd())
                        advance();
                } else {
                    addToken(SLASH);
                }
            }
            case '"' -> string();
            default -> {
                if(isDigit(c)) {
                    number();
                } else {
                    Lox.error(line, "Unexpected character.");
                }
            }
        }
    }

    private boolean isDigit(char ch) {
        return (ch >= '0' && ch <= '9');
    }

    private void number() {
        while(isDigit(peek())) {
            advance();
        }

        if(peek() == '.' && isDigit(peekNext())) {
            // consume the .
            advance();
            while (isDigit(peek())) advance();
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));

    }

    private void string() {
        while(peek() != '"' && !isAtEnd()) {
            if(peek() == '\n') {
                ++line;
            }
        }

        if(isAtEnd()) {
            Lox.error(line, "Unterminated String");
            return;
        }

        advance();
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    private char peek() {
        if (isAtEnd()) {
            return '\0';
        }
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) {
            return '\0';
        }
        return source.charAt(current + 1);
      } 

    private boolean match(char expected) {
        if (isAtEnd()) {
            return false;
        }
        if (source.charAt(current) != expected) {
            return false;
        }

        current++;
        return true;
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

}
