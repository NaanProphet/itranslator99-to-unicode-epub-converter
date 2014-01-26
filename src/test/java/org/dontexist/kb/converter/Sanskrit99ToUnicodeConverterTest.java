package org.dontexist.kb.converter;

import junit.framework.Assert;

import org.junit.Test;

public class Sanskrit99ToUnicodeConverterTest {

	private Sanskrit99ToUnicodeConverter converter = new Sanskrit99ToUnicodeConverter();
	
	// ----------- HELPER METHODS --------------------

	private void verify(final Character sanskrit99InputChar, final String expected) throws Exception {
		String actual = converter.convert(sanskrit99InputChar.toString());
		Assert.assertEquals(expected, actual);
	}

	private void verify(final String sanskrit99InputString, final String expected) throws Exception {
		String actual = converter.convert(sanskrit99InputString);
		Assert.assertEquals(expected, actual);
	}
	
	private void verifyHtml(final String sanskrit99InputString, final String expected) throws Exception {
		String actual = converter.convertHtmlBlock(sanskrit99InputString);
		Assert.assertEquals(expected, actual);
	}

	// ----------- WORD CONVERSION TESTS -------------

	@Test
	public void testConvert_1() throws Exception {
		// test of literal "<" is used here rather than "&lt;"
		String input = "nih< AnIit nih< kDu àÉuta$, sunhu krhu jae tuMhih saeha$.";
		String expected = "नहिं अनीति नहिं कछु प्रभुताई। सुनहु करहु जो तुम्हहि सोहाई॥";
		verify(input, expected);
	}

	@Test
	public void testConvert2() throws Exception {
		// literal "<" is used here rather than "&lt;"
		String input = "jgit àaßuyaTSwangaErv<";
		String expected = "जगति प्राप्नुयात्स्थानगौरवं";
		verify(input, expected);
	}
	
	@Test
	public void testConvert_1_Negative() throws Exception {
		// "&lt;" is incorrectly used here without calling convertHtmlBlock
		String input = "nih< AnIit nih&lt; kDu àÉuta$, sunhu krhu jae tuMhih saeha$.";
		String expected = "नहिं अनीति नहिृलतष कछु प्रभुताई। सुनहु करहु जो तुम्हहि सोहाई॥";
		verify(input, expected);
	}

	@Test
	public void testConvert2_Negative() throws Exception {
		// "&lt;" is incorrectly used here without calling convertHtmlBlock
		String input = "jgit àaßuyaTSwangaErv&lt;";
		String expected = "जगति प्राप्नुयात्स्थानगौरवृलतष";
		verify(input, expected);
	}
	
	@Test
	public void testConvertHtmlBlock1() throws Exception {
		String input = "<div>nih&lt; AnIit nih&lt; kDu àÉuta$, sunhu krhu jae tuMhih saeha$.</div>";
		String expected = "<div>नहिं अनीति नहिं कछु प्रभुताई। सुनहु करहु जो तुम्हहि सोहाई॥</div>";
		verifyHtml(input, expected);
	}

	@Test
	public void testConvertHtmlBlock2() throws Exception {
		String input = "<div>jgit àaßuyaTSwangaErv&lt;</div>";
		String expected = "<div>जगति प्राप्नुयात्स्थानगौरवं</div>";
		verifyHtml(input, expected);
	}

	@Test
	public void testConvert3_kRiShNa() throws Exception {
		String input = "k«:[";
		String expected = "कृष्ण";
		verify(input, expected);
	}

	// ----------- CHARACTER CONVERSION TESTS -------------

	@Test
	public void testConvert_255() throws Exception {
		verify((char) 255, "ह्र"); // hra
	}

	@Test
	public void testConvert_254() throws Exception {
		verify((char) 254, "ह्व"); // hva
	}

	@Test
	public void testConvert_253() throws Exception {
		verify((char) 253, "ह्य"); // hya
	}

	@Test
	public void testConvert_252() throws Exception {
		verify((char) 252, "ह्म"); // hma
	}

	@Test
	public void testConvert_251() throws Exception {
		verify((char) 251, "ह्न"); // hna
	}

	@Test
	public void testConvert_250() throws Exception {
		verify((char) 250, "क्ष्"); // kSh (i.e. kSha + virAma)
	}

	@Test
	public void testConvert_249() throws Exception {
		verify((char) 249, "हृ"); // hR^i
	}

	@Test
	public void testConvert_248() throws Exception {
		verify((char) 248, "हू"); // hU
	}

	@Test
	public void testConvert_247() throws Exception {
		verify((char) 247, "हु"); // hu
	}

