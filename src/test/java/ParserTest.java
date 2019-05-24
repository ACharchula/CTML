import ctml.interpreter.parser.Parser;
import ctml.interpreter.program.Program;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ParserTest {

    @Test
    void isFunctionProperlyParsed() throws Exception {
        parse(" func int function(int a, string b, float c, csv d, float[] e) { return a; } ");
    }

    @Test
    void isVariableDeclarationProperlyParsed() throws Exception {
        parse(" { int a; } ");
    }

    @Test
    void isIntAssignmentProperlyParsed() throws Exception {
        parse(" { int a; a = 1; } ");
    }

    @Test
    void isFloatAssignmentProperlyParsed() throws Exception {
        parse(" { float a; a = 2.0; } ");
    }

    @Test
    void isCsvAssignmentProperlyParsed() throws Exception {
        parse(" { csv a; a = load(\"file.txt\"); } ");
    }

    @Test
    void isTableAssignmentProperlyParsed() throws Exception {
        parse(" { int[] a; a = {1, 2, 3, 4}; }");
    }

    @Test
    void isTableAppendProperlyParsed() throws Exception {
        parse(" { int[] a; a.append(1, 2, 3); }");
    }

    @Test
    void isStringAssignmentProperlyParsed() throws Exception {
        parse(" { string b; b = \"abc\"; } ");
    }

    @Test
    void isAssignmentInDefinitionProperlyParsed() throws Exception {
        parse(" { int a = 1; } ");
    }

    @Test
    void isAssignmentInDefinitionUsingFunctionProperlyParsed() throws Exception {
        parse(" func int funk() {return a;} { int a = funk(); } ");
    }

    @Test
    void isExceptionThrownWhenDuplicateDeclarationOccurred() {
        assertThrows(Exception.class, () -> parse(" { int a =1; int a; } "));
    }

    @Test
    void isIndexVariableProperlyParsed() throws Exception {
        parse(" { int[] a = {1, 2, 3}; int b = a[1]; } ");
    }

    @Test
    void isCsvIndexProperlyParsed() throws Exception {
        parse(" { csv a = load(\"txt\"); string b = a[0][1]; } ");
    }

    @Test
    void isIfStatementProperlyParsed() throws Exception {
        parse(" { int a = 2; if(1) { a = 3; }  } ");
    }

    @Test
    void isIfElseStatementProperlyParsed() throws Exception {
        parse(" { int a = 2; if(0) { a = 3; } else { a = 4; } }");
    }

    @Test
    void isWhileStatementProperlyParser() throws Exception {
        parse(" { int a = 1; while(1) { a = a + 1; } }");
    }

    @Test
    void isLinkProperlyParsed() throws Exception {
        parse(" { link(\"a\", \"b\"); } ");
    }

    @Test
    void isHeaderProperlyParsed() throws Exception {
        parse(" { head(\"a\"); } ");
    }

    @Test
    void isParagraphProperlyParsed() throws Exception {
        parse(" { par(\"a\"); } ");
    }

    @Test
    void isImageProperlyParsed() throws Exception {
        parse(" { img(\"a\"); } ");
    }

    @Test
    void isListProperlyParsed() throws Exception {
        parse(" { list { list_item(1); list_item(2); } } ");
    }

    @Test
    void isTableProperlyParsed() throws Exception {
        parse(" { table {  row { column(1); column(2);} row { table_item(3); table_item(4); } } } ");
    }

    @Test
    void isMethodCallProperlyParsed() throws Exception {
        parse(" func void fun() { } { fun(); } ");
    }

    @Test
    void parseSimpleExpression() throws Exception {
        parse(" { float a = (((5.763/2.5)*2+2.45*33/2)-3)/7.218+2/3; } ");
    }

    @Test
    void parseExpression() throws Exception {
        parse(" { int a; int b; if( ((a > b) || (b < a)) && a != b ) {} }");
    }

    private Program parse(String content) throws Exception {
        String ctmlContent = "<div> <? " + content + " ?> </div>";
        Parser parser = new Parser(new ByteArrayInputStream(ctmlContent.getBytes()));
        parser.nextToken();
        return parser.parseProgram();
    }
}
