package kduraj;

import junit.framework.Assert;

import org.junit.Test;

public class AToBTest {

    @Test
    public void test() {
        Assert.assertEquals("pbnbmb", changeAB("panama"));
        Assert.assertEquals("Americb", changeAB("America"));
        Assert.assertEquals("Bbhbmbs", changeAB("Bahamas"));
        Assert.assertEquals("", changeAB(""));

        Assert.assertNull(changeAB(null));
    }

    private static String changeAB(String string) {
        if (string == null)
            return null;
        if ("".equals(string))
            return "";

        return ('a' == string.charAt(0) ? 'b' : string.charAt(0)) + changeAB(string.substring(1));
    }

}
