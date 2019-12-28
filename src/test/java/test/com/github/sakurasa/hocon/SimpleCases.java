package test.com.github.sakurasa.hocon;

import com.github.sakurasa.hocon.HoconParser;
import com.github.sakurasa.hocon.ParseException;
import org.junit.Assert;
import org.junit.Test;

import static test.com.github.sakurasa.hocon.utils.AssertUtil.*;

public class SimpleCases {

    @Test
    public void testBoolean() throws ParseException {
        // true
        Assert.assertEquals(true, new HoconParser("true").parseBoolean());
        // false
        Assert.assertEquals(false, new HoconParser("false").parseBoolean());
    }

    @Test
    public void testNull() throws ParseException {
        // null
        Assert.assertNull(new HoconParser("null").parseNull());
    }

    @Test
    public void testInteger() throws ParseException {
        // standard cases
        {
            Integer[] cases = new Integer[]{
                    0, 314159, -314159, Integer.MAX_VALUE, Integer.MIN_VALUE
            };
            for (Integer i : cases) {
                Assert.assertEquals(i.intValue(), new HoconParser(i.toString()).parseNumber().intValue());
            }
        }

        // super long
        Assert.assertEquals(314, new HoconParser("00000000000000314").parseNumber().intValue());

        // positive signed
        Assert.assertEquals(314, new HoconParser("+314").parseNumber().intValue());
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
                        new HoconParser(i.toString()).parseNumber().doubleValue(),
                        Math.abs(i * delta));
            }
        }

        // super long
        Assert.assertEquals(
                314000.0,
                new HoconParser("00000000000000314.000000e3").parseNumber().doubleValue(),
                delta);

        // positive signed
        Assert.assertEquals(
                3.14,
                new HoconParser("+314e-2").parseNumber().doubleValue(),
                delta);

        // missing decimal part
        Assert.assertEquals(
                314.0,
                new HoconParser("314.").parseNumber().doubleValue(),
                delta);

        // missing integer part
        Assert.assertEquals(
                0.314,
                new HoconParser(".314").parseNumber().doubleValue(),
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
                        new HoconParser(quote + value + quote).parseString()
                );
            }
        }

        Assert.assertEquals(
                "\b\t\n\r\f\"'\\",
                new HoconParser("\"\\b\\t\\n\\r\\f\\\"\\'\\\\\"").parseString()
        );
    }

    @Test
    public void testList() throws ParseException {
        assertValueEquals(
                makeList(123, 1.23, "hello", null),
                new HoconParser("[123, 1.23, 'hello', null]").parseList()
        );
        assertValueEquals(
                makeList(123, makeList(1.23, makeList("hello", makeList(null, null)))),
                new HoconParser("[123, [1.23, ['hello', [null, null]]]]").parseList()
        );
        assertValueEquals(
                makeList(123, 1.23, "hello", null),
                new HoconParser("[\n123\n 1.23\n\n, 'hello'\n,\n null\n,]").parseList()
        );
        assertValueEquals(
                makeList("naked", "is", "wow"),
                new HoconParser("[naked, is, wow]").parseList()
        );
    }

    @Test
    public void testObject() throws ParseException {
        assertValueEquals(
                makeObject("pi", 3.14, "str", "hello"),
                new HoconParser("{\"pi\": 3.14, \"str\": \"hello\"}").parseObject()
        );
        assertValueEquals(
                makeObject("pi", 3.14, "str", "hello"),
                new HoconParser("{\n  pi: 3.14,\n  str: hello\n}").parseObject()
        );
        assertValueEquals(
                makeObject("key0", makeObject("key1", makeObject("key2", "value"))),
                new HoconParser("{key0{key1:{key2=value}}}").parseObject()
        );
    }
}
