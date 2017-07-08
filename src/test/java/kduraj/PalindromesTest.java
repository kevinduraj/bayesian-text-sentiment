package kduraj;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class PalindromesTest {

    @Test
    public void test() {
        assertFalse(isPalindrome("ab"));
        assertFalse(isPalindrome(""));
        assertFalse(isPalindrome(null));
        assertFalse(isPalindrome("abb"));
        assertFalse(isPalindrome("a _ba"));

        assertTrue(isPalindrome("aba"));
        assertTrue(isPalindrome("abba"));
        assertTrue(isPalindrome("abcba"));
    }

    private static boolean isPalindrome(String string) {
        if (StringUtils.isBlank(string))
            return false;

        int size = string.length();
        int midle = size >> 1;
        for (int i = 0; i < midle; i++)
            if (string.charAt(i) != string.charAt(size - i - 1))
                return false;

        return true;
    }

}
