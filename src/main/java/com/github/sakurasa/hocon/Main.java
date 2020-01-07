package com.github.sakurasa.hocon;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String... args) throws Exception {
        final Path configDir = Paths.get(args[0]);
        final List<Path> confFiles = Files.walk(configDir)
                .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".conf"))
                .collect(Collectors.toList());

        for (Path confFile : confFiles) {
            readFile(confFile);
        }
    }

    private static void readFile(Path configPath) throws IOException, ParseException {
        try (Reader reader = new InputStreamReader(
                new FileInputStream(configPath.toFile()),
                StandardCharsets.UTF_8
        )) {
            System.out.println(configPath.toString());
            new HoconParser(reader).parseDocument().unwrap();
        }
    }
}
