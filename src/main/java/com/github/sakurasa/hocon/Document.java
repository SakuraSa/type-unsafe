package com.github.sakurasa.hocon;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Document {
    public final List<Object> lines = new ArrayList<>();

    public Map<String, Object> unwrap() {
        Map<String, Object> result = new LinkedHashMap<>();
        for (Object line : lines) {
            if (line instanceof Paragraph) {
                Paragraph paragraph = (Paragraph) line;
                result.put(paragraph.name, paragraph.value);
            } else if (line instanceof Include) {
                Include include = (Include) line;
                @SuppressWarnings("unchecked")
                List<Include> includes = (List<Include>) result
                        .computeIfAbsent("__include__", k -> new ArrayList<>());
                includes.add(include);
            }
        }
        return result;
    }
}
