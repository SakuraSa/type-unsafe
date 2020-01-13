package test.com.github.sakurasa.hocon;

import com.github.sakurasa.hocon.HoconParser;
import com.github.sakurasa.hocon.ParseException;
import com.github.sakurasa.hocon.data.ConfigElement;
import com.github.sakurasa.hocon.data.ConfigNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static test.com.github.sakurasa.hocon.utils.AssertUtil.*;

public class SimpleCases {

    @Test
    public void testBoolean() throws ParseException {
        // true
        Assert.assertTrue(new HoconParser("true").parseBoolean().value);
        // false
        Assert.assertFalse(new HoconParser("false").parseBoolean().value);
    }

    @Test
    public void testNull() throws ParseException {
        // null
        Assert.assertNull(new HoconParser("null").parseNull().value);
    }

    @Test
    public void testInteger() throws ParseException {
        // standard cases
        {
            Integer[] cases = new Integer[]{
                    0, 314159, -314159, Integer.MAX_VALUE, Integer.MIN_VALUE
            };
            for (Integer i : cases) {
                Assert.assertEquals(i.intValue(), new HoconParser(i.toString()).parseNumber().value.intValue());
            }
        }

        // super long
        Assert.assertEquals(314, new HoconParser("00000000000000314").parseNumber().value.intValue());

        // positive signed
        Assert.assertEquals(314, new HoconParser("+314").parseNumber().value.intValue());
    }

    @Test
    public void testDecimal() throws ParseException {
        final double delta = 1e-12;

        // standard cases
        {
            Double[] cases = new Double[]{
                    0.0, 314159.0, -314159.0,
                    Math.PI, -Math.PI, Math.E, -Math.E,
                    Double.MAX_VALUE, Double.MIN_VALUE
            };
            for (Double i : cases) {
                Assert.assertEquals(
                        i,
                        new HoconParser(i.toString()).parseNumber().value.doubleValue(),
                        Math.abs(i * delta));
            }
        }

        // super long
        Assert.assertEquals(
                314000.0,
                new HoconParser("00000000000000314.000000e3").parseNumber().value.doubleValue(),
                delta);

        // positive signed
        Assert.assertEquals(
                3.14,
                new HoconParser("+314e-2").parseNumber().value.doubleValue(),
                delta);

        // missing decimal part
        Assert.assertEquals(
                314.0,
                new HoconParser("314.").parseNumber().value.doubleValue(),
                delta);

        // missing integer part
        Assert.assertEquals(
                0.314,
                new HoconParser(".314").parseNumber().value.doubleValue(),
                delta);
    }

    @Test
    public void testString() throws ParseException {
        final String[] quotes = new String[]{
                "\"", "'", "\"\"\"", "'''"
        };
        final String[] values = new String[]{
                "hello world!",
                "1.234e56",
                "00000000000000000000123456789",
                "!@#$%^&*()_+-=[]{}:;,./<>?~`",
                "中文字符",
        };

        for (String value : values) {
            for (String quote : quotes) {
                Assert.assertEquals(
                        value,
                        new HoconParser(quote + value + quote).parseString().value
                );
            }
        }

        Assert.assertEquals(
                "\b\t\n\r\f\"'\\",
                new HoconParser("\"\\b\\t\\n\\r\\f\\\"\\'\\\\\"").parseString().value
        );
    }

    @Test
    public void testList() throws ParseException {
        assertValueEquals(
                makeList(123, 1.23, "hello", null),
                new HoconParser("[123, 1.23, 'hello', null]").parseArray().unwrap()
        );
        assertValueEquals(
                makeList(123, makeList(1.23, makeList("hello", makeList(null, null)))),
                new HoconParser("[123, [1.23, ['hello', [null, null]]]]").parseArray().unwrap()
        );
        assertValueEquals(
                makeList(123, 1.23, "hello", null),
                new HoconParser("[\n123\n 1.23\n\n, 'hello'\n,\n null\n,]").parseArray().unwrap()
        );
        assertValueEquals(
                makeList("naked", "is", "wow"),
                new HoconParser("[naked, is, wow]").parseArray().unwrap()
        );
    }

    @Test
    public void testObject() throws ParseException {
        assertValueEquals(
                makeObject("pi", 3.14, "str", "hello"),
                new HoconParser("{\"pi\": 3.14, \"str\": \"hello\"}").parseObject().unwrap()
        );
        assertValueEquals(
                makeObject("pi", 3.14, "str", "hello"),
                new HoconParser("{\n  pi: 3.14,\n  str: hello\n,}").parseObject().unwrap()
        );
        assertValueEquals(
                makeObject("key0", makeObject("key1", makeObject("key2", "value"))),
                new HoconParser("{key0{key1:{key2=value}}}").parseObject().unwrap()
        );
    }

    @Test
    public void testComment() throws ParseException {
        assertValueEquals(
                makeObject("hello", "world", "pi", 3.14),
                new HoconParser("{\n hello: world // this is comment! \n, pi: 3.14}").parseObject().unwrap()
        );
        assertValueEquals(
                makeObject("hello", "world", "pi", 3.14),
                new HoconParser("{\n hello: world # this is comment! \n, pi: 3.14}").parseObject().unwrap()
        );
        assertValueEquals(
                makeObject("hello", "world", "pi", 3.14),
                new HoconParser("{\n hello: world /* this\n is\n comment!\n */ \n pi: 3.14}").parseObject().unwrap()
        );
    }

    @Test
    public void testReference() throws ParseException {
        assertValueEquals(
                "${module.path.value}{\"obj\": 1}",
                new HoconParser("${module.path.value}{\n  obj = 1\n}").parseReference()
        );
    }

    @Test
    public void testInclude() throws ParseException {
        assertValueEquals(
                "include \"http://domain.com/setting.conf\"",
                new HoconParser("include \"http://domain.com/setting.conf\"").parseInclude()
        );
        assertValueEquals(
                "include \"http://domain.com/setting.conf\"",
                new HoconParser("{include 'http://domain.com/setting.conf'}").parseInclude()
        );
    }

    @Test
    public void testIncludeInObject() throws ParseException {
        assertValueEquals(
                makeObject("key", "include \"path\""),
                new HoconParser("{key {\n include 'path' \n }\n}").parseObject()
        );
    }

    @Test
    public void testMultiWhiteSpaceLine() throws ParseException {
        new HoconParser("[\na\nb,\n \n \nc\n\t\r, ]").parseArray();
        new HoconParser("{\na:a\nb:\n \nb,\n \n \nc{\n\t\r}, }").parseObject();
    }
}
