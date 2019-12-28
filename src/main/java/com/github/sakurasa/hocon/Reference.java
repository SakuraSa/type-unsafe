package com.github.sakurasa.hocon;

public class Reference {
    public final String source;
    public final Object overwrite;

    public Reference(String source, Object overwrite) {
        this.source = source;
        this.overwrite = overwrite;
    }
}
