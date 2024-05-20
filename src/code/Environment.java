package code;

import java.util.HashMap;
import java.util.Map;
public class Environment {
    final Environment enclosing;
    private final Map<String, Variable> values = new HashMap<>();

    Environment() {
        enclosing = null;
    }

    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    void define(String name, Object value, TokenType type, boolean isImmutable) {
        if (!values.containsKey(name)) {
            values.put(name, new Variable(type, value, isImmutable));
        } else {
            throw new RuntimeError(new Token(type, name, type, 0), "Variable " + name + " is already defined.");
        }
    }

    void define(String name, Object value) {
        values.put(name, new Variable(null, value, true));
    }

    Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme).getValue();
        }

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    TokenType getType(Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme).getType();
        }

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    Boolean getMutability(Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme).isMutable();
        }

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    @SuppressWarnings("incomplete-switch")
    void assign(Token name, Object value) {
        TokenType type = getType(name);
        boolean mutable = getMutability(name);
        if (mutable) {
            switch (type) {
                case STRING:
                    if (value instanceof String) {
                        values.put(name.lexeme, new Variable(TokenType.STRING, value, mutable));
                        return;
                    } else {
                        throw new RuntimeError(name,
                                "Cannot assign the value '" + value + "' to variable of type " + type + ".");
                    }
                case CHAR:
                    if (value instanceof Character) {
                        values.put(name.lexeme, new Variable(TokenType.CHAR, value, mutable));
                        return;
                    } else {
                        throw new RuntimeError(name,
                                "Cannot assign the value '" + value + "' to variable of type " + type + ".");
                    }
                case INT:
                    if (value instanceof Integer) {
                        values.put(name.lexeme, new Variable(TokenType.INT, value, mutable));
                        return;
                    } else {
                        throw new RuntimeError(name,
                                "Cannot assign the value '" + value + "' to variable of type " + type + ".");
                    }
                case FLOAT:
                    if (value instanceof Double) {
                        values.put(name.lexeme, new Variable(TokenType.FLOAT, value, mutable));
                        return;
                    } else {
                        throw new RuntimeError(name,
                                "Cannot assign the value '" + value + "' to variable of type " + type + ".");
                    }
                case BOOL:
                    if (value instanceof Boolean) {
                        values.put(name.lexeme, new Variable(TokenType.BOOL, value, mutable));
                        return;
                    } else {
                        throw new RuntimeError(name,
                                "Cannot assign the value '" + value + "' to variable of type " + type + ".");
                    }
            }
            
        } else {
            throw new RuntimeError(name,
                    "Cannot assign the value '" + value + "' to an immutable variable");
        }

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }
}
