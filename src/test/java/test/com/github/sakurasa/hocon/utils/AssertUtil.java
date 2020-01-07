package test.com.github.sakurasa.hocon.utils;

import com.github.sakurasa.hocon.data.ConfigElement;
import org.junit.Assert;

import java.util.*;

public class AssertUtil {

    @SuppressWarnings({"rawtypes"})
    static void assertListEquals(Collection expected, Collection actual) {
        Assert.assertNotNull(expected);
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.size(), actual.size());
        Iterator iteratorA = expected.iterator();
        Iterator iteratorB = actual.iterator();
        while (iteratorA.hasNext() && iteratorB.hasNext()) {
            assertValueEquals(iteratorA.next(), iteratorB.next());
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    static void assertObjectEquals(Map expected, Map actual) {
        Assert.assertNotNull(expected);
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.size(), actual.size());
        for (Map.Entry a : (Iterable<Map.Entry>) expected.entrySet()) {
            Assert.assertTrue(actual.containsKey(a.getKey()));
            assertValueEquals(a.getValue(), actual.get(a.getKey()));
        }
    }

    @SuppressWarnings({"rawtypes"})
    public static void assertValueEquals(Object expected, Object actual) {
        if (expected == actual) {
            return;
        }

        if (expected instanceof Number && actual instanceof Number) {
            Assert.assertEquals(((Number) expected).longValue(), ((Number) actual).longValue());
            Assert.assertEquals(((Number) expected).doubleValue(), ((Number) actual).doubleValue(), 1e-12);
        } else if (expected instanceof Collection && actual instanceof Collection) {
            assertListEquals((Collection) expected, (Collection) actual);
        } else if (expected instanceof Map && actual instanceof Map) {
            Assert.assertEquals(((Map) expected).size(), ((Map) actual).size());
            Set<Object> keys = new HashSet<>();
            keys.addAll(((Map) expected).keySet());
            keys.addAll(((Map) actual).keySet());
            for (Object key : keys) {
                assertValueEquals(((Map) expected).get(key), ((Map) actual).get(key));
            }
        } else if (actual instanceof ConfigElement) {
            ConfigElement element = (ConfigElement) actual;
            if (element.canUnwrap()) {
                assertValueEquals(expected, element.unwrap());
            } else {
                assertValueEquals(expected, element.toString());
            }
        } else {
            Assert.assertEquals(expected, actual);
        }
    }

    public static Map<String, Object> makeObject(
            String key0, Object value0
    ) {
        Map<String, Object> obj = new HashMap<>();
        obj.put(key0, value0);
        return obj;
    }

    public static Map<String, Object> makeObject(
            String key0, Object value0,
            String key1, Object value1
    ) {
        Map<String, Object> obj = new HashMap<>();
        obj.put(key0, value0);
        obj.put(key1, value1);
        return obj;
    }

    public static Map<String, Object> makeObject(
            String key0, Object value0,
            String key1, Object value1,
            String key2, Object value2
    ) {
        Map<String, Object> obj = new HashMap<>();
        obj.put(key0, value0);
        obj.put(key1, value1);
        obj.put(key2, value2);
        return obj;
    }
    public static Map<String, Object> makeObject(
            String key0, Object value0,
            String key1, Object value1,
            String key2, Object value2,
            String key3, Object value3
    ) {
        Map<String, Object> obj = new HashMap<>();
        obj.put(key0, value0);
        obj.put(key1, value1);
        obj.put(key2, value2);
        obj.put(key3, value3);
        return obj;
    }

    public static List<Object> makeList(Object... elements) {
        return Arrays.asList(elements);
    }
}
