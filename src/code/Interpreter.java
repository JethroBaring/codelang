package code;

import java.util.List;

import code.Expr.Assign;
import code.Expr.Binary;
import code.Expr.Grouping;
import code.Expr.Literal;
import code.Expr.Logical;
import code.Expr.Unary;
import code.Expr.Variable;
import code.Stmt.Block;
import code.Stmt.Bool;
import code.Stmt.Char;
import code.Stmt.Expression;
import code.Stmt.Float;
import code.Stmt.Function;
import code.Stmt.If;
import code.Stmt.Int;
import code.Stmt.Print;
import code.Stmt.Scan;
import code.Stmt.While;
import java.util.Scanner;

public class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Object> {

    private Environment environment = new Environment();

    void interpret(List<Stmt> statements) {
        try {
            for (Stmt statement : statements) {
                execute(statement);
            }
        } catch (RuntimeError e) {
            Code.runtimeError(e);
        }
    }

    @Override
    public Object visitBinaryExpr(Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case GREATER_THAN:
                checkNumberOperands(expr.operator, left, right);
                if (left instanceof Integer && right instanceof Integer) {
                    // return formatBoolean((int) left > (int) right);
                    return (int) left > (int) right;
                } else {
                    return (int) left < (int) right;
                    // return formatBoolean((double) left > (double) right);
                }
            case GREATER_THAN_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                if (left instanceof Integer && right instanceof Integer) {
                    // return formatBoolean((int) left >= (int) right);
                    return (int) left >= (int) right;
                } else {
                    // return formatBoolean((double) left >= (double) right);
                    return (int) left >= (int) right;
                }
            case LESS_THAN:
                checkNumberOperands(expr.operator, left, right);
                if (left instanceof Integer && right instanceof Integer) {
                    // return formatBoolean((int) left < (int) right);
                    return (int) left < (int) right;
                } else {
                    // return formatBoolean((double) left < (double) right);
                    return (int) left < (int) right;
                }
            case LESS_THAN_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                if (left instanceof Integer && right instanceof Integer) {
                    // return formatBoolean((int) left <= (int) right);
                    return (int) left <= (int) right;
                } else {
                    // return formatBoolean((double) left <= (double) right);
                    return (int) left <= (int) right;
                }
            case MINUS:
                checkNumberOperands(expr.operator, left, right);

                if (left instanceof Integer && right instanceof Integer) {
                    return (int) left - (int) right;
                } else {
                    return (double) left - (double) right;
                }
            case SLASH:
                checkNumberOperands(expr.operator, left, right);
                if (left instanceof Integer && right instanceof Integer) {
                    if ((int) right == 0) {
                        throw new RuntimeError(expr.operator, "Cannot divide by zero.");
                    } else {
                        return (int) left / (int) right;
                    }
                } else {
                    if ((double) right == 0) {
                        throw new RuntimeError(expr.operator, "Cannot divide by zero.");
                    } else {
                        return (double) left / (double) right;
                    }
                }
            case STAR:
                checkNumberOperands(expr.operator, left, right);
                if (left instanceof Integer && right instanceof Integer) {
                    return (int) left * (int) right;
                } else {
                    return (double) left * (double) right;
                }
            case PLUS:
                checkNumberOperands(expr.operator, left, right);
                if (left instanceof Integer && right instanceof Integer) {
                    return (int) left + (int) right;
                } else if (left instanceof Double && right instanceof Double) {
                    return (double) left * (double) right;
                }
                break;
            case AMPERSAND:
                return left.toString() + right.toString();
            case MODULO:
                checkNumberOperands(expr.operator, left, right);
                if (left instanceof Integer && right instanceof Integer) {
                    return (int) left % (int) right;
                } else {
                    return (double) left % (double) right;
                }
            case EQUAL_EQUAL:
                return isEqual(left, right);
            case NOT_EQUAL:
                return !isEqual(left, right);
            default:
                break;
        }

