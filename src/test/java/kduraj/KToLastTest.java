package kduraj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class KToLastTest {

    @Test
    public void test() {
        String[] a = { "a", "b", "c", "d", "e" };
        List<String> aList = new ArrayList<String>(Arrays.asList(a));
        assertNull(kToLast(null, 2));
        assertNull(kToLast(new ArrayList<String>(), 2));
        assertNull(kToLast(aList, 5));
        assertNull(kToLast(aList, -1));
        assertEquals("c", kToLast(aList, 2));
        assertEquals("e", kToLast(aList, 0));
        assertEquals("a", kToLast(aList, 4));

    }

    public static String kToLast(List<String> a, int offset) {
        if (a == null || a.isEmpty())
            return null;

        if (offset < 0)
            return null;

        int idx = a.size() - offset - 1;

        if (idx < 0)
            return null;

        return a.get(idx);
    }
}
