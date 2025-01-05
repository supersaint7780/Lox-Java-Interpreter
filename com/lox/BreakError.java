package com.lox;

public class BreakError extends RuntimeException {
    BreakError() {
        super(null, null, false, false);
    }
}
