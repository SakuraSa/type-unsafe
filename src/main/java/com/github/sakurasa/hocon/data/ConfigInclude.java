package com.github.sakurasa.hocon.data;

import java.util.List;

public class ConfigInclude extends ConfigElement {

    public final String source;

    public ConfigInclude(Range range, List<Comment> comments, String source) {
        super(range, comments, ConfigElementType.Include);
        this.source = source;
    }

    @Override
    public Object unwrap() {
        return toString();
    }

    @Override
    public String toString() {
        return String.format("include \"%s\"", source);
    }
}
