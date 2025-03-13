# Lox Interpreter

This project is an implementation of the Lox programming language interpreter, following the ["Crafting Interpreters"](https://craftinginterpreters.com/) book by Robert Nystrom. The Lox interpreter is written in Java and supports various features of the Lox language, including expressions, statements, functions, classes, and inheritance.

## Features

- **Expressions**: Supports binary, unary, grouping, literal, variable, assignment, logical, call, get, set, super, and this expressions.
- **Statements**: Supports expression, print, variable, block, if, while, function, return, and class statements.
- **Functions**: Allows defining and calling functions with parameters.
- **Classes**: Supports class definitions, inheritance, and method overriding.
- **Error Handling**: Provides error reporting for syntax and runtime errors.
- **REPL**: Interactive prompt for executing Lox code line by line.
- **Script Execution**: Execute Lox scripts from files.

## Project Structure

- `src/main/java/com/interpreter/lox`: Contains the main source code for the Lox interpreter.
  - `Expr.java`: Defines the abstract syntax tree (AST) for expressions.
  - `Stmt.java`: Defines the AST for statements.
  - `Lox.java`: Entry point for the interpreter.
  - `LoxCallable.java`: Interface for callable objects (functions and classes).
  - `Return.java`: Exception class for handling return statements.
  - `Parser.java`: Parses tokens into an AST.
  - `RuntimeError.java`: Exception class for runtime errors.
  - `Interpreter.java`: Evaluates the AST and executes the code.
  - `Resolver.java`: Resolves variable and function scopes.
  - `LoxInstance.java`: Represents instances of Lox classes.
  - `LoxClass.java`: Represents Lox classes.
  - `Scanner.java`: Scans source code into tokens.
  - `Environment.java`: Manages variable scopes.
  - `Token.java`: Represents tokens.
  - `LoxFunction.java`: Represents Lox functions.
  - `TokenType.java`: Enum for token types.
  - `AstPrinter.java`: Utility for printing the AST.
- `src/main/java/com/interpreter/tool`: Contains tools for generating AST classes.
  - `GenerateAst.java`: Tool for generating AST classes.


## Getting Started

### Prerequisites

- Java Development Kit (JDK) 23 or higher
- Maven

### Building the Project

1. Clone the repository:
   ```sh
   git clone https://github.com/Md-Talim/lox-interpreter.git
   cd lox-interpreter
   ```

2. Build the project using Maven:
   ```sh
   mvn clean install
   ```

### Running the Interpreter

To run the interpreter in interactive mode (REPL):
```sh
mvn exec:java -Dexec.mainClass="com.interpreter.lox.Lox"
```

To run a Lox script from a file:
```sh
mvn exec:java -Dexec.mainClass="com.interpreter.lox.Lox" -Dexec.args="test.lox"
```

## Example

Here is an example Lox script (`test.lox`):

```lox
class Doughnut {
  cook() {
    print "Fry until golden brown.";
  }
}

class BostonCream < Doughnut {
  cook() {
    super.cook();
    print "Pipe full of custard and coat with chocolate.";
  }
}

BostonCream().cook();
```

### Output

```
Fry until golden brown.
Pipe full of custard and coat with chocolate.
```
