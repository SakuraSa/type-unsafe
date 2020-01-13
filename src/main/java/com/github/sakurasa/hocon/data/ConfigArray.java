package com.github.sakurasa.hocon.data;

import com.github.sakurasa.hocon.IteratorUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConfigArray extends ConfigElement {

    public List<? extends ConfigElement> value;
    private List<?> unwrapped = null;

    public ConfigArray(Range range, List<Comment> comments, List<? extends ConfigElement> value) {
        super(range, comments, ConfigElementType.Array);
        this.value = value;
    }

    @Override
    public java.lang.Object unwrap() {
        if (unwrapped == null) {
            List<java.lang.Object> list = new ArrayList<>(value.size());
            value.forEach(item -> list.add(item.unwrap()));
            unwrapped = list;
        }
        return unwrapped;
    }

    @Override
    public String toString() {
        return String.format("%s", value);
    }

    @Override
    public Iterator<ConfigInclude> iterateIncludes() {
        return IteratorUtil.flatMap(value.iterator(), config -> {
            if (config instanceof ConfigInclude) {
                return IteratorUtil.singleton((ConfigInclude) config);
            } else {
                return config.iterateIncludes();
            }
        });
    }
}
