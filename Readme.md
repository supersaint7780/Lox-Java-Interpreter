<div align="center">
  <img src="./Logo.png" alt="Lox Logo" />
</div>

# Lox Programming Language

This repository contains the implementation of **Lox**, a programming language built in Java while following the book *[Crafting Interpreters](https://craftinginterpreters.com/)* by Robert Nystrom. The project is an interpreter for the Lox language, showcasing features like expression evaluation, variables, and control flow.

## About Lox

Lox is a simple yet powerful dynamically typed language. It is designed to be easy to understand and fun to work with, making it a great language for learning about programming language design and implementation.

## Features

### Features Taught in the Book

The following features were implemented as guided by *Crafting Interpreters*:

- **Dynamic Typing**: Variables can hold values of any type.
- **Expressions**:
  - Arithmetic: `+`, `-`, `*`, `/`
  - Logical: `and`, `or`
  - Comparison: `==`, `!=`, `<`, `>`, `<=`, `>=`
- **Control Flow**:
  - Conditionals: `if`/`else`
  - Loops: `while`
- **Functions**: First-class functions with closures.
- **Classes and Objects**: Class-based object-oriented programming with methods.

### Features Implemented by Me

In addition to the features taught in the book, I have added the following features:

- **New Operators**:
  - Comma (`,`) (Similar to the comma operator in C)
  - Ternary (`? :`)
- **Keywords**
  - break (To exit a loop based on a given condition)

## Grammar

The grammar of the Lox language is defined in the file [Grammar.md](./Grammar.md). This document provides a comprehensive overview of the language's syntax and structure.

## Getting Started

### Prerequisites

- **Java 17 or later**: Make sure Java is installed on your system. You can check by running:
  ```bash
  java --version
  ```

### Clone the Repository

```bash
git clone https://github.com/supersaint7780/Lox-Java-Interpreter
cd Lox-Java-Interpreter
```

### Build and Run

1. **Compile the Project**:
   ```bash
   javac -d out com/lox/*.java
   ```

2. **Run the Interpreter**:
   ```bash
   java -cp out com.lox.Lox
   ```

### Usage

Once the interpreter is running, you can:

- Execute a Lox script file:
  ```bash
  java -cp out com.lox.Lox examples/hello_world.lox
  ```

- Enter the interactive prompt (REPL):
  ```bash
  java -cp out com.lox.Lox
  > print "Hello, world!";
  Hello, world!
  ```

## Acknowledgments

- *[Crafting Interpreters](https://craftinginterpreters.com/)* by Robert Nystrom, for the inspiration and guidance in building Lox.
- The open-source community for tools and libraries.