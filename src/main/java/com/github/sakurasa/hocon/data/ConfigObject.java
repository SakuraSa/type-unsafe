package com.github.sakurasa.hocon.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ConfigObject extends ConfigElement {

    public final Map<String, ? extends ConfigElement> value;
    private Map<String, ?> unwrapped;

    public ConfigObject(Range range, List<Comment> comments, Map<String, ? extends ConfigElement> value) {
        super(range, comments, ConfigElementType.Object);
        this.value = value;
    }

    @Override
    public Object unwrap() {
        if (unwrapped == null) {
            Map<String, Object> map = new HashMap<>(value.size());
            value.forEach((key, value) -> map.put(key, value.unwrap()));
            unwrapped = map;
        }
        return unwrapped;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder().append('{');
        Iterator<String> keyIterator = value.keySet().iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            ConfigElement item = value.get(key);
            builder.append('"').append(key).append("\": ").append(item);
            if (keyIterator.hasNext()) {
                builder.append(',');
            }
        }
        return builder.append('}').toString();
    }
}
