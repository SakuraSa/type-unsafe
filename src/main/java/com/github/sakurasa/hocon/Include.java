package com.github.sakurasa.hocon;

public class Include {
    public final String source;

    public Include(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return String.format(
                "include \"%s\"", source
        );
    }
}
