package com.github.sakurasa.hocon.data;

public enum ConfigElementType {
    Null(true),
    Boolean(true),
    Number(true),
    String(true),
    Array(true),
    Object(true),
    Reference(false),
    Include(false),
    Document(true);

    private final boolean localValue;

    ConfigElementType(boolean localValue) {
        this.localValue = localValue;
    }

    public boolean isLocalValue() {
        return localValue;
    }
}
