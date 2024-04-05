package code;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    final Environment enclosing;
    private final Map<String, Object> values = new HashMap<>();

    Environment() {
        enclosing = null;
    }

    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    void define(String name, Object value) {
        if (values.containsKey(name)) {

        } else {
            values.put(name, value);
        }
    }

    Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }

        if (enclosing != null)
            return enclosing.get(name);

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    void assign(Token name, Object value) {
        Object val = get(name);
        if (val != null) {
            String type = "";
            if (val instanceof String) {
                if (value instanceof String) {
                    values.put(name.lexeme, value);
                    return;
                }
                type = "String";

            } else if (val instanceof Character) {
                if (value instanceof Character) {
                    values.put(name.lexeme, value);
                    return;
                }

                type = "Character";

            } else if (val instanceof Integer) {
                if (value instanceof Integer) {
                    values.put(name.lexeme, value);
                    return;
                }
                type = "Integer";
            } else if (val instanceof Double) {
                if (value instanceof Double) {
                    values.put(name.lexeme, value);
                    return;
                }
                type = "Float";
            } else if (val instanceof Boolean) {
                if (value instanceof Boolean) {
                    values.put(name.lexeme, value);
                    return;
                }
                type = "Boolean";
            }
            throw new RuntimeError(name, "Cannot assign the value '" + value + "' to variable of type " + type + ".");
        }

        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }
}
