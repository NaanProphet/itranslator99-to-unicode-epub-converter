package org.dontexist.kb.converter;

import junit.framework.Assert;

import org.apache.commons.lang3.StringEscapeUtils;
import org.dontexist.kb.converter.Sanskrit99ToUnicodeConverter;
import org.junit.Test;

public class Sanskrit99ToUnicodeConverterTest {

	private Sanskrit99ToUnicodeConverter converter = new Sanskrit99ToUnicodeConverter();

	@Test
	public void testConvert1() throws Exception {
		String input = "nih&lt; AnIit nih&lt; kDu àÉuta$, sunhu krhu jae tuMhih saeha$.";
		String expected = "नहिं अनीति नहिं कछु प्रभुताई। सुनहु करहु जो तुम्हहि सोहाई॥";
		verify(input, expected);
	}

	@Test
	public void testConvert2() throws Exception {
		String input = "jgit àaßuyaTSwangaErv&lt;";
		String expected = "जगति प्राप्नुयात्स्थानगौरवं";
		verify(input, expected);
	}

	@Test
	public void testConvert3_kRiShNa() throws Exception {
		String input = "k«:[";
		String expected = "कृष्ण";
		verify(input, expected);
	}

	@Test
	public void testConvert1_kRIShNa() throws Exception {
		String input = "k©:["; // using char point 169 for combining, not 168 or
								// 176
		String expected = "कॄष्ण";
		verify(input, expected);
	}

	@Test
	public void testConvert2_kRIShNa() throws Exception {
		String input = "k¨:["; // using char point 168 for combining R^I
		String expected = "कॄष्ण";
		verify(input, expected);
	}

	@Test
	public void testConvert3_kRIShNa() throws Exception {
		String input = "k°:["; // using char point 176 for combining R^I
		String expected = "कॄष्ण";
		verify(input, expected);
	}

	// ----------- CHARACTER CONVERSION TESTS -------------

	public void verify(final Character sanskrit99InputChar, final String expectedUnescaped) throws Exception {
		String actual = converter.convert(sanskrit99InputChar.toString());
		String expected = StringEscapeUtils.escapeHtml4(expectedUnescaped);
		Assert.assertEquals(expected, actual);
	}

	public void verify(final String sanskrit99InputString, final String expectedUnescaped) throws Exception {
		String actual = converter.convert(sanskrit99InputString);
		String expected = StringEscapeUtils.escapeHtml4(expectedUnescaped);
		Assert.assertEquals(expected, actual);
	}

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
		verify((char) 249, "हृ"); // hRR^i
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

	// -------- Manually added ------------

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
	public void testConvert_183() throws Exception {
		verify((char) 183, "·"); // centered period (aka middle dot). same in
									// both. note: will be escaped to HTML4
									// escape sequence &middot; by
									// conversion
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
		verify((char) 176, "ॄ"); // vocalic R^I confirmed (according to Akshara
									// Bridge)
	}

	@Test
	public void testConvert_173() throws Exception {
		verify((char) 173, "­"); // soft hyphen. same in both. will be escaped
									// to HTML4 escape sequence &shy; by
									// conversion
	}

	@Test
	public void testConvert_161() throws Exception {
		// inverted candrabindu. note, previously, the converter would output
		// "Rं", i.e. R with anusvara (0x52 0x0902) to give the *appearance* of
		// an inverted candrabindu (note the position of the "R" is then further
		// changed). should this be the official inverted candrabindu (0x0900)
		// instead, even though it doesn't read as well? note: even Akshara
		// bridge will (IMO) incorrectly convert the Sanskrit99 text "kk¡xu" to
		// "karka.ndhu" i.e. adding the r in

		// if to leave as is (readable, but incorrect)
		 verify((char) 161, "Rं"); // special preconversion (Sanskrit99 --> Sanskrit99)
		 verify("kk¡xu", "कर्कंधु"); // karka.ndhu

		// if changed (see line ~ 110 of sanskrit99_to_unicode.js)
//		verify((char) 161, "ऀ"); // inverted candrabindu
//		verify("kk¡xu", "ककऀधु"); // kakaऀdhu
	}

	@Test
	public void testConvert_105() throws Exception {
		verify((char) 105, "ि"); // short combining i. already covered
	}

	@Test
	public void testConvert_82() throws Exception {
		verify((char) 82, "R"); // this is the combining R. the logic will not
								// replace by itself, but only when next to
								// letters b/c order needs to be swapped
		verify("kmR", "कर्म"); // example word with R
	}

}
