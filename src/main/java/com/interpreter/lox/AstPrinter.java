package com.interpreter.lox;

import java.util.List;

import com.interpreter.lox.Expr.Get;
import com.interpreter.lox.Expr.Set;
import com.interpreter.lox.Expr.Super;
import com.interpreter.lox.Expr.This;
import com.interpreter.lox.Stmt.Return;

class AstPrinter implements Expr.Visitor<String>, Stmt.Visitor<String> {
    String print(Expr expr) {
        return expr.accept(this);
    }

    String print(Stmt stmt) {
        return stmt.accept(this);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesizeExprs(expr.operator.lexeme,
                expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesizeExprs("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) {
            return "nil";
        }
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesizeExprs(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return expr.name.lexeme;
    }

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return parenthesizeParts("=", expr.name.lexeme, expr.value);
    }

    @Override
    public String visitLogicalExpr(Expr.Logical expr) {
        return parenthesizeExprs(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitCallExpr(Expr.Call expr) {
        return parenthesizeParts("call", expr.callee, expr.arguments);
    }

    @Override
    public String visitExpressionStmt(Stmt.Expression stmt) {
        return parenthesizeExprs(";", stmt.expression);
    }

    @Override
    public String visitPrintStmt(Stmt.Print stmt) {
        return parenthesizeExprs("print", stmt.expression);
    }

    @Override
    public String visitReturnStmt(Return stmt) {
        if (stmt.value == null)
            return "(return)";
        return parenthesizeExprs("return", stmt.value);
    }

    @Override
    public String visitVarStmt(Stmt.Var stmt) {
        if (stmt.initializer == null) {
            return parenthesizeParts("var", stmt.name);
        }

        return parenthesizeParts("var", stmt.name, "=", stmt.initializer);
    }

    @Override
    public String visitBlockStmt(Stmt.Block stmt) {
        StringBuilder builder = new StringBuilder();
        builder.append("(block ");

        for (Stmt statement : stmt.statements) {
            builder.append(statement.accept(this));
        }

        builder.append(")");
        return builder.toString();
    }

    @Override
    public String visitIfStmt(Stmt.If stmt) {
        if (stmt.elseBranch == null) {
            parenthesizeParts("if", stmt.condition, stmt.thenBranch);
        }

        return parenthesizeParts("if-else", stmt.condition, stmt.thenBranch, stmt.elseBranch);
    }

    @Override
    public String visitWhileStmt(Stmt.While stmt) {
        return parenthesizeParts("while", stmt.condition, stmt.body);
    }

    @Override
    public String visitFunctionStmt(Stmt.Function stmt) {
        StringBuilder builder = new StringBuilder();
        builder.append("(fun " + stmt.name.lexeme + "(");

        for (Token param : stmt.params) {
            if (param != stmt.params.get(0))
                builder.append(" ");
            builder.append(param.lexeme);
        }

        builder.append(") ");

        for (Stmt body : stmt.body) {
            builder.append(body.accept(this));
        }

        builder.append(")");
        return builder.toString();
    }

    @Override
    public String visitClassStmt(Stmt.Class stmt) {
        StringBuilder builder = new StringBuilder();
        builder.append("(class" + stmt.name.lexeme);

        if (stmt.superclass != null) {
            builder.append(" < " + print(stmt.superclass));
        }

        for (Stmt.Function method : stmt.methods) {
            builder.append(" " + print(method));
        }

        builder.append(")");
        return builder.toString();
    }

    @Override
    public String visitGetExpr(Get expr) {
        return parenthesizeParts(".", expr.object, expr.name.lexeme);
    }

    @Override
    public String visitSetExpr(Set expr) {
        return parenthesizeParts("super", expr.object, expr.name.lexeme, expr.value);
    }

    @Override
    public String visitSuperExpr(Super expr) {
        return parenthesizeParts("super", expr.method);
    }

    @Override
    public String visitThisExpr(This expr) {
        return "this";
    }

    private String parenthesizeExprs(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }

    private String parenthesizeParts(String name, Object... parts) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        transform(builder, parts);
        builder.append(")");

        return builder.toString();
    }

    private void transform(StringBuilder builder, Object... parts) {
        for (Object part : parts) {
            builder.append(" ");
            if (part instanceof Expr) {
                builder.append(((Expr) part).accept(this));
            } else if (part instanceof Stmt) {
                builder.append(((Stmt) part).accept(this));
            } else if (part instanceof Token) {
                builder.append(((Token) part).lexeme);
            } else if (part instanceof List<?>) {
                transform(builder, ((List<?>) part).toArray());
            } else {
                builder.append(part);
            }
        }
    }

    public static void main(String[] args) {
        Expr expression = new Expr.Binary(
                new Expr.Unary(new Token(TokenType.MINUS, "-", null, 1), new Expr.Literal(123)),
                new Token(TokenType.STAR, "*", null, 1),
                new Expr.Grouping(new Expr.Literal(45.67)));

        System.out.println(new AstPrinter().print(expression));
    }
}
