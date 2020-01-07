package com.github.sakurasa.hocon.data;

public class Range {
    public final int beginLine, endLine, beginColumn, endColumn;

    public Range(int beginLine, int endLine, int beginColumn, int endColumn) {
        this.beginLine = beginLine;
        this.endLine = endLine;
        this.beginColumn = beginColumn;
        this.endColumn = endColumn;
    }

    @Override
    public String toString() {
        return String.format(
                "L%d.%d:L%d.%d",
                beginLine, beginColumn, endLine, endColumn
        );
    }
}
