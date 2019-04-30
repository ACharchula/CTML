import ctml.interpreter.lexer.CtmlReader;
import ctml.interpreter.lexer.HtmlReader;
import ctml.interpreter.lexer.Lexer;
import ctml.structures.token.Token;
import ctml.structures.token.TokenType;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class LexerTests {

    @Test
    void isReturningHtmlContentFromStart() throws Exception {
        String input = "<";
        Token token = getTokenFromStringForHtml(input);

        assertEquals(TokenType.HTML_CONTENT, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isCtmlStartRecognized() throws Exception {
        String input = "<?";
        Token token = getTokenFromStringForHtml(input);

        assertEquals(TokenType.CTML_START, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isIdentifierRecognized() throws Exception {
        String input = "x";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.ID, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isNumberRecognized() throws Exception {
        String input = "123";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.NUMBER, token.getType());
        assertEquals(input, token.getContent());
    }
    @Test
    void isFunctionRecognized() throws Exception {
        String input = "func";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.FUNCTION, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isIfRecognized() throws Exception {
        String input = "if";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.IF, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isWhileRecognized() throws Exception {
        String input = "while";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.WHILE, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isElseRecognized() throws Exception {
        String input = "else";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.ELSE, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isReturnRecognized() throws Exception {
        String input = "return";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.RETURN, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isLessOperandRecognized() throws Exception {
        String input = "<";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.LESS, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isLessEqualsOperandRecognized() throws Exception {
        String input = "<=";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.LESS_EQUALS, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isGreaterOperandRecognized() throws Exception {
        String input = ">";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.GREATER, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isGreaterEqualsOperandRecognized() throws Exception {
        String input = ">=";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.GREATER_EQUALS, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isBrackerOpenRecognized() throws Exception {
        String input = "{";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.BRACKET_OPEN, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isBracketCloseRecognized() throws Exception {
        String input = "}";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.BRACKET_CLOSE, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isEqualsOperandRecognized() throws Exception {
        String input = "==";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.EQUALS, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isNotEqualsOperandRecognized() throws Exception {
        String input = "!=";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.NOT_EQUALS, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isAddOperandRecognized() throws Exception {
        String input = "+";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.ADD, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isSubtractOperandRecognized() throws Exception {
        String input = "-";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.SUBTRACT, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isMultiplyOperandRecognized() throws Exception {
        String input = "*";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.MULTIPLY, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isDivideOperandRecognized() throws Exception {
        String input = "/";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.DIVIDE, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isSemicolonRecognized() throws Exception {
        String input = ";";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.SEMICOLON, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isAssignRecognized() throws Exception {
        String input = "=";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.ASSIGN, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isCommaRecognized() throws Exception {
        String input = ",";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.COMMA, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isAndOperandRecognized() throws Exception {
        String input = "&&";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.AND, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isOrOperandRecognized() throws Exception {
        String input = "||";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.OR, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isUndefinedIdentifierRecognized() {
        String input = "%";

        assertThrows(Exception.class, () -> getTokenFromStringForCtml(input));
    }

    @Test
    void isParenthesisOpenRecognized() throws Exception {
        String input = "(";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.PARENTHESIS_OPEN, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isParenthesisClosedRecognized() throws Exception {
        String input = ")";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.PARENTHESIS_CLOSE, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isVoidRecognized() throws Exception {
        String input = "void";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.VOID, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isIntegerTypeRecognized() throws Exception {
        String input = "int";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.INTEGER_TYPE, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isCsvTypeRecognized() throws Exception {
        String input = "csv";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.CSV_TYPE, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isHeaderRecognized() throws Exception {
        String input = "head";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.HEADER, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isStringTypeRecognized() throws Exception {
        String input = "string";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.STRING_TYPE, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isFloatTypeRecognized() throws Exception {
        String input = "float";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.FLOAT_TYPE, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isFloatNumberRecognized() throws Exception {
        String input = "1.2345";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.FLOAT_NUMBER, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isTableRecognized() throws Exception {
        String input = "table";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.TABLE, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isRowRecognized() throws Exception {
        String input = "row";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.ROW, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isColumnRecognized() throws Exception {
        String input = "column";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.COLUMN, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isTableItemRecognized() throws Exception {
        String input = "table_item";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.TABLE_ITEM, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isListRecognized() throws Exception {
        String input = "list";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.LIST, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isListItemRecognized() throws Exception {
        String input = "list_item";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.LIST_ITEM, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isPeriodRecognized() throws Exception {
        String input = ".";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.PERIOD, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isOpendSquareBracketRecognized() throws Exception {
        String input = "[";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.SQUARE_BRACKET_OPEN, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isClosedSquareBracketRecognized() throws Exception {
        String input = "]";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.SQUARE_BRACKET_CLOSE, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isCtmlEndRecognized() throws Exception {
        String input = "?>";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.CTML_END, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isStringContentRecognized() throws Exception {
        String input = "\"content\"";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.STRING_CONTENT, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isLoadRecognized() throws Exception {
        String input = "load";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.LOAD, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isParagraphRecognized() throws Exception {
        String input = "par";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.PARAGRAPH, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isLinkRecognized() throws Exception {
        String input = "link";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.LINK, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isImageRecognized() throws Exception {
        String input = "img";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.IMAGE, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isStringFormatterRecognized() throws Exception {
        String input = "stringFormatter";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.STRING_FORMATTER, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isArrayAppendRecognized() throws Exception {
        String input = "append";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(TokenType.ARRAY_APPEND, token.getType());
        assertEquals(input, token.getContent());
    }

    @Test
    void isEnterPassedInStringContent() throws Exception {
        String input = "\"content\ncontent\"";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(input, token.getContent());
    }

    @Test
    void isQuatationMarkIsPassedInStringContent() throws Exception {
        String input = "\"content\\\"content\\\"content\"";
        Token token = getTokenFromStringForCtml(input);

        assertEquals(input, token.getContent());
    }

    private Token getTokenFromStringForCtml(String string) throws Exception {
        Lexer lexer = new Lexer(convertStringToInputStreamReader(string));
        Lexer.setLexerState(new CtmlReader());
        return lexer.nextToken();
    }

    private Token getTokenFromStringForHtml(String string) throws Exception {
        Lexer lexer = new Lexer(convertStringToInputStreamReader(string));
        Lexer.setLexerState(new HtmlReader());
        return lexer.nextToken();
    }

    private InputStream convertStringToInputStreamReader(String string) {
        return new ByteArrayInputStream(string.getBytes());
    }

}
