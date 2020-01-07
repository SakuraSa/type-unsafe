package com.github.sakurasa.hocon.data;

import java.util.List;

public class ConfigNull extends ConfigElement {

    public final Void value = null;

    public ConfigNull(Range range, List<Comment> comments) {
        super(range, comments, ConfigElementType.Null);
    }

    @Override
    public java.lang.Object unwrap() {
        return null;
    }

    @Override
    public java.lang.String toString() {
        return "Null";
    }
}
