package ctml.interpreter.parser;

import ctml.helpers.Logger;
import ctml.interpreter.lexer.Lexer;
import ctml.structures.model.*;
import ctml.structures.token.Token;
import ctml.structures.token.TokenType;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ctml.structures.token.TokenType.*;

public class Parser {

    private Lexer lexer;
    private Token currentToken;
    private Program program;
    private boolean parseCtml = false;

    public Parser(InputStream inputStream) throws Exception {
        this.lexer = new Lexer(inputStream);
        currentToken = lexer.nextToken();
        program = new Program();
    }

    public void nextToken() throws Exception {
        currentToken = lexer.nextToken();
    }

    public Program parseProgram() throws Exception {
        while(currentToken.getType() != END) {

            if (!parseCtml) {
                if(currentToken.getType() == TokenType.HTML_CONTENT) { //pass to output file in the future

                } else if (currentToken.getType() == TokenType.CTML_START) {
                    parseCtml = true;
                }
            } else {
                acceptOneOfMany(FUNCTION, BRACKET_OPEN);

                while(currentToken.getType() == TokenType.FUNCTION) {
                    program.addFunction(parseFunction());
                    nextToken();
                }

                if(currentToken.getType() == TokenType.BRACKET_OPEN) {
                    program.addBlock(parseBlock());
                    accept(BRACKET_CLOSE);
                    nextToken();
                }

                accept(TokenType.CTML_END);
                parseCtml = false;
            }

            nextToken();
        }

        return program;
    }

    private void acceptNextToken(TokenType tokenType) throws Exception {
        nextToken();
        accept(tokenType);
    }

    private void accept(TokenType tokenType) throws Exception {
        if(!currentToken.getType().equals(tokenType)) {
            throw Logger.error("Unexpected token: " + currentToken.getType() + " in line: " + currentToken.getLineNumber() +
                    " char: " + currentToken.getCharacterNumber() + "\n Expected: " + tokenType);
        }
    }

    private void acceptOneOfMany(TokenType ... tokenTypes) throws Exception {

        if(!checkIfIsOneOfTokenTypes(tokenTypes)) {
            throw Logger.error("Error at line " + currentToken.getLineNumber() + " char " + currentToken.getCharacterNumber() +
                    " - expected one of TokenTypes: " + Arrays.toString(tokenTypes) + " but was: " + currentToken.getType());
        }
    }

    private boolean checkIfIsOneOfTokenTypes(TokenType... tokenTypes) {
        for (TokenType tokenType : tokenTypes) {
            if(currentToken.getType() == tokenType) {
                return true;
            }
        }
        return false;
    }

    private Function parseFunction() throws Exception {
        Function function = new Function();
        function.setReturnType(parseReturnType());
        function.setId(parseID());
        acceptNextToken(TokenType.PARENTHESIS_OPEN);
        function.setParameters(parseParameters());
        accept(PARENTHESIS_CLOSE);
        nextToken();
        function.setBlock(parseBlock());
        return function;
    }

    private TokenType parseReturnType() throws Exception {
        nextToken();
        acceptOneOfMany(INTEGER_TYPE, STRING_TYPE, FLOAT_TYPE, CSV_TYPE, VOID);
        return currentToken.getType();
    }

    private String parseID() throws Exception {
        acceptNextToken(TokenType.ID);
        return currentToken.getContent();
    }

    private List<Variable> parseParameters() throws Exception {
        nextToken();
        List<Variable> variableList = new ArrayList<>();
        while (currentToken.getType() != TokenType.PARENTHESIS_CLOSE) {

            if(!variableList.isEmpty()) {
                accept(TokenType.COMMA);
                nextToken();
            }
            variableList.add(parseVariableDeclaration());
            nextToken();
        }

        return variableList;
    }

    private Variable parseVariableDeclaration() throws Exception {

        acceptOneOfMany(INTEGER_TYPE, STRING_TYPE, FLOAT_TYPE, CSV_TYPE);
        Variable variable = new Variable();
        variable.setType(currentToken.getType());

        if(currentToken.getType() == TokenType.CSV_TYPE)
            variable.setIsCsv(true);

        nextToken();
        if(currentToken.getType() == TokenType.SQUARE_BRACKET_OPEN) {
            acceptNextToken(TokenType.SQUARE_BRACKET_CLOSE);
            variable.setId(parseID());
            variable.setIsTable(true);
        } else {
            accept(TokenType.ID);
            variable.setId(currentToken.getContent());
        }

        return variable;

    }

