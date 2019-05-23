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

class ProgramTests {

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
    void testHeader() throws Exception {
        runInterpreter("<? { head(\"a\"); } ?>");
        assertTrue(getResult().contains("<h1>a</h1>"), getResult());
    }

    @Test
    void testParagraph() throws Exception {
        runInterpreter("<? { par(\"a\"); } ?>");
        assertTrue(getResult().contains("<p>a</p>"), getResult());
    }

    @Test
    void testImage() throws Exception {
        runInterpreter("<? { img(\"a\"); } ?>");
        assertTrue(getResult().contains("<img src=\"a\">"), getResult());
    }

    @Test
    void testLink() throws Exception {
        runInterpreter("<? { link(\"a\", \"b\"); } ?>");
        assertTrue(getResult().contains("<a href=a>b</a>"), getResult());
    }

    @Test
    void testTable() throws Exception {
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
    void testList() throws Exception {
        runInterpreter("<? { list { list_item(\"a\"); list_item(\"b\"); } } ?>");
        assertTrue(getResult().contains("<ul>\n" +
                "<li>a</li>\n" +
                "<li>b</li>\n" +
                "</ul>"), getResult());
    }

    @Test
    void testIntAssignment() throws Exception {
        runInterpreter("<? { int a = 1; par(a); } ?>");
        assertTrue(getResult().contains("<p>1</p>"), getResult());
    }

    @Test
    void testStringAssignment() throws Exception {
        runInterpreter("<? { string a = \"abc\"; par(a); } ?>");
        assertTrue(getResult().contains("<p>abc</p>"), getResult());
    }

    @Test
    void areQuotesInStringPassedToHtml() throws Exception {
        runInterpreter("<? { string a = \"\\\"abc\\\"\"; par(a); } ?>");
        assertTrue(getResult().contains("<p>\"abc\"</p>"), getResult());
    }

    @Test
    void testFloatAssignment() throws Exception {
        runInterpreter("<? { float a = 1.23; par(a); } ?>");
        assertTrue(getResult().contains("<p>1.23</p>"));
    }

    @Test
    void testIntTableAssignment() throws Exception {
        runInterpreter("<? { int[] a = {1, 2}; par(a[0]); par(a[1]); } ?>");
        assertTrue(getResult().contains("<p>1</p>\n<p>2</p>"), getResult());
    }

    @Test
    void testStringTableAssignment() throws Exception {
        runInterpreter("<? { string[] a = { \"a\", \"b\" }; par(a[0]); par(a[1]); } ?>");
        assertTrue(getResult().contains("<p>a</p>\n<p>b</p>"), getResult());
    }

    @Test
    void testFloatTableAssignment() throws Exception {
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
    void testAppend() throws Exception {
        runInterpreter("<? { int[] a = { 1, 2}; a.append(3,4); par(a[2]); par(a[3]); } ?>");
        assertTrue(getResult().contains("<p>3</p>\n" +
                "<p>4</p>"), getResult());
    }

    @Test
    void isExceptionThrownWhenAppendDifferentTypeTable() {
        assertThrows(Exception.class, () -> runInterpreter("<? { int[] a = { 1 }; a.append(\"a\",\"b\"); } ?>"));
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
    void testRecursion() throws Exception {
        runInterpreter("<? func int s(int a) { if(a == 0) { return 1; } " +
                "else { int j = a - 1; int r = s(j); int ret = a * r; return ret; } } " +
                "{ int a = s(4); par(a); } ?>");
        assertTrue(getResult().contains("<p>24</p>"), getResult());
    }

    @Test
    void testAddOperation() throws Exception {
        runInterpreter("<? { int a = 1 + 1; par(a); } ?>");
        assertTrue(getResult().contains("<p>2</p>"), getResult());
    }

    @Test
    void testSubtractOperation() throws Exception {
        runInterpreter("<? { int a = 2 - 3; par(a); } ?>");
        assertTrue(getResult().contains("<p>-1</p>"), getResult());
    }

    @Test
    void testMultiplyOperation() throws Exception {
        runInterpreter("<? { int a = 11 * 11; par(a); } ?>");
        assertTrue(getResult().contains("<p>121</p>"), getResult());
    }

    @Test
    void testDivideOperation() throws Exception {
        runInterpreter("<? { int a = 24/6; par(a); } ?>");
        assertTrue(getResult().contains("<p>4</p>"), getResult());
    }

    @Test
    void testBracketsInExpression() throws Exception {
        runInterpreter("<? { int a = (2 + 4)*2; par(a); } ?>");
        assertTrue(getResult().contains("<p>12</p>"), getResult());
    }

    @Test
    void testVEEERYHardExpression() throws Exception {
        runInterpreter("<? { float a = (((5.763/2.5)*2+2.45*33/2)-3)/7.218+2/3; par(a); } ?>");
        assertTrue(getResult().contains("<p>6.4903574</p>"), getResult());
    }

    @Test
    void testIfStatement() throws Exception {
        runInterpreter("<? { if(1) {par(\"a\");} } ?>");
        assertTrue(getResult().contains("<p>a</p>"), getResult());
    }

    @Test
    void testIfElseStatement() throws Exception {
        runInterpreter("<? { if(0) {par(\"a\");} else {par(\"b\");} } ?>");
        assertTrue(getResult().contains("<p>b</p>"), getResult());
    }

    @Test
    void testAndOperatorSuccessful() throws Exception {
        runInterpreter("<? { int a = 1; int b = 1; if(a && b) {par(\"a\");} else {par(\"b\");} } ?>");
        assertTrue(getResult().contains("<p>a</p>"), getResult());
    }

    @Test
    void testAndOperatorUnsuccessful() throws Exception {
        runInterpreter("<? { int a = 1; int b = 0; if(a && b) {par(\"a\");} else {par(\"b\");} } ?>");
        assertTrue(getResult().contains("<p>b</p>"), getResult());
    }

    @Test
    void testOrOperatorSuccessful() throws Exception {
        runInterpreter("<? { int a = 1; int b = 0; if(a || b) {par(\"a\");} else {par(\"b\");} } ?>");
        assertTrue(getResult().contains("<p>a</p>"), getResult());
    }

    @Test
    void testOrOperatorUnsuccessful() throws Exception {
        runInterpreter("<? { int a = 0; int b = 0; if(a || b) {par(\"a\");} else {par(\"b\");} } ?>");
        assertTrue(getResult().contains("<p>b</p>"), getResult());
    }

    @Test
    void testEqualsOperatorSuccessful() throws Exception {
        runInterpreter("<? { int a = 5; int b = 5; if(a == b) {par(\"a\");} else {par(\"b\");} } ?>");
        assertTrue(getResult().contains("<p>a</p>"), getResult());
    }

    @Test
    void testEqualsOperatorUnsuccessful() throws Exception {
        runInterpreter("<? { int a = 5; int b = 6; if(a == b) {par(\"a\");} else {par(\"b\");} } ?>");
        assertTrue(getResult().contains("<p>b</p>"), getResult());
    }

    @Test
    void testNotEqualsOperatorSuccessful() throws Exception {
        runInterpreter("<? { int a = 5; int b = 6; if(a != b) {par(\"a\");} else {par(\"b\");} } ?>");
        assertTrue(getResult().contains("<p>a</p>"), getResult());
    }

    @Test
    void testNotEqualsOperatorUnsuccessful() throws Exception {
        runInterpreter("<? { int a = 5; int b = 5; if(a != b) {par(\"a\");} else {par(\"b\");} } ?>");
        assertTrue(getResult().contains("<p>b</p>"), getResult());
    }

    @Test
    void testGreaterEqualsOperatorSuccessfulWithEqualsNumbers() throws Exception {
        runInterpreter("<? { int a = 5; int b = 5; if(a >= b) {par(\"a\");} else {par(\"b\");} } ?>");
        assertTrue(getResult().contains("<p>a</p>"), getResult());
    }

    @Test
    void testGreaterEqualsOperatorSuccessfulWithGreaterNumber() throws Exception {
        runInterpreter("<? { int a = 6; int b = 5; if(a >= b) {par(\"a\");} else {par(\"b\");} } ?>");
        assertTrue(getResult().contains("<p>a</p>"), getResult());
    }

    @Test
    void testGreaterEqualsOperatorUnsuccessful() throws Exception {
        runInterpreter("<? { int a = 5; int b = 6; if(a >= b) {par(\"a\");} else {par(\"b\");} } ?>");
        assertTrue(getResult().contains("<p>b</p>"), getResult());
    }

    @Test
    void testLessEqualsOperatorSuccessfulWithEqualsNumbers() throws Exception {
        runInterpreter("<? { int a = 5; int b = 5; if(a <= b) {par(\"a\");} else {par(\"b\");} } ?>");
        assertTrue(getResult().contains("<p>a</p>"), getResult());
    }

    @Test
    void testLessEqualsOperatorSuccessfulWithSmallerNumber() throws Exception {
        runInterpreter("<? { int a = 4; int b = 5; if(a <= b) {par(\"a\");} else {par(\"b\");} } ?>");
        assertTrue(getResult().contains("<p>a</p>"), getResult());
    }

    @Test
    void testLessEqualsOperatorUnsuccessful() throws Exception {
        runInterpreter("<? { int a = 7; int b = 6; if(a <= b) {par(\"a\");} else {par(\"b\");} } ?>");
        assertTrue(getResult().contains("<p>b</p>"), getResult());
    }

    @Test
    void testLessOperatorSuccessful() throws Exception {
        runInterpreter("<? { int a = 4; int b = 5; if(a < b) {par(\"a\");} else {par(\"b\");} } ?>");
        assertTrue(getResult().contains("<p>a</p>"), getResult());
    }

    @Test
    void testLessOperatorUnsuccessful() throws Exception {
        runInterpreter("<? { int a = 7; int b = 6; if(a < b) {par(\"a\");} else {par(\"b\");} } ?>");
        assertTrue(getResult().contains("<p>b</p>"), getResult());
    }

    @Test
    void testGreaterOperatorSuccessful() throws Exception {
        runInterpreter("<? { int a = 6; int b = 5; if(a > b) {par(\"a\");} else {par(\"b\");} } ?>");
        assertTrue(getResult().contains("<p>a</p>"), getResult());
    }

    @Test
    void testGreaterOperatorUnsuccessful() throws Exception {
        runInterpreter("<? { int a = 3; int b = 6; if(a > b) {par(\"a\");} else {par(\"b\");} } ?>");
        assertTrue(getResult().contains("<p>b</p>"), getResult());
    }

    @Test
    void testBracketsInLogicalExpression() throws Exception {
        runInterpreter("<? { if( (0||1) && 0) {par(\"a\");} else {par(\"b\");} } ?>");
        assertTrue(getResult().contains("<p>b</p>"), getResult());
    }

    @Test
    void testFunctionInvocation() throws Exception {
        runInterpreter("<? func void f() { par(1); } { f(); } ?>");
        assertTrue(getResult().contains("<p>1</p>"), getResult());
    }

    @Test
    void areArgumentsInFunctionInvocationArePassed() throws Exception {
        runInterpreter("<? func void f(int a, string b, float[] c) { par(a); par(b); par(c[0]); } { int a = 1;" +
                " string b = \"2\"; float[] c = {3}; f(a,b,c); } ?>");
        assertTrue(getResult().contains("<p>1</p>\n<p>2</p>\n<p>3</p>"), getResult());
    }

    @Test
    void isExceptionThrownWhenPassingArgumentOfWrongTypeToFunction() {
        assertThrows(Exception.class, () -> runInterpreter("<? func void f(int a) { } { f(\"abc\"); } ?>"));
    }

    @Test
    void testFunctionReturn() throws Exception {
        runInterpreter("<? func int f() { return 1; } { int a = f(); par(a); } ?>");
        assertTrue(getResult().contains("<p>1</p>"), getResult());
    }

    @Test
    void isExceptionThrownWhenReturningValueOfWrongType() {
        assertThrows(Exception.class, () -> runInterpreter("<? func int f() { return \"a\"; } { f(); } ?>"));
    }

    @Test
    void testWhileStatement() throws Exception {
        runInterpreter("<? { int a = 3; while(a != 0) {par(a); a=a-1;} } ?>");
        assertTrue(getResult().contains("<p>3</p>\n<p>2</p>\n<p>1</p>"), getResult());
    }

    @Test
    void doesInnerIfSeesVariablesInMainBody() throws Exception {
        runInterpreter("<? { int a = 1; if(1) { if(1) {par(\"a\");} } } ?>");
        assertTrue(getResult().contains("<p>a</p>"), getResult());
    }

    @Test
    void isExceptionThrownWhenAssigningDifferentTypeToCsv() {
        assertThrows(Exception.class, () -> runInterpreter("<? { csv a = 1; } ?>"));
    }

    @Test
    void isExceptionThrownWhenAssigningDifferentTypeToTable() {
        assertThrows(Exception.class, () -> runInterpreter("<? { int[] a = 1; } ?>"));
    }

    @Test
    void testLoadToCsv() throws Exception {
        runInterpreter("<? { csv a = load(\"testCSV.csv\"); par(a[0][0]); par(a[1][0]); } ?>");
        assertTrue(getResult().contains("<p>test00</p>\n<p>test10</p>"), getResult());
    }

    @Test
    void areSeparateCtmlBlocksSeesFunctions() throws Exception {
        runInterpreter(
                "<? func void i(int a) { int b = a + 1; par(b); } ?>\n" +
                "<div>div1</div>\n" +
                "<? { i(1); } ?>\n" +
                "<div>div2</div>\n" +
                "<? { i(2); } ?>");
        assertTrue(getResult().contains("<div>div1</div>\n<p>2</p>\n<div>div2</div>\n<p>3</p>"), getResult());
    }

    @Test
    void testIndexAsVariableInCsvTable() throws Exception {
        runInterpreter("<? { csv d = load(\"testCSV.csv\");\n" +
                "            int a = 1;\n" +
                "            int b = 1;\n" +
                "            string e = d[a][b];\n" +
                "            par(e); } ?>");
        assertTrue(getResult().contains("<p>test11</p>"), getResult());
    }

    @Test
    void testIndexAsVariableInOneDimTable() throws Exception {
        runInterpreter("<? { int[] a = {1, 2, 3};\n" +
                "            int b = 1;\n" +
                "            int c = a[b];\n" +
                "            par(c); } ?>");
        assertTrue(getResult().contains("<p>2</p>"), getResult());
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
