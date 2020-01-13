package com.github.sakurasa.hocon.loader;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalFileLoader implements Loader {

    public final Path base;
    public final Path rootFile;

    public LocalFileLoader(String rootFilePath) {
        this.rootFile = Paths.get(rootFilePath);
        this.base = rootFile.getParent();
    }

    @Override
    public Reader getReader(String path) throws IOException {
        Path filePath = Paths.get(path);
        // fix missing ext
        if (!filePath.getFileName().toString().toLowerCase().endsWith(".conf")) {
            if (!exists(path)) {
                filePath = Paths.get(path + ".conf");
            }
        }
        return new FileReader(base.resolve(filePath).toFile());
    }

    @Override
    public boolean exists(String path) {
        return Paths.get(path).toFile().exists();
    }
}