    private Block parseBlock() throws Exception {
        accept(TokenType.BRACKET_OPEN);

        Block block = new Block();
        nextToken();
        while(currentToken.getType() != TokenType.BRACKET_CLOSE) {
            if(checkIfIsOneOfTokenTypes(INTEGER_TYPE, STRING_TYPE, FLOAT_TYPE, CSV_TYPE)) {
                int line = currentToken.getLineNumber();
                int character = currentToken.getCharacterNumber();

                Variable variable = parseVariableDeclaration();

                try {
                    block.addVariable(variable);
                } catch (Exception e) {
                    throw Logger.error("Error at line " + line + " char: " + character + ".\n" +
                            "Variable with id: " + variable.getId() + " has already been defined!");
                }

                nextToken();

                if(currentToken.getType() == ASSIGN)
                    block.addInstruction(parseVariableInit(variable));

                accept(TokenType.SEMICOLON);
                nextToken();
            } else {
                block.addInstruction(parseStatement());
            }
        }

        return block;
    }

    private Assignment parseVariableInit(Variable variable) throws Exception {
        nextToken();
        Assignment assignment = new Assignment();
        assignment.setVariable(variable);

        if (currentToken.getType() == LOAD) {
            if (variable.getType() != CSV_TYPE) {
                throw Logger.error("Error at line... Cannot use load function with other variable than csv type");
            }
            assignment.setExecutable(parseLoad());
        } else if(currentToken.getType() == BRACKET_OPEN){
            assignment.setExecutable(arrayInitialization());
            nextToken();
        } else if(currentToken.getType()== STRING_FORMATTER) {
            assignment.setExecutable(parseStringFormatter());
            nextToken();
        } else {
            assignment.setExecutable(parseSimpleExpression());
        }

        accept(SEMICOLON);

        return assignment;
    }

    private StringFormatter parseStringFormatter() throws Exception {
        StringFormatter stringFormatter = new StringFormatter();
        acceptNextToken(PARENTHESIS_OPEN);
        stringFormatter.setVariableList(parseArgumentList(PARENTHESIS_CLOSE));
        accept(PARENTHESIS_CLOSE);

        return stringFormatter;
    }

    private ArrayInit arrayInitialization() throws Exception {
        ArrayInit arrayInit = new ArrayInit();
        arrayInit.setVariableList(parseArgumentList(BRACKET_CLOSE));
        return arrayInit;
    }

    private List<Variable> parseArgumentList(TokenType end) throws Exception {
        List<Variable> variableList = new ArrayList<>();

        nextToken();
        while(currentToken.getType() != end) {
            if( variableList.size() >0) {
                accept(COMMA);
                nextToken();
            }
            variableList.add(parseArgument());
        }
        return variableList;
    }

    private Variable parseArgument() throws Exception {
        acceptOneOfMany(NUMBER, FLOAT_NUMBER, STRING_CONTENT, ID);
        Variable variable = new Variable();
        if(currentToken.getType() == ID) {
            variable.setId(currentToken.getContent());
            nextToken();
            if(currentToken.getType() == SQUARE_BRACKET_OPEN) {
                parseIndex(variable);
            }
        } else {
            variable.setValue(currentToken.getContent());
            nextToken();
        }

        return variable;
    }

    private void parseIndex(Variable variable) throws Exception {
        nextToken();
        Variable index1 = new Variable();
        acceptOneOfMany(NUMBER, ID);
        index1.setValue(currentToken.getContent());
        variable.setIndex1(index1);
        acceptNextToken(SQUARE_BRACKET_CLOSE);
        nextToken();
        if(currentToken.getType() == SQUARE_BRACKET_OPEN) {
            Variable index2 = new Variable();
            nextToken();
            acceptOneOfMany(NUMBER, ID);
            index2.setValue(currentToken.getContent());
            variable.setIndex2(index2);
            acceptNextToken(SQUARE_BRACKET_CLOSE);
            nextToken();
        }
    }

    private Executable parseStatement() throws Exception {
        switch(currentToken.getType()) {
            case IF:
                return parseIf();
            case WHILE:
                return parseWhile();
            case RETURN:
                return parseReturn();
            case LINK:
                return parseLink();
            case HEADER:
                return parseHeader();
            case PARAGRAPH:
                return parseParagraph();
            case IMAGE:
                return parseImage();
            case LIST:
                return parseList();
            case TABLE:
                return parseTable();
            case ID:
                Executable executable = parseMethodCallOrAssignment();
                nextToken();
                return executable;
        }

        return null;
    }

    private If parseIf() throws Exception {
        If ifStatement = new If();
        acceptNextToken(PARENTHESIS_OPEN);
        nextToken();
        ifStatement.setExpression(parseExpression());
        accept(PARENTHESIS_CLOSE);
        nextToken();
        ifStatement.setBlock(parseBlock());
        nextToken();
        if(currentToken.getType() == ELSE) {
            nextToken();
            ifStatement.setElseBlock(parseBlock());
            nextToken();
        }
        return ifStatement;
    }

