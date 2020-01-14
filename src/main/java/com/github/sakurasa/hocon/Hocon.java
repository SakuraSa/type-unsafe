package com.github.sakurasa.hocon;

import com.github.sakurasa.hocon.data.ConfigDocument;
import com.github.sakurasa.hocon.data.ConfigElement;
import com.github.sakurasa.hocon.data.ConfigInclude;
import com.github.sakurasa.hocon.loader.Loader;
import com.github.sakurasa.hocon.loader.LocalFileLoader;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class Hocon {

    public static ConfigDocument loadFileDocument(String path)
            throws IOException, ParseException {
        Context context = new Context(path);
        int cnt = context.loadAllIncludes();
        System.out.println(String.format("total included file %d.", cnt));
        return context.loadRoot();
    }


    public static class Context {
        public Path rootPath;
        public Loader loader;
        public ConfigDocument root;
        public Map<Path, ConfigElement> cache;
        public Map<Path, Path> source;

        public Context(String path) {
            initLoader(path);
            this.root = null;
            this.cache = new HashMap<>();
            this.source = new HashMap<>();
        }

        public ConfigDocument loadRoot() throws IOException, ParseException {
            return loadDocument(rootPath);
        }

        public int loadAllIncludes() throws IOException, ParseException {
            LinkedList<Path> opens = new LinkedList<>();
            LinkedList<Path> sources = new LinkedList<>();
            opens.add(rootPath);
            sources.add(Paths.get("__root__"));
            int cnt = 0, total = 0;

            while (!opens.isEmpty()) {
                Path now = opens.pollFirst();
                Path from = sources.pollFirst();
                total ++;
                if (cache.containsKey(now)) {
                    System.out.println(String.format(
                            "Loading[%d/%d]: %s => %s but already load from %s",
                            cnt, total, from, now, this.source.get(now)
                    ));
                    continue;
                }
                System.out.println(String.format("Loading[%d/%d]: %s => %s", cnt, total, from, now));
                ConfigDocument doc = loadDocument(now);
                this.cache.put(now, doc);
                this.source.put(now, from);
                cnt ++;
                Iterator<ConfigInclude> iterator = doc.iterateIncludes();
                while (iterator.hasNext()) {
                    ConfigInclude include = iterator.next();
                    Path parent = now.getParent();
                    Path nextPath = parent == null
                            ? Paths.get(include.source)
                            : parent.resolve(include.source);
                    opens.addLast(nextPath.normalize());
                    sources.addLast(now);
                }
            }
            return cnt;
        }

        private ConfigDocument loadDocument(Path path) throws IOException, ParseException {
            if (!cache.containsKey(path)) {
                try (Reader reader = loader.getReader(path.toString())) {
                    cache.put(path, new HoconParser(reader).parseDocument());
                }
            }
            return (ConfigDocument) cache.get(path);
        }

        private void initLoader(String path) {
            int sep = path.indexOf("://");
            String scheme = sep >= 0 ? path.substring(0, sep) : "file";
            switch (scheme) {
                case "file":
                    loader = new LocalFileLoader(path);
                    break;
            }
            rootPath = Paths.get(path).getFileName();
        }

    }
}
