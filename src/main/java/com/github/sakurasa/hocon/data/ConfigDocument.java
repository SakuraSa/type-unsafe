package com.github.sakurasa.hocon.data;

import com.github.sakurasa.hocon.IteratorUtil;

import java.util.*;

public class ConfigDocument extends ConfigElement {

    public final List<ConfigInclude> includes;
    public final Map<String, ? extends ConfigElement> value;
    private Map<String, ?> unwrapped;

    public ConfigDocument(Range range, List<Comment> comments,
                          List<ConfigInclude> includes,
                          Map<String, ? extends ConfigElement> value) {
        super(range, comments, ConfigElementType.Document);
        this.includes = includes;
        this.value = value;
    }

    @Override
    public Object unwrap() {
        if (unwrapped == null) {
            int size = value.size();
            if (!includes.isEmpty()) {
                size ++;
            }
            Map<String, Object> map = new HashMap<>(size);
            value.forEach((key, value) -> map.put(key, value.unwrap()));
            List<Object> unwrappedIncludes = new ArrayList<>(includes.size());
            includes.forEach(include -> unwrappedIncludes.add(include.unwrap()));
            map.put("__include__", unwrappedIncludes);
            unwrapped = map;
        }
        return unwrapped;
    }

    @Override
    public String toString() {
        return String.format("{\"includes\":%s,\"value\":%s}", includes, value);
    }

    @Override
    public Iterator<ConfigInclude> iterateIncludes() {
        return IteratorUtil.concat(
                includes.iterator(),
                IteratorUtil.flatMap(value.values().iterator(), config -> {
                    if (config instanceof ConfigInclude) {
                        return IteratorUtil.singleton((ConfigInclude) config);
                    } else {
                        return config.iterateIncludes();
                    }
                })
        );
    }
}