    private While parseWhile() throws Exception {
        While whileStatement = new While();
        acceptNextToken(PARENTHESIS_OPEN);
        nextToken();
        whileStatement.setExpression(parseExpression());
        accept(PARENTHESIS_CLOSE);
        nextToken();
        whileStatement.setBlock(parseBlock());
        nextToken();
        return whileStatement;
    }

    private Return parseReturn() throws Exception {
        Return returnStatement = new Return();
        nextToken();
        if(currentToken.getType() != SEMICOLON)
            returnStatement.setExpression(parseExpression());
        accept(SEMICOLON);
        nextToken();
        return returnStatement;
    }

    private Link parseLink() throws Exception {
        Link link = new Link();
        acceptNextToken(PARENTHESIS_OPEN);
        nextToken();
        link.setLink(parseArgument());
        accept(COMMA);
        nextToken();
        link.setText(parseArgument());
        accept(PARENTHESIS_CLOSE);
        acceptNextToken(SEMICOLON);
        nextToken();
        return link;
    }

    private Load parseLoad() throws Exception {
        Load load = new Load();
        accept(LOAD);
        acceptNextToken(PARENTHESIS_OPEN);
        nextToken();
        load.setVariable(parseArgument());
        accept(PARENTHESIS_CLOSE);
        acceptNextToken(SEMICOLON);
        return load;
    }

    private Header parseHeader() throws Exception {
        Header header = new Header();
        accept(HEADER);
        acceptNextToken(PARENTHESIS_OPEN);
        nextToken();
        header.setText(parseArgument());
        accept(PARENTHESIS_CLOSE);
        acceptNextToken(SEMICOLON);
        nextToken();
        return header;
    }

    private Paragraph parseParagraph() throws Exception {
        Paragraph paragraph = new Paragraph();
        accept(PARAGRAPH);
        acceptNextToken(PARENTHESIS_OPEN);
        nextToken();
        paragraph.setText(parseArgument());
        accept(PARENTHESIS_CLOSE);
        acceptNextToken(SEMICOLON);
        nextToken();
        return paragraph;
    }

    private ImageCtml parseImage() throws Exception {
        ImageCtml imageCtml = new ImageCtml();
        accept(IMAGE);
        acceptNextToken(PARENTHESIS_OPEN);
        nextToken();
        imageCtml.setLink(parseArgument());
        accept(PARENTHESIS_CLOSE);
        acceptNextToken(SEMICOLON);
        nextToken();
        return imageCtml;
    }

    private ListCtml parseList() throws Exception {
        acceptNextToken(BRACKET_OPEN);
        ListCtml listCtml = new ListCtml();
        nextToken();
        while (currentToken.getType() == LIST_ITEM) {
            acceptNextToken(PARENTHESIS_OPEN);
            nextToken();
            listCtml.addVariable(parseArgument());
            accept(PARENTHESIS_CLOSE);
            acceptNextToken(SEMICOLON);
            nextToken();
        }

        accept(BRACKET_CLOSE);
        nextToken();
        return listCtml;
    }

    private Table parseTable() throws Exception {
        Table table = new Table();
        acceptNextToken(BRACKET_OPEN);
        nextToken();
        while (currentToken.getType() == ROW) {
            table.addRow(parseRow());
        }
        accept(BRACKET_CLOSE);
        nextToken();
        return table;
    }

    private Row parseRow() throws Exception {
        Row row = new Row();
        acceptNextToken(BRACKET_OPEN);
        nextToken();
        while(checkIfIsOneOfTokenTypes(COLUMN, TABLE_ITEM)) {
            acceptOneOfMany(COLUMN, TABLE_ITEM);
            if (currentToken.getType() == COLUMN) {
                Column column = new Column();
                acceptNextToken(PARENTHESIS_OPEN);
                nextToken();
                column.setVariable(parseArgument());
                accept(PARENTHESIS_CLOSE);
                row.addTableItem(column);
            } else if (currentToken.getType() == TABLE_ITEM) {
                TableItem tableItem = new TableItem();
                acceptNextToken(PARENTHESIS_OPEN);
                nextToken();
                tableItem.setVariable(parseArgument());
                accept(PARENTHESIS_CLOSE);
                row.addTableItem(tableItem);
            }
            acceptNextToken(SEMICOLON);
            nextToken();
        }
        accept(BRACKET_CLOSE);
        nextToken();
        return row;
    }

