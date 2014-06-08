package org.dontexist.kb.service.converter;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:app-context.xml"})
public class PalladioIT2UnicodeConverterServiceImplTest {

    @Autowired
    private PalladioIT2UnicodeConverterServiceImpl converter;

    // ----------- HELPER METHODS --------------------

    private void verify(final String palladioInputString, final String expected) {
        String actual = converter.convert(palladioInputString);
        Assert.assertEquals(expected, actual);
    }

    private void verifyHtml(final String palladioInputString, final String expected) {
        String actual = converter.convertHtmlBlock(palladioInputString);
        Assert.assertEquals(expected, actual);
    }

    // ----------- WORD CONVERSION TESTS -------------

    @Test
    public void testConvert1() {
        String input = "vivähadinamidaà bhavatu harñadam,";
        String expected = "vivāhadinamidaṁ bhavatu harṣadam,";
        verify(input, expected);
    }

    @Test
    public void testConvert2() {
        String input = "kasyacidapi sambandhasya-ädhäro yadi laukikas-tarhi sa bandhana-käraka, ädhyätmikastu mukti-däyako bhavati. – Jivansüträëi 5.5";
        String expected = "kasyacidapi sambandhasya-ādhāro yadi laukikas-tarhi sa bandhana-kāraka, ādhyātmikastu mukti-dāyako bhavati. – Jivansūtrāṇi 5.5";
        verify(input, expected);
    }

    @Test
    public void testConvert4() {
        String input = "Jïänänanda";
        String expected = "Jñānānanda";
        verify(input, expected);
    }

    @Test
    public void testConvert5() {
        String input = "Jïänänanda fiancé";
        String expected = "Jñānānanda fiancé";
        verify(input, expected);
    }

    // ----------- HTML BLOCK CONVERSION TESTS ------------

    @Test
    public void testConvert3() {
        String input = "<div class=\"italictext2\">\n    <sup class=\"calibre6\"><a href=\"../Text/part0007.html#ch2_n1\" id=\"ch2_no1\">1</a></sup>&nbsp; kasyacidapi sambandhasya-ädhäro yadi laukikas-tarhi sa bandhana-käraka, ädhyätmikastu mukti-däyako bhavati. – Jivansüträëi 5.5\n  </div>";
        String expected = "<div class=\"italictext2\">\n    <sup class=\"calibre6\"><a href=\"../Text/part0007.html#ch2_n1\" id=\"ch2_no1\">1</a></sup>&nbsp; kasyacidapi sambandhasya-ādhāro yadi laukikas-tarhi sa bandhana-kāraka, ādhyātmikastu mukti-dāyako bhavati. – Jivansūtrāṇi 5.5\n  </div>";
        verifyHtml(input, expected);
    }

    // ----------- CHARACTER CONVERSION TESTS -------------

    // @formatter:off
    @Test
    public void test_0x00D1_to_0x1E62() {
        verify("Ñ", "Ṣ");
    }

    @Test
    public void test_0x00CF_to_0x00D1() {
        // ORDER dependant test!
        verify("Ï", "Ñ");
    }

    @Test
    public void test_0x00F1_to_0x1E63() {
        verify("ñ", "ṣ");
    }

    @Test
    public void test_0x00EF_to_0x00F1() {
        // ORDER dependant test!
        verify("ï", "ñ");
    }

    @Test
    public void test_0x00C4_to_0x0100() {
        verify("Ä", "Ā");
    }

    @Test
    public void test_0x00E4_to_0x0101() {
        verify("ä", "ā");
    }

    @Test
    public void test_0x00C9_to_0x012A() {
        verify("É", "Ī");
    }

    @Test
    public void test_0x00E9_to_0x012B() {
        verify("é", "ī");
    }

    @Test
    public void test_0x00C7_to_0x015A() {
        verify("Ç", "Ś");
    }

    @Test
    public void test_0x00E7_to_0x015B() {
        verify("ç", "ś");
    }

    @Test
    public void test_0x00DC_to_0x016A() {
        verify("Ü", "Ū");
    }

    @Test
    public void test_0x00FC_to_0x016B() {
        verify("ü", "ū");
    }

    @Test
    public void test_0x00D2_to_0x1E0C() {
        verify("Ò", "Ḍ");
    }

    @Test
    public void test_0x00F2_to_0x1E0D() {
        verify("ò", "ḍ");
    }

    @Test
    public void test_0x00D9_to_0x1E24() {
        verify("Ù", "Ḥ");
    }

    @Test
    public void test_0x00F9_to_0x1E25() {
        verify("ù", "ḥ");
    }

    @Test
    public void test_0x00DF_to_0x1E36() {
        verify("ß", "Ḷ");
    }

    @Test
    public void test_0x00C0_to_0x1E40() {
        verify("À", "Ṁ");
    }

    @Test
    public void test_0x00E0_to_0x1E41() {
        verify("à", "ṁ");
    }

    @Test
    public void test_0x00CC_to_0x1E44() {
        verify("Ì", "Ṅ");
    }

    @Test
    public void test_0x00EC_to_0x1E45() {
        verify("ì", "ṅ");
    }

    @Test
    public void test_0x00CB_to_0x1E46() {
        verify("Ë", "Ṇ");
    }

    @Test
    public void test_0x00EB_to_0x1E47() {
        verify("ë", "ṇ");
    }

    @Test
    public void test_0x00C5_to_0x1E5A() {
        verify("Å", "Ṛ");
    }

    @Test
    public void test_0x00E5_to_0x1E5B() {
        verify("å", "ṛ");
    }

    @Test
    public void test_0x00C8_to_0x1E5C() {
        verify("È", "Ṝ");
    }

    @Test
    public void test_0x00E8_to_0x1E5D() {
        verify("è", "ṝ");
    }

    @Test
    public void test_0x00D6_to_0x1E6C() {
        verify("Ö", "Ṭ");
    }

    @Test
    public void test_0x00F6_to_0x1E6D() {
        verify("ö", "ṭ");
    }

    @Test
    public void test_0x00D7_to_0x030D() {
        verify("×", "̍");
        // Vedik accent '
    }

    @Test
    public void test_0x00DE_to_0x0331() {
        verify("Þ", "̱");
        // Vedik accent _
    }


    @Test
    public void test_0x00F7_to_0x030E() {
        verify("÷", "̎");
        // Vedik accent ''
    }

    // ----------- NON-CONVERSION TESTS -------------

    //
    @Test
    public void testNonconversion() {
        String input = "fiancé";
        String expected = input;
        verify(input, expected);
    }

    // ----------- ESCAPE CHARACTERS CONVERSION TESTS -------------

    @Test
    public void testEscape1() {
        String input = "Q &amp; A";
        String expected = input;
        verify(input, expected);
    }

    @Test
    public void testEscape2() {
        String input = "<p class=\"justify3\"><span class=\"bodytext\"><a href=\"../Text/part0009.html#ch4\">Q &amp; A</a></span></p>";
        String expected = input;
        verifyHtml(input, expected);
    }

}
