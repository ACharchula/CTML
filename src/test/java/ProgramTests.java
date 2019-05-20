import ctml.interpreter.Interpreter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class ProgramTests {

    private static String testFileDir = "test.html";

    @AfterAll
    static void deleteTestFile() {
        File file = new File(testFileDir);
        file.delete();
    }

    @Test
    void isHtmlPassedToFile() throws Exception {
        runInterpreter("<div> <? {} ?> </div>");
        assertEquals("<div> </div>", getResult());

    }

    @Test
    void isCtmlExcludedFromFile() throws Exception {
        runInterpreter("<div> <? {int a = 1; if(a == 2) { } } ?> </div>");
        assertEquals("<div> </div>", getResult());

    }

    @Test
    void isHeaderWorking() throws Exception {
        runInterpreter("<? { head(\"a\"); } ?>");
        assertTrue(getResult().contains("<h1>a</h1>"), getResult());
    }

    @Test
    void isParagraphWorking() throws Exception {
        runInterpreter("<? { par(\"a\"); } ?>");
        assertTrue(getResult().contains("<p>a</p>"), getResult());
    }

    @Test
    void isImageWorking() throws Exception {
        runInterpreter("<? { img(\"a\"); } ?>");
        assertTrue(getResult().contains("<img src=\"a\">"), getResult());
    }

    @Test
    void isLinkWorking() throws Exception {
        runInterpreter("<? { link(\"a\", \"b\"); } ?>");
        assertTrue(getResult().contains("<a href=a>b</a>"), getResult());
    }

    @Test
    void isTableWorking() throws Exception {
        runInterpreter("<? { table { row { column(\"a\"); } row { table_item(\"b\"); } } } ?>");
        assertTrue(getResult().contains("<table style=\"width:100%\" border=\"1\">\n" +
                "<tr>\n" +
                "<th>a</th>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>b</td>\n" +
                "</tr>\n" +
                "</table>"), getResult());
    }

    @Test
    void isListWorking() throws Exception {
        runInterpreter("<? { list { list_item(\"a\"); list_item(\"b\"); } } ?>");
        assertTrue(getResult().contains("<ul>\n" +
                "<li>a</li>\n" +
                "<li>b</li>\n" +
                "</ul>"), getResult());
    }

    @Test
    void isIntAssignmentWorking() throws Exception {
        runInterpreter("<? { int a = 1; par(a); } ?>");
        assertTrue(getResult().contains("<p>1</p>"), getResult());
    }

    @Test
    void isStringAssignmentWorking() throws Exception {
        runInterpreter("<? { string a = \"abc\"; par(a); } ?>");
        assertTrue(getResult().contains("<p>abc</p>"), getResult());
    }

    @Test
    void areQuotesInStringPassedToHtml() throws Exception {
        runInterpreter("<? { string a = \"\\\"abc\\\"\"; par(a); } ?>");
        assertTrue(getResult().contains("<p>\"abc\"</p>"), getResult());
    }

    @Test
    void isFloatAssignmentWorking() throws Exception {
        runInterpreter("<? { float a = 1.23; par(a); } ?>");
        assertTrue(getResult().contains("<p>1.23</p>"));
    }

    @Test
    void isIntTableAssignmentWorking() throws Exception {
        runInterpreter("<? { int[] a = {1, 2}; par(a[0]); par(a[1]); } ?>");
        assertTrue(getResult().contains("<p>1</p>\n<p>2</p>"), getResult());
    }

    @Test
    void isStringTableAssignmentWorking() throws Exception {
        runInterpreter("<? { string[] a = { \"a\", \"b\" }; par(a[0]); par(a[1]); } ?>");
        assertTrue(getResult().contains("<p>a</p>\n<p>b</p>"), getResult());
    }

    @Test
    void isFloatTableAssignmentWorking() throws Exception {
        runInterpreter("<? { float[] a = { 1.1, 2.1 }; par(a[0]); par(a[1]); } ?>");
        assertTrue(getResult().contains("<p>1.1</p>\n<p>2.1</p>"), getResult());
    }

    @Test
    void isExceptionThrownWhenStringToIntAssignment() {
        assertThrows(Exception.class, () -> runInterpreter(
                "<? { int a = \"a\"; } ?>"));
    }

    @Test
    void isExceptionThrownWhenStringToFloatAssignment() {
        assertThrows(Exception.class, () -> runInterpreter(
                "<? { float a = \"a\"; } ?>"));
    }

    @Test
    void isExceptionThrownWhenFloatToStringAssignment() {
        assertThrows(Exception.class, () -> runInterpreter(
                "<? { string a = 1.23; } ?>"));
    }

    @Test
    void isExceptionThrownWhenIntToStringAssignment() {
        assertThrows(Exception.class, () -> runInterpreter(
                "<? { string a = 1; } ?>"));
    }

    @Test
    void isAppendWorking() throws Exception {
        runInterpreter("<? { int[] a = { 1, 2}; a.append(3,4); par(a[2]); par(a[3]); } ?>");
        assertTrue(getResult().contains("<p>3</p>\n" +
                "<p>4</p>"), getResult());
    }

    @Test
    void isExceptionThrownWhenAppendDifferentTypeTable() {
        assertThrows(Exception.class, () -> runInterpreter("<? { string[] a = { \"1\"}; a.append(3,4); } ?>"));
    }

    @Test
    void isExceptionThrownWhenOutOfTableBound() {
        assertThrows(Exception.class, () -> runInterpreter("<? { int[] a = { 1, 2}; par(a[3]); } ?>"));
    }

    @Test
    void isExceptionThrownWhenArrayInitWithDifferentType() {
        assertThrows(Exception.class, () -> runInterpreter("<? { int[] a = {\"a\", \"b\"}; } ?>"));
    }

    @Test
    void isRecursionWorking() throws Exception {
        runInterpreter("<? func int s(int a) { if(a == 0) { return 1; } " +
                "else { int j = a - 1; int r = s(j); int ret = a * r; return ret; } } " +
                "{ int a = s(4); par(a); } ?>");
        assertTrue(getResult().contains("<p>24</p>"));
    }

    private static InputStream convertStringToInputStreamReader(String string) {
        return new ByteArrayInputStream(string.getBytes());
    }

    private void runInterpreter(String content) throws Exception {
        Interpreter.run(convertStringToInputStreamReader(content), testFileDir);
    }

    private String getResult() throws IOException {
        return new String(Files.readAllBytes(Paths.get(testFileDir)));
    }
}
