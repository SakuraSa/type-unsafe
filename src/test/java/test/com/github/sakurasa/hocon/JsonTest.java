package test.com.github.sakurasa.hocon;

import com.github.sakurasa.hocon.JsonParser;
import com.github.sakurasa.hocon.ParseException;
import com.google.gson.Gson;
import org.junit.Test;
import test.com.github.sakurasa.hocon.utils.AssertUtil;

import java.util.*;

public class JsonTest {

    private static Gson gson = new Gson();

    private static void testJson(String content) throws ParseException {
        long begin = System.nanoTime();
        Object actual = new JsonParser(content).parseAny().unwrap();
        long parserCost = System.nanoTime() - begin;
        begin = System.nanoTime();
        Class<?> type = actual == null ? Void.class : actual.getClass();
        Object expected = gson.fromJson(content, type);
        long gsonCost = System.nanoTime() - begin;
        AssertUtil.assertValueEquals(expected, actual);
        System.out.println(String.format(
                "content size %d parser cost %.2fms gson cost %.2fms",
                content.length(),
                parserCost / 1e6,
                gsonCost / 1e6
        ));
    }

    @Test
    public void testNull() throws ParseException {
        testJson("null");
    }

    @Test
    public void testBoolean() throws ParseException {
        testJson("true");
        testJson("false");
    }

    @Test
    public void testNumber() throws ParseException {
        testJson("123123");
        testJson(Long.toString(Long.MAX_VALUE));
        testJson(Long.toString(Long.MIN_VALUE));
        testJson(Double.toString(Math.PI));
        testJson(Double.toString(Math.E));
        testJson("+1.23e5");
        testJson("1.e3");
    }

    @Test
    public void testString() throws ParseException {
        testJson(gson.toJson("hello world!"));
        testJson(gson.toJson("中文汉字"));
        testJson(gson.toJson("\r\n\f\t\b\\\"'~!@#$%^&*()[]{};:?<>,."));
        {
            int length = 1024 * 10;
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append((char) (Objects.hash(i, length) % 256));
            }
            testJson(gson.toJson(builder.toString()));
        }
    }

    @Test
    public void testArray() throws ParseException {
        testJson(gson.toJson(AssertUtil.makeList(
                "hello", 123, 1.23, null
        )));
        {
            testJson(gson.toJson(AssertUtil.makeList(
                    "0", AssertUtil.makeList(
                            "1", AssertUtil.makeObject(
                                    "pi", 3.14
                            )
                    )
            )));
        }
        {
            int length = 1024 * 10;
            List<Long> array = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                array.add((long) Objects.hash(i, length));
            }
            testJson(gson.toJson(array));
        }
        {
            int depth = 1024;
            List<Object> root = new ArrayList<>(1), ptr = root;
            for (int i = 0; i < depth; i++) {
                List<Object> newLayer = new ArrayList<>(1);
                ptr.add(newLayer);
                ptr = newLayer;
            }
            ptr.add("finally!");
            testJson(gson.toJson(root));
        }
    }

    @Test
    public void testObject() throws ParseException {
        testJson(gson.toJson(AssertUtil.makeObject(
                "pi", 3.14,
                "null", null,
                "hello", "world"
        )));
        testJson(gson.toJson(AssertUtil.makeObject(
                "level0", AssertUtil.makeObject(
                        "level1", AssertUtil.makeObject(
                                "hello", "world"
                        )))));
        {
            int length = 1024 * 10;
            Map<String, Object> object = new HashMap<>(length);
            for (int i = 0; i < length; i++) {
                object.put(
                        Integer.toString(Objects.hash(i, length), 16),
                        Integer.toString(Objects.hash(length - i, length), 16)
                );
            }
            testJson(gson.toJson(object));
        }
        {
            int depth = 1024;
            Map<String, Object> root = new HashMap<>(1), ptr = root;
            for (int i = 0; i < depth; i++) {
                Map<String, Object> newLayer = new HashMap<>(1);
                ptr.put(Integer.toString(i), newLayer);
                ptr = newLayer;
            }
            ptr.put("finally", "this is the end!");
            testJson(gson.toJson(root));
        }
    }
}
