package com.github.sakurasa.hocon.data;

import java.net.URI;
import java.util.List;

public class ConfigInclude extends ConfigElement {

    public final URI source;

    public ConfigInclude(Range range, List<Comment> comments, String source) {
        super(range, comments, ConfigElementType.Include);
        this.source = URI.create(source);
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
