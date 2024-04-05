package code;

import java.util.List;

/**
 * CodeCallable
 */

public interface CodeCallable {
    Object call(Interpreter interpreter, List<Object> arguments);
}
