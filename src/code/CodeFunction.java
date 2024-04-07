package code;

import java.util.List;

public class CodeFunction implements CodeCallable {
    private final Stmt.Function declaration;

    CodeFunction(Stmt.Function declaration) {
        this.declaration = declaration;
    }

    @Override
    public int arity() {
        return declaration.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(interpreter.globals);

        for (int i = 0; i < declaration.params.size(); i++) {
            TokenType type = declaration.params.get(i).type.type;
            Object value = arguments.get(i);
            if (type == TokenType.STRING) {
                environment.define(declaration.params.get(i).name.lexeme, value, declaration.params.get(i).type.type,
                        true);
            } else if (type == TokenType.CHAR) {
                environment.define(declaration.params.get(i).name.lexeme, value, declaration.params.get(i).type.type,
                        true);

            } else if (type == TokenType.INT) {
                environment.define(declaration.params.get(i).name.lexeme, value, declaration.params.get(i).type.type,
                        true);

            } else if (type == TokenType.FLOAT) {
                environment.define(declaration.params.get(i).name.lexeme, value, declaration.params.get(i).type.type,
                        true);

            } else {
                environment.define(declaration.params.get(i).name.lexeme, value, declaration.params.get(i).type.type,
                        true);

            }
        }

        try {
            interpreter.executeBlock(declaration.body, environment);
        } catch (Return returnValue) {
            return returnValue.value;
        }
        
        return null;
    }

}
