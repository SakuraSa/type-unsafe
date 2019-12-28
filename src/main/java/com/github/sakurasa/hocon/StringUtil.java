package com.github.sakurasa.hocon;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private static final Pattern ESCAPE_CHAR = Pattern.compile("\\\\[btnrf\"'\\\\]");
    private static final Map<String, String> ESCAPE_MAPPING;
    static {
        ESCAPE_MAPPING = new HashMap<>();
        ESCAPE_MAPPING.put("\\b", "\b");
        ESCAPE_MAPPING.put("\\t", "\t");
        ESCAPE_MAPPING.put("\\n", "\n");
        ESCAPE_MAPPING.put("\\r", "\r");
        ESCAPE_MAPPING.put("\\f", "\f");
        ESCAPE_MAPPING.put("\\\"", "\"");
        ESCAPE_MAPPING.put("\\'", "'");
        ESCAPE_MAPPING.put("\\\\", "\\\\");
    }

    public static String unescapeJsonString(CharSequence value, int trim) {
        Matcher matcher = ESCAPE_CHAR.matcher(value);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer,
                    ESCAPE_MAPPING.getOrDefault(matcher.group(), matcher.group()));
        }
        matcher.appendTail(buffer);
        return buffer.substring(trim, buffer.length() - trim);
    }

}
