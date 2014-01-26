package org.dontexist.kb.converter;

import junit.framework.Assert;

import org.apache.commons.lang3.StringEscapeUtils;
import org.dontexist.kb.converter.PalladioIT2UnicodeConverter;
import org.junit.Test;

public class PalladioIT2UnicodeConverterTest {

	private PalladioIT2UnicodeConverter converter = new PalladioIT2UnicodeConverter();

	// ----------- HELPER METHODS --------------------

	private void verify(final Character palladioInputChar, final String expectedUnescaped) throws Exception {
		String actual = converter.convert(palladioInputChar.toString());
		String expected = StringEscapeUtils.unescapeXml(expectedUnescaped);
		Assert.assertEquals(expected, actual);
	}

	private void verify(final String palladioInputString, final String expectedUnescaped) throws Exception {
		String actual = converter.convert(palladioInputString);
		String expected = StringEscapeUtils.unescapeXml(expectedUnescaped);
		Assert.assertEquals(expected, actual);
	}
	
	private void verifyHtml(final String palladioInputString, final String expectedUnescaped) throws Exception {
		String actual = converter.convertHtmlBlock(palladioInputString);
		String expected = StringEscapeUtils.unescapeXml(expectedUnescaped);
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
		// note: ndash will not be escaped (b/c content is XML not actual HTML)
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

	@Test
	public void testConvert5() throws Exception {
		String input = "Q &amp; A";
		String expected = "Q &amp; A";
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


}
