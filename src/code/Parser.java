package code;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private static class ParseError extends RuntimeException {
    }

    private final List<Token> tokens;
    private int current = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();

        consume(TokenType.BEGIN, "Expecting BEGIN.");
        consume(TokenType.CODE, "Expecting 'CODE' after BEGIN");

        while (match(TokenType.STRING, TokenType.CHAR, TokenType.INT, TokenType.FLOAT, TokenType.BOOL)) {
            statements.add(varDeclaration());
        }

        while (!isAtEnd() && !check(TokenType.END)) {
            statements.add(statement());
        }

        consume(TokenType.END, "Expecting END.");
        consume(TokenType.CODE, "Expecting 'CODE' after END");

        return statements;
    }

    private Expr expression() {
        return assignment();
    }

    private Expr assignment() {
        Expr expr = or();

        if (match(TokenType.EQUAL)) {
            Token equals = previous();
            Expr value = assignment();

            if (expr instanceof Expr.Variable) {
                Token name = ((Expr.Variable) expr).name;
                return new Expr.Assign(name, value);
            }

            error(equals, "Invalid assignment target.");
        }

        return expr;
    }

    private Expr or() {
        Expr expr = and();

        while (match(TokenType.OR)) {
            Token operator = previous();
            Expr right = and();
            expr = new Expr.Logical(expr, operator, right);
        }

        return expr;
    }

    private Expr and() {
        Expr expr = equality();
        while (match(TokenType.AND)) {
            Token operator = previous();
            Expr right = equality();
            expr = new Expr.Logical(expr, operator, right);
        }

        return expr;
    }

    // private Stmt declaration() {
    // try {
    // if (match(TokenType.STRING, TokenType.CHAR, TokenType.INT, TokenType.FLOAT,
    // TokenType.BOOL)) {
    // return varDeclaration();
    // }

    // return statement();
    // } catch (ParseError error) {
    // synchronize();
    // return null;
    // }
    // }

    private Stmt statement() {
        if (match(TokenType.DISPLAY)) {
            consume(TokenType.COLON, "Expecting ':' after DISPLAY");
            return displayStatement();
        }

        if (match(TokenType.IF)) {
            return ifStatement();
        } else if (match(TokenType.WHILE)) {
            return whileStatement();
        }
        return expressionStatement();
    }

    private Stmt displayStatement() {
        Expr value = expression();
        // consume(TokenType.SEMICOLON, "Expect ';' after value");
        return new Stmt.Print(value);
    }

    private Stmt ifStatement() {
        consume(TokenType.LEFT_PARENTHESIS, "Expecting '(' after IF.");
        Expr condition = expression();
        consume(TokenType.RIGHT_PARENTHESIS, "Expecting ')' after an expression");

        consume(TokenType.BEGIN, "Expecting a BEGIN after Condition.");
        consume(TokenType.IF, "Expecting an IF after BEGIN");


        List<Stmt> thenBranch = new ArrayList<>();
        while (!isAtEnd() && !check(TokenType.END)) {
            thenBranch.add(statement());
        }

        consume(TokenType.END, "Expecting END after a statement.");
        consume(TokenType.IF, "Expecting IF after END");

        List<Stmt> elseBranch = null;

        if (match(TokenType.ELSE)) {
            elseBranch = new ArrayList<>();
            consume(TokenType.BEGIN, "Expecting a BEGIN after Condition.");
            consume(TokenType.IF, "Expecting an IF after BEGIN");

            while (!isAtEnd() && !check(TokenType.END)) {
                elseBranch.add(statement());
            }

            consume(TokenType.END, "Expecting END after a statement.");
            consume(TokenType.IF, "Expecting IF after END");

        }

        return new Stmt.If(condition, thenBranch, elseBranch);

    }

    private Stmt whileStatement() {
        consume(TokenType.LEFT_PARENTHESIS, "Expecting '(' after IF.");
        Expr condition = expression();
        consume(TokenType.RIGHT_PARENTHESIS, "Expecting ')' after an expression");

        consume(TokenType.BEGIN, "Expecting a BEGIN after Condition.");
        consume(TokenType.WHILE, "Expecting an IF after BEGIN");

        List<Stmt> body = new ArrayList<>();
        while (!isAtEnd() && !check(TokenType.END)) {
            body.add(statement());
        }

        consume(TokenType.END, "Expecting END after a statement.");
        consume(TokenType.WHILE, "Expecting IF after END");

        return new Stmt.While(condition, body);

    }

    private Stmt varDeclaration() {
        Token token = previous();
        Token name = consume(TokenType.IDENTIFIER, "Expect variable name.");
        Expr initializer = null;

        if (match(TokenType.EQUAL)) {
            initializer = expression();
        }

        switch (token.type) {
            case STRING:
                return new Stmt.String(name, initializer);
            case CHAR:
                return new Stmt.Char(name, initializer);
            case INT:
                return new Stmt.Int(name, initializer);
            case FLOAT:
                return new Stmt.Float(name, initializer);
            default:
                return new Stmt.Bool(name, initializer);
        }
    }

    private Stmt expressionStatement() {
        Expr expr = expression();
        // consume(TokenType.SEMICOLON, "Expect ';' after expression");
        return new Stmt.Expression(expr);
    }

    private List<Stmt> block(TokenType type) {
        String token = "";

        switch (type) {
            case IF:
                token = "IF";
                break;
            case WHILE:
                token = "WHILE";
            default:
                break;
        }

        consume(TokenType.LEFT_PARENTHESIS, "Expecting '(' after " + token + ".");
        Expr expr = expression();
        consume(TokenType.RIGHT_PARENTHESIS, "Expecting ')' after an expression");

        consume(TokenType.BEGIN, "Expecting a BEGIN after " + token + " Condition.");
        consume(type, "Expecting an " + token + " after BEGIN");

        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd() && !check(TokenType.END)) {
            statements.add(statement());
        }

        consume(TokenType.END, "Expecting END.");
        consume(TokenType.CODE, "Expecting " + token + " after END");

        return statements;
    }

    private Expr equality() {
        Expr expr = comparison();
        while (match(TokenType.NOT_EQUAL, TokenType.EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr comparison() {
        Expr expr = term();
        while (match(TokenType.GREATER_THAN, TokenType.GREATER_THAN_EQUAL, TokenType.LESS_THAN,
                TokenType.LESS_THAN_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr term() {
        Expr expr = factor();

        while (match(TokenType.MINUS, TokenType.PLUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr factor() {
        Expr expr = unary();
        while (match(TokenType.SLASH, TokenType.STAR, TokenType.MODULO)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr unary() {
        if (match(TokenType.NOT, TokenType.MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }

        return primary();
    }

    private Expr primary() {
        if (match(TokenType.TRUE_LITERAL))
            return new Expr.Literal(true);
        if (match(TokenType.FALSE_LITERAL))
            return new Expr.Literal(false);
        if (match(TokenType.NULL))
            return new Expr.Literal(null);
        if (match(TokenType.STRING_LITERAL, TokenType.CHAR_LITERAL,
                TokenType.INT_LITERAL, TokenType.FLOAT_LITERAL))
            return new Expr.Literal(previous().literal);
        if (match(TokenType.LEFT_PARENTHESIS)) {
            Expr expr = expression();
            consume(TokenType.RIGHT_PARENTHESIS, "Expect ')' after expression");
            return new Expr.Grouping(expr);
        }
        if (match(TokenType.IDENTIFIER)) {
            return new Expr.Variable(previous());
        }
        throw error(peek(), "Expect expression.");
    }

    private Token consume(TokenType type, String message) {
        if (check(type))
            return advance();

        throw error(peek(), message);
    }

    private ParseError error(Token token, String message) {
        Code.error(token, message);
        return new ParseError();
    }

    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().type == TokenType.SEMICOLON)
                return;
            switch (peek().type) {
                case STRING:
                case INT:
                case CHAR:
                case BOOL:
                case DISPLAY:
                case SCAN:
                    return;
                default:
                    break;
            }
            advance();
        }
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token advance() {
        if (!isAtEnd())
            current++;

        return previous();
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
        if (isAtEnd())
            return false;
        return peek().type == type;
    }

}
