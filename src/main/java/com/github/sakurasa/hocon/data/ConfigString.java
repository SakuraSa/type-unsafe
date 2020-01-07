package com.github.sakurasa.hocon.data;

import java.util.List;

public class ConfigString extends ConfigElement {

    public final java.lang.String value;

    public ConfigString(Range range, List<Comment> comments, java.lang.String value) {
        super(range, comments, ConfigElementType.String);
        this.value = value;
    }

    @Override
    public java.lang.Object unwrap() {
        return value;
    }

    @Override
    public java.lang.String toString() {
        return java.lang.String.format("\"%s\"", value);
    }
}
