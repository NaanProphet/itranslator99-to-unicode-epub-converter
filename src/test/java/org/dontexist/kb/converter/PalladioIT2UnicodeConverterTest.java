package org.dontexist.kb.converter;

import junit.framework.Assert;

import org.dontexist.kb.converter.PalladioIT2UnicodeConverter;
import org.junit.Test;

public class PalladioIT2UnicodeConverterTest {

	private PalladioIT2UnicodeConverter converter = new PalladioIT2UnicodeConverter();

	// ----------- GENERAL CONVERSION TESTS -------------
	
	@Test
	public void testConvert1() throws Exception {
		String input = "vivähadinamidaà bhavatu harñadam,";
		String actual = converter.convert(input);
		String expected = "vivāhadinamidaṁ bhavatu harṣadam,";
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testConvert2() throws Exception {
		String input = "kasyacidapi sambandhasya-ädhäro yadi laukikas-tarhi sa bandhana-käraka, ädhyätmikastu mukti-däyako bhavati. – Jivansüträëi 5.5";
		String actual = converter.convert(input);
		String expected = "kasyacidapi sambandhasya-ādhāro yadi laukikas-tarhi sa bandhana-kāraka, ādhyātmikastu mukti-dāyako bhavati. &ndash; Jivansūtrāṇi 5.5";
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testConvert3() throws Exception {
		String input = "<div class=\"italictext2\">\n    <sup class=\"calibre6\"><a href=\"../Text/part0007.html#ch2_n1\" id=\"ch2_no1\">1</a></sup>&nbsp; kasyacidapi sambandhasya-ädhäro yadi laukikas-tarhi sa bandhana-käraka, ädhyätmikastu mukti-däyako bhavati. – Jivansüträëi 5.5\n  </div>";
		String actual = converter.convertHtmlBlock(input);
		String expected = "<div class=\"italictext2\">\n    <sup class=\"calibre6\"><a href=\"../Text/part0007.html#ch2_n1\" id=\"ch2_no1\">1</a></sup>&nbsp; kasyacidapi sambandhasya-ādhāro yadi laukikas-tarhi sa bandhana-kāraka, ādhyātmikastu mukti-dāyako bhavati. &ndash; Jivansūtrāṇi 5.5\n  </div>";
		Assert.assertEquals(expected, actual);
	}
	
	// ----------- CHARACTER CONVERSION TESTS -------------
	
	public void verify(final Character palladioInputChar, final String expected) throws Exception {
		String actual = converter.convert(palladioInputChar.toString());
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testConvert_255() throws Exception {
		verify((char) 255, "ह्र"); // hra
	}

}
