package com.github.sakurasa.hocon.data;

import java.util.List;

public class ConfigBoolean extends ConfigElement {

    public final boolean value;

    public ConfigBoolean(Range range, List<Comment> comments, boolean value) {
        super(range, comments, ConfigElementType.Boolean);
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
