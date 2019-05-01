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

                acceptOneOfMany(FUNCTION, BRACKET_OPEN);

                while(currentToken.getType() == TokenType.FUNCTION) {
                    parseFunction();
                    nextToken();
                }

                if(currentToken.getType() == TokenType.BRACKET_OPEN) {
                    parseBlock(false);
                    accept(BRACKET_CLOSE);
                }

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

    private void accept(TokenType tokenType) throws Exception {
        if(!currentToken.getType().equals(tokenType)) {
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

    private void parseFunction() throws Exception {
        Function function = new Function();
        function.setReturnType(parseReturnType());
        function.setId(parseID());
        acceptNextToken(TokenType.PARENTHESIS_OPEN);
        function.setParameters(parseParameters());
        accept(PARENTHESIS_CLOSE);
        function.setBlock(parseBlock());

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

    private boolean isOneOfType(List<TokenType> tokenTypeList) {
        return tokenTypeList.contains(currentToken.getType());
    }

    private Block parseBlock() throws Exception {
        return parseBlock(true);
    }

    private Block parseBlock(boolean a) throws Exception {
        if(a)
            acceptNextToken(TokenType.BRACKET_OPEN);

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

                if(currentToken.getType() == ASSIGN) {
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
        if (currentToken.getType() == LOAD) {
            if (variable.getType() != CSV_TYPE) {
                throw Logger.error("Error at line... Cannot use load function with other variable than of csv type ");
            }
            parseLoad();
        } else if(currentToken.getType() == BRACKET_OPEN){
            parseArrayArgumentList();
            nextToken();
        } else if(currentToken.getType()== STRING_FORMATTER) {
            acceptNextToken(PARENTHESIS_OPEN);
            parseArgumentList();
            accept(PARENTHESIS_CLOSE);
            nextToken();
        } else {
            parseSimpleExpression();
        }

        accept(SEMICOLON);
    }

    private List<Argument> parseArgumentList() throws Exception {

        nextToken();
        int i = 0;
        while(currentToken.getType() != PARENTHESIS_CLOSE) {
            if( i>0) {
                accept(COMMA);
                nextToken();
            }
            parseArgument();
            ++i;
        }
        return null;
    }

    private List<Argument> parseArrayArgumentList() throws Exception {

        nextToken();
        int i = 0;
        while(currentToken.getType() != BRACKET_CLOSE) {
            if( i>0) {
                accept(COMMA);
                nextToken();
            }
            parseArgument();
            ++i;
        }
        return null;
    }

    private void parseArgument() throws Exception {
        acceptOneOfMany(NUMBER, FLOAT_NUMBER, STRING_CONTENT, ID);
        if(currentToken.getType() == ID) {
            nextToken();
            if(currentToken.getType() == SQUARE_BRACKET_OPEN) {
                nextToken();
                acceptOneOfMany(NUMBER, ID);
                acceptNextToken(SQUARE_BRACKET_CLOSE);
                nextToken();
                if(currentToken.getType() == SQUARE_BRACKET_OPEN) {
                    nextToken();
                    acceptOneOfMany(NUMBER, ID);
                    acceptNextToken(SQUARE_BRACKET_CLOSE);
                    nextToken();
                }
            }
        } else {
            nextToken();
        }
    }

    private void parseStatement() throws Exception {
        switch(currentToken.getType()) {
            case IF:
                parseIf();
                break;
            case WHILE:
                parseWhile();
                break;
            case RETURN:
                parseReturn();
                break;
            case LINK:
                parseLink();
                break;
            case HEADER:
                parseHeader();
                break;
            case PARAGRAPH:
                parseParagraph();
                break;
            case IMAGE:
                parseImage();
                break;
            case LIST:
                parseList();
                break;
            case TABLE:
                parseTable();
                break;
            case ID:
                parseMethodCallOrAssignment();
                nextToken();
                break;
        }
    }

    private void parseIf() throws Exception {
        acceptNextToken(PARENTHESIS_OPEN);
        parseExpression();
        accept(PARENTHESIS_CLOSE);
        parseBlock();
        nextToken();
        if(currentToken.getType() == ELSE) {
            parseBlock();
            nextToken();
        }
    }

    private void parseWhile() throws Exception {
        acceptNextToken(PARENTHESIS_OPEN);
        parseExpression();
        accept(PARENTHESIS_CLOSE);
        parseBlock();
        nextToken();
    }

    private void parseReturn() throws Exception {
        nextToken();
        if(currentToken.getType() != SEMICOLON)
            parseExpressionNext(false);
        accept(SEMICOLON);
        nextToken();
    }

    private void parseLink() throws Exception {
        acceptNextToken(PARENTHESIS_OPEN);
        nextToken();
        parseArgument();
        accept(COMMA);
        nextToken();
        parseArgument();
        accept(PARENTHESIS_CLOSE);
        acceptNextToken(SEMICOLON);
        nextToken();
    }

    private void parseLoad() throws Exception { ///needs to be added to variable initializer
        accept(LOAD);
        acceptNextToken(PARENTHESIS_OPEN);
        nextToken();
        parseArgument();
        accept(PARENTHESIS_CLOSE);
        acceptNextToken(SEMICOLON);
    }

    private void parseHeader() throws Exception {
        accept(HEADER);
        acceptNextToken(PARENTHESIS_OPEN);
        nextToken();
        parseArgument();
        accept(PARENTHESIS_CLOSE);
        acceptNextToken(SEMICOLON);
        nextToken();
    }

    private void parseParagraph() throws Exception {
        accept(PARAGRAPH);
        acceptNextToken(PARENTHESIS_OPEN);
        nextToken();
        parseArgument();
        accept(PARENTHESIS_CLOSE);
        acceptNextToken(SEMICOLON);
        nextToken();
    }

    private void parseImage() throws Exception {
        accept(IMAGE);
        acceptNextToken(PARENTHESIS_OPEN);
        nextToken();
        parseArgument();
        accept(PARENTHESIS_CLOSE);
        acceptNextToken(SEMICOLON);
        nextToken();
    }

    private void parseList() throws Exception {
        acceptNextToken(BRACKET_OPEN);
        nextToken();
        while (currentToken.getType() == LIST_ITEM) {
            acceptNextToken(PARENTHESIS_OPEN);
            nextToken();
            parseArgument();
            accept(PARENTHESIS_CLOSE);
            acceptNextToken(SEMICOLON);
            nextToken();
        }

        accept(BRACKET_CLOSE);
        nextToken();
    }

    private void parseTable() throws Exception {
        acceptNextToken(BRACKET_OPEN);
        nextToken();
        while (currentToken.getType() == ROW) {
            parseRow();
        }
        accept(BRACKET_CLOSE);
        nextToken();
    }

    //add accept of many in else to throw exception - not only here
    private void parseRow() throws Exception {
        acceptNextToken(BRACKET_OPEN);
        nextToken();
        while(checkIfIsOneOfTokenTypes(COLUMN, TABLE_ITEM)) {
            acceptOneOfMany(COLUMN, TABLE_ITEM);
            if (currentToken.getType() == COLUMN) {
                acceptNextToken(PARENTHESIS_OPEN);
                nextToken();
                parseArgument();
                accept(PARENTHESIS_CLOSE);
            } else if (currentToken.getType() == TABLE_ITEM) {
                acceptNextToken(PARENTHESIS_OPEN);
                nextToken();
                parseArgument();
                accept(PARENTHESIS_CLOSE);

            }
            acceptNextToken(SEMICOLON);
            nextToken();
        }
        accept(BRACKET_CLOSE);
        nextToken();
    }

    private void parseMethodCallOrAssignment() throws Exception {
        Variable variable = parseVariableOrMethodCall();
        if (variable.getType() == NUMBER) {
            acceptOneOfMany(PERIOD, ASSIGN);
            if(currentToken.getType() == PERIOD) {
                acceptNextToken(ARRAY_APPEND);
                acceptNextToken(PARENTHESIS_OPEN);
                parseArgumentList();
                accept(PARENTHESIS_CLOSE);
                acceptNextToken(SEMICOLON);
            } else if(currentToken.getType() == ASSIGN) {
                parseVariableInit(null);
            }
        } else {
            acceptNextToken(SEMICOLON);
        }
    }

    private Expression parseExpression() throws Exception {
        return parseExpressionNext(true);

    }

    private Expression parseExpressionNext(boolean next) throws Exception {
        if(next)
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
            parseVariableOrMethodCall();
        } else {
            parseLiteral();
        }

        return expression;
    }

    private Variable parseVariableOrMethodCall() throws Exception { /////sprawdzic czy jesli jest csv to dwa brackety
        accept(ID);
        nextToken();

        Variable variable = new Variable();

        if(currentToken.getType() == SQUARE_BRACKET_OPEN) {
            acceptOneOfMany(NUMBER, ID);
            acceptNextToken(SQUARE_BRACKET_CLOSE);
            nextToken();
            if(currentToken.getType() == SQUARE_BRACKET_OPEN) {
                acceptOneOfMany(NUMBER, ID);
                acceptNextToken(SQUARE_BRACKET_CLOSE);
            }
            variable.setType(NUMBER);
        } else if (currentToken.getType() == PARENTHESIS_OPEN) {

            parseArgumentList();
            accept(PARENTHESIS_CLOSE);
            variable.setType(FUNCTION);
        } else {
            variable.setType(NUMBER);
        }

        return variable;
    }

    private Variable parseLiteral() throws Exception {
        acceptOneOfMany(STRING_CONTENT, NUMBER, FLOAT_NUMBER);
        nextToken();
        return null;
    }
}
