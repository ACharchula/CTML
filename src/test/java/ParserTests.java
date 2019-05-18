import ctml.interpreter.parser.Parser;
import ctml.interpreter.parser.Program;
import ctml.structures.model.Executable;
import ctml.structures.model.Function;
import ctml.structures.model.If;
import ctml.structures.model.Variable;
import ctml.structures.token.TokenType;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTests {

    @Test
    void isFunctionParsed() throws Exception {
        String content = "func void function(int a, string[] b) {}";
        Program program = parse(content);

        assertEquals(1, program.getFunctionList().size());
        assertEquals(Function.class, program.getFunctionList().get(0).getClass());
        assertEquals("function", program.getFunctionList().get(0).getId());
        assertEquals(TokenType.VOID, program.getFunctionList().get(0).getReturnType());
        assertEquals(2, program.getFunctionList().get(0).getParameters().size());
    }

    @Test
    void isFunctionParametersParsed() throws Exception {
        String content = "func void function(int a, string[] b, csv c, float[] d) {}";
        Program program = parse(content);

        List<Variable> params = program.getFunctionList().get(0).getParameters();

        assertEquals(4, params.size());

        assertEquals(TokenType.INTEGER_TYPE, params.get(0).getType());
        assertEquals("a", params.get(0).getId());
        assertFalse(params.get(0).isTable());

        assertEquals(TokenType.STRING_TYPE, params.get(1).getType());
        assertEquals("b", params.get(1).getId());
        assertTrue(params.get(1).isTable());

        assertEquals(TokenType.CSV_TYPE, params.get(2).getType());
        assertEquals("c", params.get(2).getId());
        assertTrue(params.get(2).isTable());
        assertTrue(params.get(2).isCsv());

        assertEquals(TokenType.FLOAT_TYPE, params.get(3).getType());
        assertEquals("d", params.get(3).getId());
        assertTrue(params.get(3).isTable());
    }

    @Test
    void isBlockParsed() throws Exception {
        String content = "{ int a; } ";
        Program program = parse(content);

        assertEquals(1, program.getBlockList().size());
    }

    @Test
    void isIfParsed() throws Exception {
        String content = "{ if (a == b) { a = 10; } }";
        Program program = parse(content);

        Executable instruction = program.getBlockList().get(0).getInstructions().get(0);
        assertEquals(If.class, instruction.getClass());
    }

    private Program parse(String content) throws Exception {
        String ctmlContent = "<div> <? " + content + " ?> </div>";
        Parser parser = new Parser(new ByteArrayInputStream(ctmlContent.getBytes()));
        parser.nextToken();
        return parser.parseProgram();
    }
}
