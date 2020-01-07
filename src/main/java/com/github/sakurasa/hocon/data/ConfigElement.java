package com.github.sakurasa.hocon.data;

import java.util.List;

public abstract class ConfigElement {
    public final Range range;
    public final List<Comment> comments;
    public final ConfigElementType type;

    protected ConfigElement(Range range, List<Comment> comments, ConfigElementType type) {
        this.range = range;
        this.comments = comments;
        this.type = type;
    }

    public abstract java.lang.Object unwrap();

    public boolean canUnwrap() {
        return type.isLocalValue();
    }
}
