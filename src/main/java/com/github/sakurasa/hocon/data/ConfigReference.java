package com.github.sakurasa.hocon.data;

import java.util.List;

public class ConfigReference extends ConfigElement {

    private final String source;
    private final List<ConfigElement> modifiers;

    public ConfigReference(Range range, List<Comment> comments,
                              String source, List<ConfigElement> modifiers) {
        super(range, comments, ConfigElementType.Reference);
        this.source = source;
        this.modifiers = modifiers;
    }

    @Override
    public Object unwrap() {
        return toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("${").append(source).append("}");
        for (ConfigElement modifier : modifiers) {
            builder.append(modifier);
        }
        return builder.toString();
    }
}
