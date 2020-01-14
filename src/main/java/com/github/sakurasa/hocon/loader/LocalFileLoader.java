package com.github.sakurasa.hocon.loader;

import java.io.*;
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
        File configFile = base.resolve(filePath).toFile();
        if (!configFile.exists()) {
            throw new FileNotFoundException(String.format("Config file \"%s\" not found.", path));
        }
        return new FileReader(configFile);
    }

    @Override
    public boolean exists(String path) {
        return Paths.get(path).toFile().exists();
    }
}
