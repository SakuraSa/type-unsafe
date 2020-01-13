package com.github.sakurasa.hocon.data;

import com.github.sakurasa.hocon.IteratorUtil;

import java.util.Iterator;
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

    @Override
    public Iterator<ConfigInclude> iterateIncludes() {
        if (modifiers == null || modifiers.isEmpty()) {
            return super.iterateIncludes();
        }
        return IteratorUtil.flatMap(modifiers.iterator(), config -> {
            if (config instanceof ConfigInclude) {
                return IteratorUtil.singleton((ConfigInclude) config);
            } else {
                return config.iterateIncludes();
            }
        });
    }
}