	@Test
	public void testConvert_246() throws Exception {
		verify((char) 246, "स्र"); // sra
	}

	@Test
	public void testConvert_245() throws Exception {
		verify((char) 245, "स्न"); // sna
	}

	@Test
	public void testConvert_244() throws Exception {
		verify((char) 244, "स्त्र"); // stra
	}

	@Test
	public void testConvert_243() throws Exception {
		verify((char) 243, "ष्ठ"); // ShTha
	}

	@Test
	public void testConvert_242() throws Exception {
		verify((char) 242, "ष्ट"); // ShTa
	}

	@Test
	public void testConvert_241() throws Exception {
		verify((char) 241, "श्व"); // zva
	}

	@Test
	public void testConvert_240() throws Exception {
		verify((char) 240, "श्ल"); // zla
	}

	@Test
	public void testConvert_239() throws Exception {
		verify((char) 239, "श्र"); // zri
	}

	@Test
	public void testConvert_183() throws Exception {
		verify((char) 183, "·"); // centered period (aka middle dot). same in
									// both. note: will be escaped to HTML4
									// escape sequence &middot; by
									// conversion
	}

	@Test
	public void testConvert_173() throws Exception {
		verify((char) 173, "­"); // soft hyphen. same in both. will be escaped
									// to HTML4 escape sequence &shy; by
									// conversion
	}

	@Test
	public void testConvert_169() throws Exception {
		verify((char) 169, "ॄ"); // vocalic R^I normal position (e.g. used with
									// k)
		verify("k©:[", "कॄष्ण"); // kR^IShNa

	}

	@Test
	public void testConvert_168() throws Exception {
		verify((char) 168, "ॄ"); // vocalic R^I offset right position (e.g. used
									// with t)
		verify("t¨[", "तॄण"); // tR^INa
		verify("Ã¨", "ञ्जॄ");// ~njR^I
	}

	@Test
	public void testConvert_161() throws Exception {
		verify((char) 161, "Rं"); // note: this is NOT an inverted candrabindu;
									// it is instead a special ligature for R
									// and anusvara. Because of the way
									// sanskrit99 renders, this "character" must
									// be placed at the end of the consonant
									// character (unlike Unicode fonts which can
									// dynamically create the ligature) and thus
									// requires special pre-conversion mapping
									// (from Sanskrit99 to Sanskrit99)
		verify("kk¡xu", "कर्कंधु"); // karka.ndhu
	}

	@Test
	public void testConvert_105() throws Exception {
		verify((char) 105, "ि"); // short combining i
		verify("ik", "कि"); // test letter reordering
	}
	
	@Test
	public void testConvert_82() throws Exception {
		verify((char) 82, "R"); // combining r. the logic will not
								// replace by itself, but only when next to
								// letters b/c order needs to be swapped
		verify("kmR", "कर्म"); // example word with R
	}

	// -------- MANUALLY ADDED CONVERSION ------------

	@Test
	public void testConvert_235() throws Exception {
		verify((char) 235, "व्न"); // vna
	}

	@Test
	public void testConvert_229() throws Exception {
		verify((char) 229, "भ्न"); // bhna
	}

	@Test
	public void testConvert_227() throws Exception {
		verify((char) 227, "ब्न"); // bna
	}

	@Test
	public void testConvert_226() throws Exception {
		verify((char) 226, "ब्ज"); // bja
	}

	@Test
	public void testConvert_222() throws Exception {
		verify((char) 222, "प्त्"); // pt (i.e. pa + virAma + ta + virAma)
	}

	@Test
	public void testConvert_219() throws Exception {
		verify((char) 219, "न्न्"); // nn (i.e. na + viRama + na + virAma)
	}

	@Test
	public void testConvert_215() throws Exception {
		verify((char) 215, "ध्न"); // dhna
	}

	@Test
	public void testConvert_210() throws Exception {
		verify((char) 210, "द्म"); // dma
	}

	@Test
	public void testConvert_208() throws Exception {
		verify((char) 208, "द्न"); // dna
	}

	@Test
	public void testConvert_205() throws Exception {
		verify((char) 205, "द्ग्र"); // dgra
	}

	@Test
	public void testConvert_178() throws Exception {
		verify((char) 178, "क्न"); // kna
	}

	@Test
	public void testConvert_177() throws Exception {
		verify((char) 177, "न्क्र"); // TODO confirm nkra
	}

	@Test
	public void testConvert_176() throws Exception {
		verify((char) 176, "ॄ"); // vocalic R^I offset down (used e.g. with
									// ~NkR^I)
		verify("»°", "ङ्कॄ"); // ~NkR^I

	}

}
