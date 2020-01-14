package com.github.sakurasa.hocon.loader;

import java.io.IOException;
import java.io.Reader;

public interface Loader {

    Reader getReader(String path) throws IOException;

    boolean exists(String path);

}