    private Executable parseMethodCallOrAssignment() throws Exception {
        Variable variable = parseVariableOrMethodCall();
        if (variable.getType() != FUNCTION) {
            acceptOneOfMany(PERIOD, ASSIGN);
            if(currentToken.getType() == PERIOD) {
                Append append = new Append();
                append.setVariable(variable);
                acceptNextToken(ARRAY_APPEND);
                acceptNextToken(PARENTHESIS_OPEN);
                append.setArguments(parseArgumentList(PARENTHESIS_CLOSE));
                accept(PARENTHESIS_CLOSE);
                acceptNextToken(SEMICOLON);
                return append;
            } else if(currentToken.getType() == ASSIGN) {
                return parseVariableInit(variable);
            }
        }

        FunctionCall functionCall = new FunctionCall();
        functionCall.setId(variable.getId());
        functionCall.setArguments(variable.getFunctionArguments());
        acceptNextToken(SEMICOLON);
        return functionCall;

    }

    private Expression parseExpression() throws Exception {
        Expression expression = new Expression();
        expression.addOperand(parseAndExpression());
        while (currentToken.getType() == OR) {
            nextToken();
            expression.setOperator(OR);
            expression.addOperand(parseAndExpression());
        }

        return expression;
    }

    private Expression parseAndExpression() throws Exception {
        Expression andExpression = new Expression();
        andExpression.addOperand(parseEqualityExpression());
        while(currentToken.getType() == AND) {
            nextToken();
            andExpression.setOperator(AND);
            andExpression.addOperand(parseEqualityExpression());
        }

        return andExpression;
    }

    private Expression parseEqualityExpression() throws Exception {
        Expression equalityExpression = new Expression();
        equalityExpression.addOperand(parseRelation());
        while(currentToken.getType() == EQUALS || currentToken.getType() == NOT_EQUALS) {
            equalityExpression.setOperator(currentToken.getType());
            nextToken();
            equalityExpression.addOperand(parseRelation());
        }

        return equalityExpression;
    }

    private Expression parseRelation() throws Exception {
        Expression relationExpression = new Expression();
        relationExpression.addOperand(parsePrimaryExpression());
        while(checkIfIsOneOfTokenTypes(LESS, LESS_EQUALS, GREATER, GREATER_EQUALS)) {
            relationExpression.setOperator(currentToken.getType());
            nextToken();
            relationExpression.addOperand(parsePrimaryExpression());
        }

        return relationExpression;
    }

    private Expression parsePrimaryExpression() throws Exception {
        Expression expression = new Expression();
        if (currentToken.getType() == PARENTHESIS_OPEN) {
            nextToken();
            expression.addOperand(parseExpression());
            acceptNextToken(PARENTHESIS_CLOSE);
        } else {
            expression.addOperand(parseSimpleExpression());
        }

        return expression;
    }

    private Expression parseSimpleExpression() throws Exception {
        Expression expression = new Expression();
        expression.addOperand(parseMultiplication());
        while(currentToken.getType() == ADD || currentToken.getType() == SUBTRACT) {
            expression.setOperator(currentToken.getType());
            nextToken();
            expression.addOperand(parseMultiplication());
        }

        return expression;
    }

    private Expression parseMultiplication() throws Exception {
        Expression expression = new Expression();
        expression.addOperand(parseFactor());
        while(currentToken.getType() == MULTIPLY || currentToken.getType() == DIVIDE) {
            expression.setOperator(currentToken.getType());
            nextToken();
            expression.addOperand(parseFactor());
        }

        return expression;
    }

    private Expression parseFactor() throws Exception {
        Expression expression = new Expression();

        if (currentToken.getType() == PARENTHESIS_OPEN) {
            nextToken();
            expression.addOperand(parseExpression());
            acceptNextToken(PARENTHESIS_CLOSE);
        } else if (currentToken.getType() == ID) {
            expression.addVariable(parseVariableOrMethodCall());
        } else {
            expression.addVariable(parseLiteral());
        }

        return expression;
    }

    private Variable parseVariableOrMethodCall() throws Exception { /////sprawdzic czy jesli jest csv to dwa brackety
        accept(ID);
        String id = currentToken.getContent();
        Variable variable = new Variable();
        variable.setId(id);
        variable.setType(currentToken.getType());
        nextToken();
        if(currentToken.getType() == SQUARE_BRACKET_OPEN) {
            parseIndex(variable);
        } else if (currentToken.getType() == PARENTHESIS_OPEN) {
//            if(!program.checkIfFunctionExsists(id)) { //not working when recursion - needs to be changed
//                throw new Exception("Error at line " + currentToken.getLineNumber() + " character " +currentToken.getCharacterNumber() +
//                        ".\nThe function with id - " + id + " is not defined");
//            }
            variable.setType(FUNCTION);
            variable.setFunctionArguments(parseArgumentList(PARENTHESIS_CLOSE));
            accept(PARENTHESIS_CLOSE);
        }

        return variable;
    }

    private Variable parseLiteral() throws Exception {
        Variable variable = new Variable();
        acceptOneOfMany(STRING_CONTENT, NUMBER, FLOAT_NUMBER);
        variable.setType(currentToken.getType());
        variable.setValue(currentToken.getContent());
        nextToken();
        return variable;
    }
}
