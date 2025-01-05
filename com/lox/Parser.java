package com.lox;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import static com.lox.TokenType.*;

class Parser {
    private static class ParseError extends RuntimeException {
    }

    private final List<Token> tokens;

    // position of the next token to be parsed
    private int current = 0;
    private Boolean inLoop = false;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(declaration());
        }
        return statements;
    }

    private Stmt declaration() {
        try {
            if (match(VAR)) {
                return varDeclaration();
            } else {
                return statement();
            }
        } catch (ParseError error) {
            synchronize();
            return null;
        }
    }

    private Stmt varDeclaration() {
        Token name = consume(IDENTIFIER, "Expect variable name.");

        Expr initializer = null;
        if (match(EQUAL)) {
            initializer = expression();
        }

        consume(SEMICOLON, "Expect ';' after variable declaration.");
        return new Stmt.Var(name, initializer);
    }

    private Stmt statement() {
        if (match(FOR)) {
            return forStatement();
        }
        if (match(WHILE)) {
            return whileStatement();
        }
        if (match(IF)) {
            return ifStatement();
        }
        if (match(PRINT)) {
            return printStatement();
        }
        if (check(BREAK)) {
            return breakStatement();
        }
        if (match(LEFT_BRACE)) {
            return new Stmt.Block(block());
        }
        return expressionStatement();
    }

    private Stmt breakStatement() {
        if (!inLoop) {
            throw error(peek(), "Cannot use 'break' outside of a loop.");
        }
        Token keyword = consume(BREAK, "Expect 'break'.");
        consume(SEMICOLON, "Expected ';' after 'break'.");
        return new Stmt.Break(keyword);
    }

    private Stmt forStatement() {
        // We do not use a separate construct for the for loop
        // Instead we de suagr it into a while loop
        Boolean enclosingLoop = inLoop;
        inLoop = true;

        try {
            consume(LEFT_PAREN, "Expect '(' after 'for'.");
            Stmt initializer;
            if (match(SEMICOLON)) {
                initializer = null;
            } else if (match(VAR)) {
                initializer = varDeclaration();
            } else {
                initializer = expressionStatement();
            }

            Expr condition = null;
            if (!check(SEMICOLON)) {
                condition = expression();
            }
            consume(SEMICOLON, "Expect ';' after loop condition.");

            Expr increment = null;
            if (!check(RIGHT_PAREN)) {
                increment = expression();
            }
            consume(RIGHT_PAREN, "Expect ')' after for clauses.");
            Stmt body = statement();

            // increment is executed after body so we attach increment at end
            // of body
            if (increment != null) {
                body = new Stmt.Block(Arrays.asList(
                        body,
                        new Stmt.Expression(increment)));
            }

            if (condition == null) {
                condition = new Expr.Literal(true);
            }
            body = new Stmt.While(condition, body);

            if (initializer != null) {
                body = new Stmt.Block(Arrays.asList(initializer, body));
            }

            return body;
        } finally {
            inLoop = enclosingLoop;
        }
    }

    private Stmt whileStatement() {
        Boolean enclosingLoop = inLoop;
        inLoop = true;

        try {
            consume(LEFT_PAREN, "Expect '(' after if.");
            Expr condition = expression();
            consume(RIGHT_PAREN, "Expect ')' after if condition.");

            Stmt body = statement();
            return new Stmt.While(condition, body);
        } finally {
            inLoop = enclosingLoop;
        }
    }

    private Stmt ifStatement() {
        consume(LEFT_PAREN, "Expect '(' after if.");
        Expr condition = expression();
        consume(RIGHT_PAREN, "Expect ')' after if condition");

        Stmt thenBranch = statement();
        Stmt elseBranch = null;
        if (match(ELSE)) {
            elseBranch = statement();
        }
        return new Stmt.If(condition, thenBranch, elseBranch);
    }

    private List<Stmt> block() {
        List<Stmt> statements = new ArrayList<>();
        while (!check(RIGHT_BRACE) && !isAtEnd()) {
            statements.add(declaration());
        }
        consume(RIGHT_BRACE, "Expect '}' after the block");
        return statements;
    }

    private Stmt printStatement() {
        Expr value = expression();
        consume(SEMICOLON, "Expect ';' after value.");
        return new Stmt.Print(value);
    }

    private Stmt expressionStatement() {
        Expr expr = expression();
        consume(SEMICOLON, "Expect ';' after value.");
        return new Stmt.Expression(expr);
    }

    private Expr expression() {
        return comma();
    }

    private Expr comma() {
        Expr expr = assignment();
        while (match(COMMA)) {
            Token operator = previous();
            Expr right = assignment();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr assignment() {
        Expr expr = conditional();
        if (match(EQUAL)) {
            Token equals = previous();
            Expr value = assignment();
            if (expr instanceof Expr.Variable) {
                Token name = ((Expr.Variable) expr).name;
                return new Expr.Assign(name, value);
            }

            error(equals, "Invalid Assignment target.");
        }
        return expr;
    }

    private Expr conditional() {
        Expr expr = or();
        if (match(QUESTION)) {
            Expr trueExpr = conditional();
            consume(COLON, " Expect ':' after expression");
            Expr falseExpr = conditional();
            expr = new Expr.Ternary(expr, trueExpr, falseExpr);
        }
        return expr;
    }

    private Expr or() {
        Expr expr = and();
        while (match(OR)) {
            Token operator = previous();
            Expr right = and();
            expr = new Expr.Logical(expr, operator, right);
        }
        return expr;
    }

    private Expr and() {
        Expr expr = equality();
        while (match(AND)) {
            Token operator = previous();
            Expr right = equality();
            expr = new Expr.Logical(expr, operator, right);
        }
        return expr;
    }

    private Expr equality() {
        Expr expr = comparison();

        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr comparison() {
        Expr expr = term();

        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr term() {
        Expr expr = factor();

        while (match(MINUS, PLUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr factor() {
        Expr expr = unary();

        while (match(SLASH, STAR)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr unary() {
        if (match(BANG, MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }
        return primary();
    }

    private Expr primary() {
        if (match(FALSE)) {
            return new Expr.Literal(false);
        }
        if (match(TRUE)) {
            return new Expr.Literal(true);
        }
        if (match(NIL)) {
            return new Expr.Literal(null);
        }

        if (match(NUMBER, STRING)) {
            return new Expr.Literal(previous().literal);
        }

        if (match(IDENTIFIER)) {
            return new Expr.Variable(previous());
        }

        if (match(LEFT_PAREN)) {
            Expr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression");
            return new Expr.Grouping(expr);
        }

        throw error(peek(), "Expect Expression.");
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) {
            return advance();
        }
        throw error(peek(), message);
    }

    private ParseError error(Token token, String message) {
        Lox.error(token, message);
        return new ParseError();
    }

    @SuppressWarnings("incomplete-switch")
    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().type == SEMICOLON) {
                return;
            }

            switch (peek().type) {
                case CLASS, FUN, VAR, FOR, IF, WHILE, PRINT, RETURN -> {
                    return;
                }
            }

            advance();
        }
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) {
            return false;
        }
        return peek().type == type;
    }

    // consumes a token
    private Token advance() {
        if (!isAtEnd()) {
            ++current;
        }
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    // returns token yet to be consumed
    private Token peek() {
        return tokens.get(current);
    }

    // return the last consumed token
    private Token previous() {
        return tokens.get(current - 1);
    }
}