        return null;
    }

    @Override
    public Object visitGroupingExpr(Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitLiteralExpr(Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitUnaryExpr(Unary expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case NOT:
                return !isTruthy(right);
            case MINUS:
                checkNumberOperand(expr.operator, right);
                if (right instanceof Integer) {
                    return -(int) right;
                } else {
                    return -(double) right;
                }
            case PLUS:
                checkNumberOperand(expr.operator, right);
                if (right instanceof Integer) {
                    return +(int) right;
                } else {
                    return +(double) right;
                }
            default:
                break;
        }

        return null;
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private void execute(Stmt stmt) {
        stmt.accept(this);
    }

    void executeBlock(List<Stmt> statements, Environment environment) {
        Environment previous = this.environment;
        try {
            this.environment = environment;
            for (Stmt statement : statements) {
                execute(statement);
            }
        } finally {
            this.environment = previous;
        }

    }

    private boolean isTruthy(Object object) {
        if (object == null)
            return false;
        if (object instanceof Boolean)
            return (boolean) object;
        return true;
    }

    private String formatBoolean(Boolean value) {
        return Boolean.toString(value).toUpperCase();
    }

    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null)
            return true;
        if (a == null)
            return false;

        return a.equals(b);
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double || operand instanceof Integer)
            return;
        throw new RuntimeError(operator, "Operand must be an integer or a float nuimber.");
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if ((left instanceof Integer && right instanceof Integer)
                || (left instanceof Double && right instanceof Double))
            return;
        throw new RuntimeError(operator, "Operand must be an integer or a float nuimber.");
    }

    private String stringify(Object object) {
        if (object == null)
            return "null";

        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }

            return text;
        }

        return object.toString();
    }

    @Override
    public Void visitExpressionStmt(Expression stmt) {
        evaluate(stmt.expression);
        return null;
    }

    @Override
    public Void visitPrintStmt(Print stmt) {
        Object value = evaluate(stmt.expression);
        // if ((boolean) value == true || (boolean) value == false) {
        // System.out.println(stringify(value).toUpperCase());
        // } else {
        // System.out.println(stringify(value));
        // }
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Object visitStringStmt(Stmt.String stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
            if (value instanceof String) {
                environment.define(stmt.name.lexeme, value);
            } else {
                throw new RuntimeError(stmt.name, "Value '" + value + "' is not of type String.");
            }
        }
        environment.define(stmt.name.lexeme, value);
        return null;
    }

    @Override
    public Object visitIntStmt(Int stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
            if (value instanceof Integer) {
                environment.define(stmt.name.lexeme, value);
            } else {
                throw new RuntimeError(stmt.name, "Value '" + value + "' is not of type Integer.");
            }
        }
        environment.define(stmt.name.lexeme, value);
        return null;
    }

    @Override
    public Object visitFloatStmt(Float stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
            if (value instanceof Double) {
                environment.define(stmt.name.lexeme, value);
            } else {
                throw new RuntimeError(stmt.name, "Value '" + value + "' is not of type Float.");
            }
        }
        environment.define(stmt.name.lexeme, value);
        return null;
    }

    @Override
    public Object visitCharStmt(Char stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
            if (value instanceof Character) {
                environment.define(stmt.name.lexeme, value);
            } else {
                throw new RuntimeError(stmt.name, "Value '" + value + "' is not of type Character.");
            }
        }
        environment.define(stmt.name.lexeme, value);
        return null;
    }

    @Override
    public Object visitBoolStmt(Bool stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
            if (value instanceof Boolean) {
                environment.define(stmt.name.lexeme, value);
            } else {
                throw new RuntimeError(stmt.name, "Value '" + value + "' is not of type Boolean.");
            }
        }
        environment.define(stmt.name.lexeme, value);
        return null;
    }

    @Override
    public Object visitVariableExpr(Variable expr) {
        return environment.get(expr.name);
    }

    @Override
    public Object visitAssignExpr(Assign expr) {
        Object value = evaluate(expr.value);
        environment.assign(expr.name, value);
        return value;
    }

    @Override
    public Object visitBlockStmt(Block stmt) {
        executeBlock(stmt.statements, new Environment(environment));

        return null;
    }

    @Override
    public Object visitIfStmt(If stmt) {
        if (isTruthy(evaluate(stmt.condition))) {
            for (Stmt statement : stmt.thenBranch) {
                execute(statement);
            }
        } else if (stmt.elseBranch != null) {
            for (Stmt statement : stmt.elseBranch) {
                execute(statement);
            }
        }

        return null;
    }

    @Override
    public Object visitLogicalExpr(Logical expr) {
        Object left = evaluate(expr.left);

        if (expr.operator.type == TokenType.OR) {
            if (isTruthy(left))
                return left;
        } else {
            if (!isTruthy(left))
                return left;
        }

        return evaluate(expr.right);
    }

    @Override
    public Object visitWhileStmt(While stmt) {
        while (isTruthy(evaluate(stmt.condition))) {
            for (Stmt statement : stmt.body) {
                execute(statement);
            }
        }

        return null;
    }

    @Override
    public Object visitFunctionStmt(Function stmt) {
        throw new UnsupportedOperationException("Unimplemented method 'visitFunctionStmt'");
    }

    @Override
    public Object visitScanStmt(Scan stmt) {
        Scanner scanner = new java.util.Scanner(System.in);
        String line = scanner.nextLine();

        code.Scanner tokenizer = new code.Scanner(line);
        List<Token> tokens = tokenizer.scanTokens();

        int current = 0;
        int current2 = 0;
        while(current2 < stmt.identifiers.size()) {
            Object value = tokens.get(current).literal;
            environment.assign(stmt.identifiers.get(current2), value);
            current+=2;
            current2++;
        }
        return null;
    }
}
