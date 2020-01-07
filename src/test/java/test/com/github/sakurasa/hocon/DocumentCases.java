package test.com.github.sakurasa.hocon;

import com.github.sakurasa.hocon.HoconParser;
import com.github.sakurasa.hocon.ParseException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import test.com.github.sakurasa.hocon.utils.AssertUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

public class DocumentCases {

    private static HoconParser parseHocon(String resource) throws IOException {
        try (InputStream is = DocumentCases.class.getResourceAsStream(resource)) {
            Scanner scanner = new Scanner(is, "UTF-8");
            String content = scanner.useDelimiter("\\Z").next();
            return new HoconParser(content);
        }
    }

    private static Map<String, Object> parseJson(String resource) throws IOException {
        try (InputStream is = DocumentCases.class.getResourceAsStream(resource)) {
            try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                Gson gson = new Gson();
                return gson.fromJson(reader, new TypeToken<Map<String, Object>>(){}.getType());
            }
        }
    }

    private static void testCaseFile(String name) throws IOException, ParseException {
        String hoconPath = String.format("/document/%s.conf", name);
        String jsonPath = String.format("/document/%s.json", name);
        HoconParser parser = parseHocon(hoconPath);
        Map<String, Object> json = parseJson(jsonPath);
        AssertUtil.assertValueEquals(json, parser.parseDocument());
    }

    @Test
    public void testSimpleDocument000() throws ParseException, IOException {
       testCaseFile("simple000");
    }

    @Test
    public void testSimpleDocument001() throws ParseException, IOException {
        testCaseFile("simple001");
    }

    @Test
    public void testSimpleDocument002() throws ParseException, IOException {
        testCaseFile("simple002");
    }

    @Test
    public void testSimpleDocument003() throws ParseException, IOException {
        testCaseFile("simple003");
    }
}
