package ctml.interpreter.parser;

import ctml.helpers.Logger;
import ctml.interpreter.lexer.Lexer;
import ctml.structures.model.Block;
import ctml.structures.model.Expression;
import ctml.structures.model.Function;
import ctml.structures.model.Variable;
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

    private void nextToken() throws Exception {
        currentToken = lexer.nextToken();
    }

    public void parseProgram() throws Exception {
        while(currentToken.getType() != END) {

            if (!parseCtml) {
                if(currentToken.getType() == TokenType.HTML_CONTENT) { //pass to output file in the future

                } else if (currentToken.getType() == TokenType.CTML_START) {
                    parseCtml = true;
                }
            } else {

                while(currentToken.getType() == TokenType.FUNCTION)
                    parseFunction();

                if(currentToken.getType() == TokenType.BRACKET_OPEN)
                    parseBlock();

                acceptNextToken(TokenType.CTML_END);
                parseCtml = false;
            }

            nextToken();
        }
    }

    private void acceptNextToken(TokenType tokenType) throws Exception {
        nextToken();
        accept(tokenType);
    }

    private boolean accept(TokenType tokenType) throws Exception {
        if(currentToken.getType().equals(tokenType)) {
            return true;
        } else {
            throw Logger.error("Unexpected token: " + currentToken.getType() + " in line: " + currentToken.getLineNumber() +
                    " char: " + currentToken.getCharacterNumber() + "\n Expected: " + tokenType);
        }
    }

    private boolean acceptOneOfMany(List<TokenType> tokenTypeList) throws Exception {
        if (tokenTypeList.contains(currentToken.getType()))
            return true;
        else {
            throw Logger.error("TODO");
        }
    }

    private void parseFunction() throws Exception {
        Function function = new Function();
        function.setReturnType(parseReturnType());
        function.setId(parseID());
        acceptNextToken(TokenType.PARENTHESIS_OPEN);
        function.setParameters(parseParameters());
        function.setBlock(parseBlock());

    }

    private TokenType parseReturnType() throws Exception {
        nextToken();
        acceptOneOfMany(new ArrayList<>(Arrays.asList(TokenType.INTEGER_TYPE, TokenType.STRING_TYPE, TokenType.FLOAT_TYPE, TokenType.CSV_TYPE, TokenType.VOID)));
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

        acceptOneOfMany(new ArrayList<>(Arrays.asList(TokenType.INTEGER_TYPE, TokenType.STRING_TYPE, TokenType.FLOAT_TYPE, TokenType.CSV_TYPE)));
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

    private boolean isOneOfType(List<TokenType> tokenTypeList) {
        return tokenTypeList.contains(currentToken.getType());
    }

    private Block parseBlock() throws Exception {
        acceptNextToken(TokenType.BRACKET_OPEN);

        Block block = new Block();

        nextToken();
        while(currentToken.getType() != TokenType.BRACKET_CLOSE) {
            if(isOneOfType(new ArrayList<>(Arrays.asList(TokenType.INTEGER_TYPE, TokenType.STRING_TYPE, TokenType.FLOAT_TYPE, TokenType.CSV_TYPE)))) {
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

                if(currentToken.getType() == TokenType.EQUALS) {
                    parseVariableInit(variable);
                }

                accept(TokenType.SEMICOLON);
                nextToken();

            } else {
                parseStatement();
            }
        }

        return block;
    }

    private void parseVariableInit(Variable variable) throws Exception {
        nextToken();
        parseExpression();
    }

    private void parseStatement() throws Exception {
        switch(currentToken.getType()) {
            case IF:
                acceptNextToken(PARENTHESIS_OPEN);
                parseExpression();
                accept(PARENTHESIS_CLOSE);
                nextToken();
                ///
            case WHILE:;
            case RETURN:;
            case LINK:;
            case LOAD:;
            case HEADER:;
            case PARAGRAPH:;
            case IMAGE:;
            case LIST:;
            case TABLE:;
            case ID: ;
        }
    }

    private Expression parseExpression() throws Exception {
        nextToken();
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
        while(isOneOfType(new ArrayList<>(Arrays.asList(LESS, LESS_EQUALS, GREATER, GREATER_EQUALS)))) {
            relationExpression.setOperator(currentToken.getType());
            nextToken();
            relationExpression.addOperand(parsePrimaryExpression());
        }

        return relationExpression;
    }

    private Expression parsePrimaryExpression() throws Exception {
        Expression expression = new Expression();
        if (currentToken.getType() == PARENTHESIS_OPEN) {
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
            expression.addOperand(parseExpression());
            acceptNextToken(PARENTHESIS_CLOSE);
        } else if (currentToken.getType() == ID) {
            parseVariable();
        } else {
            parseLiteral();
        }

        return expression;
    }

    private Variable parseVariable() throws Exception { /////sprawdzic czy jesli jest csv to dwa brackety
        accept(ID);
        nextToken();

        if(currentToken.getType() == SQUARE_BRACKET_OPEN) {
            acceptNextToken(NUMBER);
            acceptNextToken(SQUARE_BRACKET_CLOSE);
            nextToken();
            if(currentToken.getType() == SQUARE_BRACKET_OPEN) {
                acceptNextToken(NUMBER);
                acceptNextToken(SQUARE_BRACKET_CLOSE);
            }
        }

        return null; ///!!
    }

    private Variable parseLiteral() throws Exception {
        acceptOneOfMany(new ArrayList<>(Arrays.asList(STRING_CONTENT, NUMBER, FLOAT_NUMBER)));
        nextToken();
        return null;
    }



}
