package com.lox;

import java.util.List;

interface LoxCallable {
    Object call(Interpreter interpreter, List<Object> arguments);

    // arity defines the number of arguments a function can take
    int arity();
}
