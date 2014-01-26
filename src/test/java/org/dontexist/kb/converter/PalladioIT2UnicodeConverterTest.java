package org.dontexist.kb.converter;

import junit.framework.Assert;

import org.apache.commons.lang3.StringEscapeUtils;
import org.dontexist.kb.converter.PalladioIT2UnicodeConverter;
import org.junit.Test;

public class PalladioIT2UnicodeConverterTest {

	private PalladioIT2UnicodeConverter converter = new PalladioIT2UnicodeConverter();

	// ----------- HELPER METHODS --------------------

	private void verify(final Character palladioInputChar, final String expected) throws Exception {
		String actual = converter.convert(palladioInputChar.toString());
		Assert.assertEquals(expected, actual);
	}

	private void verify(final String palladioInputString, final String expected) throws Exception {
		String actual = converter.convert(palladioInputString);
		Assert.assertEquals(expected, actual);
	}

	private void verifyHtml(final String palladioInputString, final String expected) throws Exception {
		String actual = converter.convertHtmlBlock(palladioInputString);
		Assert.assertEquals(expected, actual);
	}

	// ----------- WORD CONVERSION TESTS -------------

	@Test
	public void testConvert1() throws Exception {
		String input = "vivähadinamidaà bhavatu harñadam,";
		String expected = "vivāhadinamidaṁ bhavatu harṣadam,";
		verify(input, expected);
	}

	@Test
	public void testConvert2() throws Exception {
		String input = "kasyacidapi sambandhasya-ädhäro yadi laukikas-tarhi sa bandhana-käraka, ädhyätmikastu mukti-däyako bhavati. – Jivansüträëi 5.5";
		String expected = "kasyacidapi sambandhasya-ādhāro yadi laukikas-tarhi sa bandhana-kāraka, ādhyātmikastu mukti-dāyako bhavati. – Jivansūtrāṇi 5.5";
		verify(input, expected);
	}

	@Test
	public void testConvert4() throws Exception {
		String input = "Jïänänanda";
		String expected = "Jñānānanda";
		verify(input, expected);
	}

	// ----------- HTML BLOCK CONVERSION TESTS ------------

	@Test
	public void testConvert3() throws Exception {
		String input = "<div class=\"italictext2\">\n    <sup class=\"calibre6\"><a href=\"../Text/part0007.html#ch2_n1\" id=\"ch2_no1\">1</a></sup>&nbsp; kasyacidapi sambandhasya-ädhäro yadi laukikas-tarhi sa bandhana-käraka, ädhyätmikastu mukti-däyako bhavati. – Jivansüträëi 5.5\n  </div>";
		String expected = "<div class=\"italictext2\">\n    <sup class=\"calibre6\"><a href=\"../Text/part0007.html#ch2_n1\" id=\"ch2_no1\">1</a></sup>&nbsp; kasyacidapi sambandhasya-ādhāro yadi laukikas-tarhi sa bandhana-kāraka, ādhyātmikastu mukti-dāyako bhavati. – Jivansūtrāṇi 5.5\n  </div>";
		verifyHtml(input, expected);
	}

	// ----------- CHARACTER CONVERSION TESTS -------------

	// @formatter:off
	@Test public void test_0x00D1_to_0x1E62() throws Exception { verify( "Ñ", "Ṣ"); }
	@Test public void test_0x00CF_to_0x00D1() throws Exception { verify( "Ï", "Ñ"); }
	@Test public void test_0x00F1_to_0x1E63() throws Exception { verify( "ñ", "ṣ"); }
	@Test public void test_0x00EF_to_0x00F1() throws Exception { verify( "ï", "ñ"); }
	@Test public void test_0x00C4_to_0x0100() throws Exception { verify( "Ä", "Ā"); }
	@Test public void test_0x00E4_to_0x0101() throws Exception { verify( "ä", "ā"); }
	@Test public void test_0x00C9_to_0x012A() throws Exception { verify( "É", "Ī"); }
	@Test public void test_0x00E9_to_0x012B() throws Exception { verify( "é", "ī"); }
	@Test public void test_0x00C7_to_0x015A() throws Exception { verify( "Ç", "Ś"); }
	@Test public void test_0x00E7_to_0x015B() throws Exception { verify( "ç", "ś"); }
	@Test public void test_0x00DC_to_0x016A() throws Exception { verify( "Ü", "Ū"); }
	@Test public void test_0x00FC_to_0x016B() throws Exception { verify( "ü", "ū"); }
	@Test public void test_0x00D2_to_0x1E0C() throws Exception { verify( "Ò", "Ḍ"); }
	@Test public void test_0x00F2_to_0x1E0D() throws Exception { verify( "ò", "ḍ"); }
	@Test public void test_0x00D9_to_0x1E24() throws Exception { verify( "Ù", "Ḥ"); }
	@Test public void test_0x00F9_to_0x1E25() throws Exception { verify( "ù", "ḥ"); }
	@Test public void test_0x00DF_to_0x1E36() throws Exception { verify( "ß", "Ḷ"); }
	@Test public void test_0x00C0_to_0x1E40() throws Exception { verify( "À", "Ṁ"); }
	@Test public void test_0x00E0_to_0x1E41() throws Exception { verify( "à", "ṁ"); }
	@Test public void test_0x00CC_to_0x1E44() throws Exception { verify( "Ì", "Ṅ"); }
	@Test public void test_0x00EC_to_0x1E45() throws Exception { verify( "ì", "ṅ"); }
	@Test public void test_0x00CB_to_0x1E46() throws Exception { verify( "Ë", "Ṇ"); }
	@Test public void test_0x00EB_to_0x1E47() throws Exception { verify( "ë", "ṇ"); }
	@Test public void test_0x00C5_to_0x1E5A() throws Exception { verify( "Å", "Ṛ"); }
	@Test public void test_0x00E5_to_0x1E5B() throws Exception { verify( "å", "ṛ"); }
	@Test public void test_0x00C8_to_0x1E5C() throws Exception { verify( "È", "Ṝ"); }
	@Test public void test_0x00E8_to_0x1E5D() throws Exception { verify( "è", "ṝ"); }
	@Test public void test_0x00D6_to_0x1E6C() throws Exception { verify( "Ö", "Ṭ"); }
	@Test public void test_0x00F6_to_0x1E6D() throws Exception { verify( "ö", "ṭ"); }
	@Test public void test_0x00D7_to_0x030D() throws Exception { verify( "×", "̍");  } // Vedik accent '
	@Test public void test_0x00DE_to_0x0331() throws Exception { verify( "Þ", "̱");  } // Vedik accent _
	@Test public void test_0x00F7_to_0x030E() throws Exception { verify( "÷", "̎");  } // Vedik accent ''
	// @formatter:on
	
	// ----------- NON-CONVERSION TESTS -------------
	
	//
	@Test
	public void testNonconversion() throws Exception {
		String input = "fiancé";
		String expected = input;
		verify(input, expected);
	}
	
	// ----------- ESCAPE CHARACTERS CONVERSION TESTS -------------
	
	// <p class="justify3"><span class="bodytext"><a href="../Text/part0009.html#ch4">Q &amp; A</a></span></p>

	@Test
	public void testEscape1() throws Exception {
		String input = "Q &amp; A";
		String expected = input;
		verify(input, expected);
	}
	
	@Test
	public void testEscape2() throws Exception {
		String input = "<p class=\"justify3\"><span class=\"bodytext\"><a href=\"../Text/part0009.html#ch4\">Q &amp; A</a></span></p>";
		String expected = input;
		verifyHtml(input, expected);
	}
	
}
