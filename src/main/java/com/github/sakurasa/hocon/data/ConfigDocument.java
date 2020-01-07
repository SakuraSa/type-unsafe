package com.github.sakurasa.hocon.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
