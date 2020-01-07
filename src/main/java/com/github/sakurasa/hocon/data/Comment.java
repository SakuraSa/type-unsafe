package com.github.sakurasa.hocon.data;

public class Comment {
    public final String content;
    public final Range range;

    public Comment(String content, Range range) {
        this.content = content;
        this.range = range;
    }

    @Override
    public String toString() {
        return String.format("Comment{%s}", content);
    }
}
