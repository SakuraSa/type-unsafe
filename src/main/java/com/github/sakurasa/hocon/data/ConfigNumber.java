package com.github.sakurasa.hocon.data;

import java.util.List;

public class ConfigNumber extends ConfigElement {

    public final java.lang.Number value;

    public ConfigNumber(Range range, List<Comment> comments, java.lang.Number value) {
        super(range, comments, ConfigElementType.Number);
        this.value = value;
    }

    @Override
    public java.lang.Object unwrap() {
        return value;
    }

    @Override
    public java.lang.String toString() {
        return java.lang.String.format("%s", value);
    }
}
