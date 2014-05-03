package org.dontexist.kb.visual;

import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnicodeCharacterCreatorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnicodeCharacterCreatorTest.class);

    /**
     * Visual test to print the unicode ॐ character to the console properly.
     */
    @Test
    public void test() throws UnsupportedEncodingException {
        char[] _0950 = Character.toChars(0x950);
        System.out.println(_0950[0]);
        String s = Character.toString((char) 2384);
        s = new String(Character.toString((char) 2384).getBytes(), "UTF8");
        System.out.println(s);
        System.out.println("ॐ");
    }

    /**
     * Visual test to print the unicode Ï character to the console propertly.
     */
    @Test
    public void test2() throws Exception {
        char c = 0xCF;
        System.out.println(c);

        String s = new String(Character.toString((char) 0xCF).getBytes(), "UTF8");
        System.out.println(s);
    }

}
