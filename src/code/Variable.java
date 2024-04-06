package code;

public class Variable {
    private TokenType type;
    private Object value;
    private boolean isImmutable;

    public Variable(TokenType type, Object value, boolean isImmutable) {
        this.type = type;
        this.value = value;
        this.isImmutable = isImmutable;
    }

    // Getters and setters
    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isImmutable() {
        return isImmutable;
    }

    public void setImmutable(boolean immutable) {
        isImmutable = immutable;
    }
}
