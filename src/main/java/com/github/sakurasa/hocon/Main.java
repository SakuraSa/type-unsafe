package com.github.sakurasa.hocon;

import com.github.sakurasa.hocon.data.ConfigDocument;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String... args) throws Exception {
        final Path configDir = Paths.get(args[0]);

        long begin = System.nanoTime();
        ConfigDocument doc = Hocon.loadFileDocument(configDir.toString());
        System.out.println(doc);
        long cost = System.nanoTime() - begin;
        System.out.println(String.format("Cost %.2fs", cost / 1e9));
    }
}
