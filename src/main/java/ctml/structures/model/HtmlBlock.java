package ctml.structures.model;

import ctml.interpreter.Interpreter;

public class HtmlBlock implements Block {

    private String htmlContent;

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    @Override
    public void execute() {
        Interpreter.writer.print(htmlContent);
    }
}
