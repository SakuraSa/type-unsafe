package test.com.github.sakurasa.hocon.utils;

import org.junit.Assert;

import java.util.*;

public class AssertUtil {

    @SuppressWarnings({"rawtypes"})
    static void assertListEquals(Collection listA, Collection listB) {
        Assert.assertNotNull(listA);
        Assert.assertNotNull(listB);
        Assert.assertEquals(listA.size(), listB.size());
        Iterator iteratorA = listA.iterator();
        Iterator iteratorB = listB.iterator();
        while (iteratorA.hasNext() && iteratorB.hasNext()) {
            assertValueEquals(iteratorA.next(), iteratorB.next());
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    static void assertObjectEquals(Map objA, Map objB) {
        Assert.assertNotNull(objA);
        Assert.assertNotNull(objB);
        Assert.assertEquals(objA.size(), objB.size());
        Iterator<Map.Entry> iteratorA = objA.entrySet().iterator();
        Iterator<Map.Entry> iteratorB = objB.entrySet().iterator();
        while (iteratorA.hasNext() && iteratorB.hasNext()) {
            Map.Entry a = iteratorA.next();
            Map.Entry b = iteratorB.next();
            assertValueEquals(a.getKey(), b.getKey());
            assertValueEquals(a.getValue(), b.getValue());
        }
    }

    @SuppressWarnings({"rawtypes"})
    public static void assertValueEquals(Object a, Object b) {
        if (a == b) {
            return;
        }

        if (a instanceof Number && b instanceof Number) {
            Assert.assertEquals(((Number) a).longValue(), ((Number) b).longValue());
            Assert.assertEquals(((Number) a).doubleValue(), ((Number) b).doubleValue(), 1e-12);
        } else if (a instanceof Collection && b instanceof Collection) {
            assertListEquals((Collection) a, (Collection) b);
        } else if (a instanceof Map && b instanceof Map) {
            assertObjectEquals((Map) a, (Map) b);
        } else {
            Assert.assertEquals(a, b);
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
