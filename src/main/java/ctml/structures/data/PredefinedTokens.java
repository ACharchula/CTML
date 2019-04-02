package ctml.structures.data;

import java.util.HashMap;

import static ctml.structures.data.TokenType.*;

public class PredefinedTokens {

    public static final HashMap<String, TokenType> KEYWORDS = new HashMap<String, TokenType>() {{
        put("func", FUNCTION);
        put("if", IF);
        put("while", WHILE);
        put("else", ELSE);
        put("return", RETURN);
        put("void", VOID);
        put("int", INTEGER_TYPE);
        put("Csv", CSV_TYPE);
        put("head", HEADER);
        put("String", STRING_TYPE);
        put("float", FLOAT_TYPE);
        put("Table", TABLE);
        put("Row", ROW);
        put("column", COLUMN);
        put("table_item", TABLE_ITEM);
        put("load", LOAD);
        put("par", PARAGRAPH);
        put("link", LINK);
        put("img", IMAGE);
        put("append", ARRAY_APPEND);
        put("stringFormatter", STRING_FORMATTER);


    }};

    public static final HashMap<String, TokenType> OPERATORS = new HashMap<String, TokenType>() {{
        put("=", ASSIGN);
        put("==", EQUALS);
        put(">=", GREATER_EQUALS);
        put("<=", LESS_EQUALS);
        put("&&", AND);
        put("||", OR);
        put(",", COMMA);
        put(".", PERIOD);
        put(";", SEMICOLON);
        put("+", ADD);
        put("-", SUBTRACT);
        put("*", MULTIPLY);
        put("/", DIVIDE);
        put("(", PARENTHESIS_OPEN);
        put(")", PARENTHESIS_CLOSE);
        put("{", BRACKET_OPEN);
        put("}", BRACKET_CLOSE);
        put("<", LESS);
        put(">", GREATER);
        put("!=", NOT_EQUALS);
        put("\"", QUOTATION_MARK);
        put("\n", NEXT_LINE);
        put("[", SQUARE_BRACKET_OPEN);
        put("]", SQUARE_BRACKET_CLOSE);
        put("<?", CTML_START);
        put("?>", CTML_END);
        put("//", COMMENT);

    }};

}
