package ctml.interpreter.parser;

import ctml.interpreter.lexer.Lexer;
import ctml.interpreter.program.Program;
import ctml.structures.model.*;
import ctml.structures.model.ctmlObjects.*;
import ctml.structures.model.variables.*;
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
                if(currentToken.getType() == TokenType.HTML_CONTENT) {
                    program.addBlock(parseHtmlBlock());
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

    private HtmlBlock parseHtmlBlock() throws Exception {
        HtmlBlock htmlBlock = new HtmlBlock();
        StringBuilder stringBuilder = new StringBuilder();

        while(currentToken.getType() != CTML_START && currentToken.getType() != END) {
            stringBuilder.append(currentToken.getContent());
            nextToken();
        }

        htmlBlock.setHtmlContent(stringBuilder.toString());

        if(currentToken.getType() == CTML_START)
            parseCtml = true;

        return htmlBlock;
    }

    private void acceptNextToken(TokenType tokenType) throws Exception {
        nextToken();
        accept(tokenType);
    }

    private void accept(TokenType tokenType) throws Exception {
        if(!currentToken.getType().equals(tokenType)) {
            throw new Exception("Unexpected token: " + currentToken.getType() + " in line: " + currentToken.getLineNumber() +
                    " char: " + currentToken.getCharacterNumber() + "\n Expected: " + tokenType);
        }
    }

    private void acceptOneOfMany(TokenType ... tokenTypes) throws Exception {

        if(!checkIfIsOneOfTokenTypes(tokenTypes)) {
            throw new Exception("Error at line " + currentToken.getLineNumber() + " char " + currentToken.getCharacterNumber() +
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
        function.setCtmlBlock(parseBlock());
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
        Variable variable = createNewVariable();
        variable.setType(currentToken.getType());

        nextToken();
        if(currentToken.getType() == TokenType.SQUARE_BRACKET_OPEN) {
            acceptNextToken(TokenType.SQUARE_BRACKET_CLOSE);
            variable.setId(parseID());
            variable.setTable(true);
        } else {
            accept(TokenType.ID);
            variable.setId(currentToken.getContent());
        }

        return variable;

    }

    private Variable createNewVariable(){
        switch(currentToken.getType()) {
            case INTEGER_TYPE:
                return new CtmlInt();
            case FLOAT_TYPE:
                return new CtmlFloat();
            case STRING_TYPE:
                return new CtmlString();
            case CSV_TYPE:
                return new CtmlCsv();
        }

        return null;
    }

    private CtmlBlock parseBlock() throws Exception {
        accept(TokenType.BRACKET_OPEN);

        CtmlBlock ctmlBlock = new CtmlBlock();
        nextToken();
        while(currentToken.getType() != TokenType.BRACKET_CLOSE) {
            if(checkIfIsOneOfTokenTypes(INTEGER_TYPE, STRING_TYPE, FLOAT_TYPE, CSV_TYPE)) {
                int line = currentToken.getLineNumber();
                int character = currentToken.getCharacterNumber();

                Variable variable = parseVariableDeclaration();

                try {
                    ctmlBlock.addVariable(variable);
                } catch (Exception e) {
                    throw new Exception("Error at line " + line + " char: " + character + ".\n" +
                            "Variable with id: " + variable.getId() + " has already been defined!");
                }

                nextToken();

                if(currentToken.getType() == ASSIGN)
                    ctmlBlock.addInstruction(parseVariableInit(variable));

                accept(TokenType.SEMICOLON);
                nextToken();
            } else {
                ctmlBlock.addInstruction(parseStatement());
            }
        }

        return ctmlBlock;
    }

    private Assignment parseVariableInit(Variable variable) throws Exception {
        nextToken();
        Assignment assignment = new Assignment();
        assignment.setVariable(variable);

        if (currentToken.getType() == LOAD) {
            assignment.setReturnExecutable(parseLoad());
        } else if(currentToken.getType() == BRACKET_OPEN){
            assignment.setReturnExecutable(arrayInitialization(variable.getId()));
            nextToken();
        } else {
            assignment.setReturnExecutable(parseSimpleExpression());
        }

        accept(SEMICOLON);

        return assignment;
    }

    private ArrayInit arrayInitialization(String id) throws Exception {
        ArrayInit arrayInit = new ArrayInit(id);
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
        Variable variable = newLiteralOrId();
        variable.setType(currentToken.getType());
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

    private Variable newLiteralOrId() {
        switch(currentToken.getType()) {
            case NUMBER:
                return new CtmlInt();
            case FLOAT_NUMBER:
                return new CtmlFloat();
            case STRING_CONTENT:
                return new CtmlString();
            case ID:
                return new CtmlArguments();
        }
        return null;
    }

    private void parseIndex(Variable variable) throws Exception {
        nextToken();
        CtmlInt index1 = new CtmlInt();
        acceptOneOfMany(NUMBER, ID);
        setIndexValue(index1);
        variable.setIndex1(index1);
        acceptNextToken(SQUARE_BRACKET_CLOSE);
        nextToken();
        if(currentToken.getType() == SQUARE_BRACKET_OPEN) {
            CtmlInt index2 = new CtmlInt();
            nextToken();
            acceptOneOfMany(NUMBER, ID);
            setIndexValue(index2);
            variable.setIndex2(index2);
            acceptNextToken(SQUARE_BRACKET_CLOSE);
            nextToken();
        }
    }

    private void setIndexValue(Variable index) throws Exception {
        if(currentToken.getType() == ID) {
            index.setId(currentToken.getContent());
        } else
            index.setValue(currentToken.getContent());
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
        ifStatement.setCtmlBlock(parseBlock());
        nextToken();
        if(currentToken.getType() == ELSE) {
            nextToken();
            ifStatement.setElseCtmlBlock(parseBlock());
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
        whileStatement.setCtmlBlock(parseBlock());
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
                column.setText(parseArgument());
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
        functionCall.setArguments(variable.getTableValues());
        accept(SEMICOLON);
        return functionCall;

    }

    private Expression parseExpression() throws Exception {
        Expression expression = new Expression();
        expression.addOperand(parseAndExpression());
        while (currentToken.getType() == OR) {
            nextToken();
            expression.addOperator(OR);
            expression.addOperand(parseAndExpression());
        }

        return expression;
    }

    private Expression parseAndExpression() throws Exception {
        Expression andExpression = new Expression();
        andExpression.addOperand(parseEqualityExpression());
        while(currentToken.getType() == AND) {
            nextToken();
            andExpression.addOperator(AND);
            andExpression.addOperand(parseEqualityExpression());
        }

        return andExpression;
    }

    private Expression parseEqualityExpression() throws Exception {
        Expression equalityExpression = new Expression();
        equalityExpression.addOperand(parseRelation());
        while(currentToken.getType() == EQUALS || currentToken.getType() == NOT_EQUALS) {
            equalityExpression.addOperator(currentToken.getType());
            nextToken();
            equalityExpression.addOperand(parseRelation());
        }

        return equalityExpression;
    }

    private Expression parseRelation() throws Exception {
        Expression relationExpression = new Expression();
        relationExpression.addOperand(parsePrimaryExpression());
        while(checkIfIsOneOfTokenTypes(LESS, LESS_EQUALS, GREATER, GREATER_EQUALS)) {
            relationExpression.addOperator(currentToken.getType());
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
            accept(PARENTHESIS_CLOSE); //acceptNExtToken
            nextToken();;
        } else {
            expression.addOperand(parseSimpleExpression());
        }

        return expression;
    }

    private Expression parseSimpleExpression() throws Exception {
        Expression expression = new Expression();
        expression.addOperand(parseMultiplication());
        while(currentToken.getType() == ADD || currentToken.getType() == SUBTRACT) {
            expression.addOperator(currentToken.getType());
            nextToken();
            expression.addOperand(parseMultiplication());
        }

        return expression;
    }

    private Expression parseMultiplication() throws Exception {
        Expression expression = new Expression();
        expression.addOperand(parseFactor());
        while(currentToken.getType() == MULTIPLY || currentToken.getType() == DIVIDE) {
            expression.addOperator(currentToken.getType());
            nextToken();
            expression.addOperand(parseFactor());
        }

        return expression;
    }

    private Expression parseFactor() throws Exception {
        Expression expression = new Expression();

        if (currentToken.getType() == PARENTHESIS_OPEN) {
            nextToken();
            expression.addOperand(parseSimpleExpression()); //parseExpression
            accept(PARENTHESIS_CLOSE);
            nextToken();
        } else if (currentToken.getType() == ID) {
            expression.addVariable(parseVariableOrMethodCall());
        } else {
            expression.addVariable(parseLiteral());
        }

        return expression;
    }

    private Variable parseVariableOrMethodCall() throws Exception {
        accept(ID);
        String id = currentToken.getContent();
        Variable variable = newLiteralOrId();
        variable.setId(id);
        variable.setType(currentToken.getType());
        nextToken();
        if(currentToken.getType() == SQUARE_BRACKET_OPEN) {
            parseIndex(variable);
        } else if (currentToken.getType() == PARENTHESIS_OPEN) {
            variable.setType(FUNCTION);
            variable.setTableValues(parseArgumentList(PARENTHESIS_CLOSE));
            accept(PARENTHESIS_CLOSE);
            nextToken();
        }

        return variable;
    }

    private Variable parseLiteral() throws Exception {
        acceptOneOfMany(STRING_CONTENT, NUMBER, FLOAT_NUMBER);
        Variable variable = newLiteralOrId();
        variable.setType(currentToken.getType());
        variable.setValue(currentToken.getContent());
        nextToken();
        return variable;
    }
}
