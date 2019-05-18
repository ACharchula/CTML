package ctml.structures.model;

import ctml.interpreter.Interpreter;

public class HtmlBlock implements Node {

    String htmlContent;

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    @Override
    public String getStructure() {
        return "";
    }

    @Override
    public void execute() {
        Interpreter.writer.print(htmlContent);
    }
}
