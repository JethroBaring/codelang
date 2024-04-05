package code;

enum TokenType {
    // Single character tokens
    LEFT_PARENTHESIS,
    RIGHT_PARENTHESIS,
    LEFT_BRACE,
    RIGHT_BRACE,
    LEFT_BRACKET,
    RIGHT_BRACKET,
    COMMA,
    COLON,
    AMPERSAND,
    SLASH,
    PLUS,
    MINUS,
    STAR,
    HASH,
    SEMICOLON,

    // One or two character tokens
    NOT,
    NOT_EQUAL,
    EQUAL,
    EQUAL_EQUAL,
    GREATER_THAN,
    GREATER_THAN_EQUAL,
    LESS_THAN,
    LESS_THAN_EQUAL,
    // Literals
    IDENTIFIER,
    STRING,
    INT,
    FLOAT,
    CHAR,
    BOOL,
    NULL,
    STRING_LITERAL,
    INT_LITERAL,
    FLOAT_LITERAL,
    CHAR_LITERAL,
    TRUE_LITERAL,
    FALSE_LITERAL,

    // Keywords
    BEGIN,
    CODE,
    END,
    DISPLAY,
    SCAN,

    EOF,
}
