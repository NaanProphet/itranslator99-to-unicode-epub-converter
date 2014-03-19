package org.dontexist.kb.service;

import junit.framework.Assert;

import org.dontexist.kb.service.JavascriptSanskrit99ToUnicodeServiceImpl;
import org.junit.Ignore;
import org.junit.Test;

public class JavascriptSanskrit99ToUnicodeServiceImplTest {

	private JavascriptSanskrit99ToUnicodeServiceImpl converter = new JavascriptSanskrit99ToUnicodeServiceImpl();

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
	//
	public void testConvertHtmlBlock3() throws Exception {
		String input = "<p class=\"justify1\"><span class=\"santext\">ivvahidnimd&lt; Évtu h;Rdm!,<br class=\"calibre4\"/>m¼l&lt; twa va&lt; c ]emdm!.1.</span></p>";
		String expected = "<p class=\"justify1\"><span class=\"santext\">विवाहदिनमिदं भवतु हर्षदम्।<br class=\"calibre4\"/>मङ्गलं तथा वां च क्षेमदम्॥१॥</span></p>";
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
	public void testConvert_238() throws Exception {
		verify((char) 238, "श्न"); // shna
	}

	@Test
	public void testConvert_237() throws Exception {
		verify((char) 237, "श्च"); // shcha
	}

	@Test
	public void testConvert_236() throws Exception {
		verify((char) 236, "व्र"); // vra
	}

	@Test
	public void testConvert_235() throws Exception {
		// MANUALLY ADDED CONVERSION
		verify((char) 235, "व्न"); // vna
	}

	@Test
	public void testConvert_234() throws Exception {
		verify((char) 234, "रू"); // rU
	}

	@Test
	public void testConvert_233() throws Exception {
		verify((char) 233, "रु"); // ru
	}

	@Test
	public void testConvert_232() throws Exception {
		verify((char) 232, "म्र"); // mra
	}

	@Test
	public void testConvert_231() throws Exception {
		verify((char) 231, "म्न"); // mna
	}

	@Test
	public void testConvert_230() throws Exception {
		verify((char) 230, "भ्र"); // bhra
	}

	@Test
	public void testConvert_229() throws Exception {
		// MANUALLY-ADDED CONVERSION
		verify((char) 229, "भ्न"); // bhna
	}

	@Test
	public void testConvert_228() throws Exception {
		verify((char) 228, "ब्र"); // bra
	}

	@Test
	public void testConvert_227() throws Exception {
		// MANUALLY-ADDED CONVERSION
		verify((char) 227, "ब्न"); // bna
	}

	@Test
	public void testConvert_226() throws Exception {
		// MANUALLY-ADDED CONVERSION
		verify((char) 226, "ब्ज"); // bja
	}

	@Test
	public void testConvert_225() throws Exception {
		// HALF-GLYPH
		verify((char) 225, "फ्"); // ph (pha + virama)
	}

	@Test
	public void testConvert_224() throws Exception {
		verify((char) 224, "प्र"); // pra
	}

	@Test
	public void testConvert_223() throws Exception {
		verify((char) 223, "प्न"); // pna
	}

	@Test
	public void testConvert_222() throws Exception {
		// HALF-GLYPH
		// MANUALLY-ADDED CONVERSION
		verify((char) 222, "प्त्"); // pt (i.e. pa + virAma + ta + virAma)
	}

	@Test
	public void testConvert_221() throws Exception {
		verify((char) 221, "प्त"); // pta
	}

	@Test
	public void testConvert_220() throws Exception {
		verify((char) 220, "न्र"); // nra
	}

	@Test
	public void testConvert_219() throws Exception {
		// HALF-GLYPH
		// MANUALLY-ADDED CONVERSION
		verify((char) 219, "न्न्"); // nn (i.e. na + viRama + na + virAma)
	}

	@Test
	public void testConvert_218() throws Exception {
		verify((char) 218, "न्न"); // nna
	}

	@Test
	public void testConvert_217() throws Exception {
		verify((char) 217, "न्त्र"); // ntra
	}

	@Test
	public void testConvert_216() throws Exception {
		verify((char) 216, "ध्र"); // dhra
	}

	@Test
	public void testConvert_215() throws Exception {
		// MANUALLY-ADDED CONVERSION
		verify((char) 215, "ध्न"); // dhna
	}

	@Test
	public void testConvert_214() throws Exception {
		verify((char) 214, "द्व"); // dva
	}

	@Test
	public void testConvert_213() throws Exception {
		verify((char) 213, "द्र्य"); // drya
	}

	@Test
	public void testConvert_212() throws Exception {
		verify((char) 212, "द्र"); // dra
	}

	@Test
	public void testConvert_211() throws Exception {
		verify((char) 211, "द्ब"); // dba
	}

	@Test
	public void testConvert_210() throws Exception {
		// MANUALLY-ADDED CONVERSION
		verify((char) 210, "द्म"); // dma
	}

	@Test
	public void testConvert_209() throws Exception {
		verify((char) 209, "द्भ"); // dbha
	}

	@Test
	public void testConvert_208() throws Exception {
		// MANUALLY-ADDED CONVERSION
		verify((char) 208, "द्न"); // dna
	}

	@Test
	public void testConvert_207() throws Exception {
		verify((char) 207, "द्ध"); // ddha
	}

	@Test
	public void testConvert_206() throws Exception {
		verify((char) 206, "द्द"); // dda
	}

	@Test
	public void testConvert_205() throws Exception {
		// MANUALLY-ADDED CONVERSION
		verify((char) 205, "द्ग्र"); // dgra
	}

	@Test
	public void testConvert_204() throws Exception {
		verify((char) 204, "द्ग"); // dga
	}

	@Test
	public void testConvert_203() throws Exception {
		verify((char) 203, "दू"); // dU (requires special placement)
	}

	@Test
	public void testConvert_202() throws Exception {
		verify((char) 202, "दु"); // du (requires special placement)
	}

	@Test
	public void testConvert_201() throws Exception {
		verify((char) 201, "भ"); // bha
	}

	@Test
	public void testConvert_200() throws Exception {
		// HALF-GLYPH
		verify((char) 200, "त्र्"); // tr (ta + virama + ra + virama)
		verify("Èm", "त्र्म"); // trma
	}

	@Test
	public void testConvert_199() throws Exception {
		verify((char) 199, "त्र"); // tra
	}

	@Test
	public void testConvert_198() throws Exception {
		verify((char) 198, "त्न"); // tna
	}

	@Test
	public void testConvert_197() throws Exception {
		// HALF-GLYPH
		verify((char) 197, "त्त्"); // tt (ta + virama + ta + virama)
	}

	@Test
	public void testConvert_196() throws Exception {
		verify((char) 196, "त्त"); // tta
	}

	@Test
	public void testConvert_195() throws Exception {
		verify((char) 195, "ञ्ज"); // ~nja
	}

	@Test
	public void testConvert_194() throws Exception {
		verify((char) 194, "ञ्च"); // ~nca
	}

	@Test
	public void testConvert_193() throws Exception {
		// HALF-GLYPH
		verify((char) 193, "ञ्"); // ~n combining (i.e. ~na + virama)
	}

	@Test
	public void testConvert_192() throws Exception {
		verify((char) 192, "छ्र"); // chhra
	}

	@Test
	public void testConvert_191() throws Exception {
		verify((char) 191, "ज्र"); // jra
	}

	@Test
	public void testConvert_190() throws Exception {
		verify((char) 190, "ज्ज"); // jja
	}

	@Test
	public void testConvert_189() throws Exception {
		verify((char) 189, "च्च"); // cca
	}

	@Test
	public void testConvert_188() throws Exception {
		verify((char) 188, "ङ्ग"); // ~Nga
	}

	@Test
	public void testConvert_187() throws Exception {
		verify((char) 187, "ङ्क"); // ~Nka
	}

	@Test
	public void testConvert_186() throws Exception {
		verify((char) 186, "घ्र"); // ghra
	}

	@Test
	public void testConvert_185() throws Exception {
		verify((char) 185, "घ्न"); // ghna
	}

	@Test
	public void testConvert_184() throws Exception {
		// HALF-GLYPH
		// MANUALLY-CORRECTED CONVERSION
		verify((char) 184, "घ्"); // gh (gha + virama)
		verify("¸m", "घ्म"); // ghma
	}

	@Test
	public void testConvert_183() throws Exception {
		// PASS THRU
		verify((char) 183, "·"); // centered period (aka middle dot). same in
									// both. note: will be escaped to HTML4
									// escape sequence &middot; by
									// conversion
	}

	@Test
	public void testConvert_182() throws Exception {
		verify((char) 182, "ग्न"); // gna
	}

	@Test
	public void testConvert_181() throws Exception {
		verify((char) 181, "ख्र"); // khra
	}

	@Test
	public void testConvert_180() throws Exception {
		verify((char) 180, "क्त"); // kta
	}

	@Test
	public void testConvert_179() throws Exception {
		verify((char) 179, "क्र"); // kra
	}

	@Test
	public void testConvert_178() throws Exception {
		// MANUALLY-ADDED CONVERSION
		verify((char) 178, "क्न"); // kna
	}

	@Test
	public void testConvert_177() throws Exception {
		// MANUALLY-ADDED CONVERSION
		verify((char) 177, "न्क्र"); // TODO confirm nkra
	}

	@Test
	public void testConvert_176() throws Exception {
		// MANUALLY-ADDED CONVERSION
		verify((char) 176, "ॄ"); // vocalic R^I offset down (used e.g. with
									// ~NkR^I)
		verify("»°", "ङ्कॄ"); // ~NkR^I
	}

	@Test
	public void testConvert_175() throws Exception {
		verify((char) 175, "ृ"); // R^i combining
	}

	@Test
	public void testConvert_174() throws Exception {
		// HALF-GLYPH
		verify((char) 174, "क्त्"); // kta (i.e. ka + virama + ta + virama) used
									// e.g. in ktla
		verify("É®l", "भक्त्ल"); // bhaktla (might not be a real word)
	}

	@Test
	public void testConvert_173() throws Exception {
		// PASS THRU
		verify((char) 173, "­"); // soft hyphen. same in both. will be escaped
									// to HTML4 escape sequence &shy; by
									// conversion
	}

	@Test
	public void testConvert_172() throws Exception {
		verify((char) 172, "क्ल"); // kla
	}

	@Test
	public void testConvert_171() throws Exception {
		// ZERO-WIDTH
		verify((char) 171, "ृ"); // R^i combining
	}

	@Test
	public void testConvert_170() throws Exception {
		// ZERO-WIDTH
		verify((char) 170, "ू"); // U combining
	}

	@Test
	public void testConvert_169() throws Exception {
		// ZERO-WIDTH
		verify((char) 169, "ॄ"); // combining R^I normal position (e.g. used
									// with
									// k)
		verify("k©:[", "कॄष्ण"); // kR^IShNa

	}

	@Test
	public void testConvert_168() throws Exception {
		// ZERO-WIDTH
		verify((char) 168, "ॄ"); // combining R^I offset right position (e.g.
									// used
									// with t)
		verify("t¨[", "तॄण"); // tR^INa
		verify("Ã¨", "ञ्जॄ");// ~njR^I
	}

	@Test
	public void testConvert_167() throws Exception {
		verify((char) 167, "ॠ"); // RR^I
	}

	@Test
	public void testConvert_166() throws Exception {
		verify((char) 166, "ळ"); // LLa
	}

	@Test
	public void testConvert_165() throws Exception {
		verify((char) 165, "ॡ"); // LL^I
	}

	@Test
	public void testConvert_164() throws Exception {
		verify((char) 164, "ऌ"); // LL^i
	}

	@Test
	public void testConvert_163() throws Exception {
		// ZERO-WIDTH
		verify((char) 163, "ॅ"); // ardhacandra for Hindi only
	}

	@Test
	public void testConvert_162() throws Exception {
		verify((char) 162, "ग्र"); // gra
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
	public void testConvert_160() throws Exception {
		// PASS THRU
		verify((char) 160, " "); // no break space
	}

	// ---------- CONTROL CHARACTERS START TODO cleanup? ---------------

	@Test
	public void testConvert_159() throws Exception {
		verify((char) 159, "ह्ण"); // hNa (Akshara Bridge 376, because of Western - Windows encoding)
	}

	@Test
	@Ignore
	public void testConvert_156() throws Exception {
		verify((char) 156, "");
	}

	@Test
	@Ignore
	public void testConvert_155() throws Exception {
		verify((char) 155, "");
	}

	@Test
	@Ignore
	public void testConvert_154() throws Exception {
		verify((char) 154, "");
	}

	@Test
	@Ignore
	public void testConvert_153() throws Exception {
		verify((char) 153, "");
	}

	@Test
	@Ignore
	public void testConvert_152() throws Exception {
		verify((char) 152, "");
	}

	@Test
	@Ignore
	public void testConvert_151() throws Exception {
		verify((char) 151, "");
	}

	@Test
	@Ignore
	public void testConvert_150() throws Exception {
		verify((char) 150, "");
	}

	@Test
	@Ignore
	public void testConvert_149() throws Exception {
		verify((char) 149, "");
	}

	@Test
	@Ignore
	public void testConvert_148() throws Exception {
		verify((char) 148, "");
	}

	@Test
	@Ignore
	public void testConvert_147() throws Exception {
		verify((char) 147, "");
	}

	@Test
	@Ignore
	public void testConvert_146() throws Exception {
		verify((char) 146, "");
	}

	@Test
	@Ignore
	public void testConvert_145() throws Exception {
		verify((char) 145, "");
	}

	@Test
	@Ignore
	public void testConvert_140() throws Exception {
		verify((char) 140, "");
	}

	@Test
	@Ignore
	public void testConvert_139() throws Exception {
		verify((char) 139, "");
	}

	@Test
	@Ignore
	public void testConvert_138() throws Exception {
		verify((char) 138, "");
	}

	@Test
	@Ignore
	public void testConvert_137() throws Exception {
		verify((char) 137, "");
	}

	@Test
	@Ignore
	public void testConvert_136() throws Exception {
		verify((char) 136, "");
	}

	@Test
	@Ignore
	public void testConvert_135() throws Exception {
		verify((char) 135, "");
	}

	@Test
	@Ignore
	public void testConvert_134() throws Exception {
		verify((char) 134, "");
	}

	@Test
	@Ignore
	public void testConvert_133() throws Exception {
		verify((char) 133, "");
	}

	@Test
	@Ignore
	public void testConvert_132() throws Exception {
		verify((char) 132, ""); 
	}

	@Test
	@Ignore
	public void testConvert_131() throws Exception {
		// ZERO-WIDTH
		verify((char) 131, ""); // u combining
	}

	@Test
	@Ignore
	public void testConvert_130() throws Exception {
		// ZERO-WIDTH
		verify((char) 130, ""); // U combining
	}

	@Test
	public void testConvert_129() throws Exception {
		// UNUSED
		verify((char) 129, new Character((char) 129).toString());
	}

	@Test
	public void testConvert_128() throws Exception {
		// UNUSED
		verify((char) 128, new Character((char) 128).toString());
	}

	@Test
	public void testConvert_127() throws Exception {
		// UNUSED
		verify((char) 127, new Character((char) 127).toString());
	}
	
	// ---------- CONTROL CHARACTERS END --------------------------

	@Test
	public void testConvert_126() throws Exception {
		// ZERO-WIDTH
		verify((char) 126, "ँ"); // candrabindu
	}

	@Test
	public void testConvert_125() throws Exception {
		verify((char) 125, "ज्ञ"); // j~na
	}

	@Test
	public void testConvert_124() throws Exception {
		verify((char) 124, "ञ"); // ~na
	}

	@Test
	public void testConvert_123() throws Exception {
		// HALF-GLYPH
		verify((char) 123, "ण्"); // N (Na + virama)
	}

	@Test
	public void testConvert_122() throws Exception {
		verify((char) 122, "श"); // sha
	}

	@Test
	public void testConvert_121() throws Exception {
		verify((char) 121, "य"); // ya
	}

	@Test
	public void testConvert_120() throws Exception {
		verify((char) 120, "ध"); // dha
	}

	@Test
	public void testConvert_119() throws Exception {
		verify((char) 119, "थ"); // tha
	}

	@Test
	public void testConvert_118() throws Exception {
		verify((char) 118, "व"); // va
	}

	@Test
	public void testConvert_117() throws Exception {
		// ZERO-WIDTH
		verify((char) 117, "ु"); // u combining
	}

	@Test
	public void testConvert_116() throws Exception {
		verify((char) 116, "त"); // ta
	}

	@Test
	public void testConvert_115() throws Exception {
		verify((char) 115, "स"); // sa
	}

	@Test
	public void testConvert_114() throws Exception {
		verify((char) 114, "र"); // ra
	}

	@Test
	public void testConvert_113() throws Exception {
		verify((char) 113, "ट"); // Ta
	}

	@Test
	public void testConvert_112() throws Exception {
		verify((char) 112, "प"); // pa
	}

	@Test
	public void testConvert_111() throws Exception {
		verify((char) 111, "ख"); // kha
	}

	@Test
	public void testConvert_110() throws Exception {
		verify((char) 110, "न"); // na
	}

	@Test
	public void testConvert_109() throws Exception {
		verify((char) 109, "म"); // ma
	}

	@Test
	public void testConvert_108() throws Exception {
		verify((char) 108, "ल"); // la
	}

	@Test
	public void testConvert_107() throws Exception {
		verify((char) 107, "क"); // ka
	}

	@Test
	public void testConvert_106() throws Exception {
		verify((char) 106, "ज"); // ja
	}

	@Test
	public void testConvert_105() throws Exception {
		verify((char) 105, "ि"); // short combining i
		verify("ik", "कि"); // test letter reordering
	}

	@Test
	public void testConvert_104() throws Exception {
		verify((char) 104, "ह"); // ha
	}

	@Test
	public void testConvert_103() throws Exception {
		verify((char) 103, "ग"); // ga
	}

	@Test
	public void testConvert_102() throws Exception {
		verify((char) 102, "ड"); // Da
	}

	@Test
	public void testConvert_101() throws Exception {
		// ZERO-WIDTH
		verify((char) 101, "े"); // e (combining)
	}

	@Test
	public void testConvert_100() throws Exception {
		verify((char) 100, "द"); // da
	}

	@Test
	public void testConvert_99() throws Exception {
		verify((char) 99, "च"); // cha
	}

	@Test
	public void testConvert_98() throws Exception {
		verify((char) 98, "ब"); // ba
	}

	@Test
	public void testConvert_97() throws Exception {
		verify((char) 97, "ा"); // A (combining)
	}

	@Test
	public void testConvert_96() throws Exception {
		verify((char) 96, "ॐ"); // OM
	}

	@Test
	public void testConvert_95() throws Exception {
		// HALF-GLYPH
		verify((char) 95, "भ्"); // bh (bha + virama)
	}

	@Test
	public void testConvert_94() throws Exception {
		verify((char) 94, "ऊ"); // U (complete)
	}

	@Test
	public void testConvert_93() throws Exception {
		verify((char) 93, "क्ष"); // kSha
	}

	@Test
	public void testConvert_92() throws Exception {
		verify((char) 92, "ऋ"); // RR^i
	}

	@Test
	public void testConvert_91() throws Exception {
		verify((char) 91, "ण"); // Na
	}

	@Test
	public void testConvert_90() throws Exception {
		// HALF-GLYPH
		verify((char) 90, "श्"); // sh (sha + virama)
	}

	@Test
	public void testConvert_89() throws Exception {
		// HALF-GLYPH
		verify((char) 89, "य्"); // y (ya + virama)
	}

	@Test
	public void testConvert_88() throws Exception {
		// HALF-GLYPH
		verify((char) 88, "ध्"); // dh (dha + virama)
	}

	@Test
	public void testConvert_87() throws Exception {
		// HALF-GLYPH
		verify((char) 87, "थ्"); // th (tha + virama)
	}

	@Test
	public void testConvert_86() throws Exception {
		// HALF-GLYPH
		verify((char) 86, "व्"); // v (va + virama)
	}

	@Test
	public void testConvert_85() throws Exception {
		// ZERO-GLYPH
		verify((char) 85, "ू"); // U combining
	}

	@Test
	public void testConvert_84() throws Exception {
		// HALF-GLYPH
		verify((char) 84, "त्"); // t (ta + virama)
	}

	@Test
	public void testConvert_83() throws Exception {
		// HALF-GLYPH
		verify((char) 83, "स्"); // s (sa + virama)
	}

	@Test
	public void testConvert_82() throws Exception {
		// ZERO-WIDTH
		verify((char) 82, "R"); // combining r. the logic will not
								// replace by itself, but only when next to
								// letters b/c order needs to be swapped
		verify("kmR", "कर्म"); // example word with R
	}

	@Test
	public void testConvert_81() throws Exception {
		verify((char) 81, "ठ"); // Tha
	}

	@Test
	public void testConvert_80() throws Exception {
		// HALF-GLYPH
		verify((char) 80, "प्"); // p (pa + virama)
	}

	@Test
	public void testConvert_79() throws Exception {
		// HALF-GLYPH
		verify((char) 79, "ख्"); // kh (kha + virama)
	}

	@Test
	public void testConvert_78() throws Exception {
		// HALF-GLYPH
		verify((char) 78, "न्"); // n (na + virama)
	}

	@Test
	public void testConvert_77() throws Exception {
		// HALF-GLYPH
		verify((char) 77, "म्"); // m (ma + virama)
	}

	@Test
	public void testConvert_76() throws Exception {
		// HALF-GLYPH
		verify((char) 76, "ल्"); // l (la + virama)
	}

	@Test
	public void testConvert_75() throws Exception {
		// HALF-GLYPH
		verify((char) 75, "क्"); // k (ka + virama)
	}

	@Test
	public void testConvert_74() throws Exception {
		// HALF-GLYPH
		verify((char) 74, "ज्"); // j (ja + virama)
	}

	@Test
	public void testConvert_73() throws Exception {
		verify((char) 73, "ी"); // I (combining)
	}

	@Test
	public void testConvert_72() throws Exception {
		verify((char) 72, "झ"); // jha
	}

	@Test
	public void testConvert_71() throws Exception {
		// HALF-GLYPH
		verify((char) 71, "ग्"); // g (ga + virama)
	}

	@Test
	public void testConvert_70() throws Exception {
		verify((char) 70, "ढ"); // Dha
	}

	@Test
	public void testConvert_69() throws Exception {
		// ZERO-WIDTH
		verify((char) 69, "ै"); // ai conjunct
		verify("kE", "कै"); // kai
	}

	@Test
	public void testConvert_68() throws Exception {
		verify((char) 68, "छ"); // chha
	}

	@Test
	// HALF-GLYPH
	public void testConvert_67() throws Exception {
		verify((char) 67, "च्"); // c (i.e. ca + virama)
	}

	@Test
	public void testConvert_66() throws Exception {
		// HALF-GLYPH
		verify((char) 66, "ब्"); // b (i.e. ba + virama)
	}

	@Test
	public void testConvert_65() throws Exception {
		verify((char) 65, "अ"); // a (complete)
	}

	@Test
	public void testConvert_64() throws Exception {
		verify((char) 64, "ए"); // e (complete)
	}

	@Test
	public void testConvert_63() throws Exception {
		// ZERO-WIDTH
		verify((char) 63, "॑"); // vedik ' udatta
	}

	@Test
	public void testConvert_62() throws Exception {
		verify((char) 62, "ः"); // visarga
	}

	@Test
	public void testConvert_61() throws Exception {
		verify((char) 61, "ऽ"); // avagraha (i.e. apostrophe)
		verify("kae=hm!", "कोऽहम्"); // ko.aham
	}

	@Test
	public void testConvert_60() throws Exception {
		// ZERO-WIDTH
		verify((char) 60, "ं"); // anusvara
	}

	@Test
	public void testConvert_59() throws Exception {
		verify((char) 59, "ष"); // Sha
	}

	@Test
	public void testConvert_58() throws Exception {
		// HALF-GLYPH
		verify((char) 58, "ष्"); // Sha left-hand half ligature
	}

	@Test
	public void testConvert_57() throws Exception {
		verify((char) 57, "९"); // 9
	}

	@Test
	public void testConvert_56() throws Exception {
		verify((char) 56, "८"); // 8
	}

	@Test
	public void testConvert_55() throws Exception {
		verify((char) 55, "७"); // 7
	}

	@Test
	public void testConvert_54() throws Exception {
		verify((char) 54, "६"); // 6
	}

	@Test
	public void testConvert_53() throws Exception {
		verify((char) 53, "५"); // 5
	}

	@Test
	public void testConvert_52() throws Exception {
		verify((char) 52, "४"); // 4
	}

	@Test
	public void testConvert_51() throws Exception {
		verify((char) 51, "३"); // 3
	}

	@Test
	public void testConvert_50() throws Exception {
		verify((char) 50, "२"); // 2
	}

	@Test
	public void testConvert_49() throws Exception {
		verify((char) 49, "१"); // 1
	}

	@Test
	public void testConvert_48() throws Exception {
		verify((char) 48, "०"); // 0
	}

	@Test
	public void testConvert_47() throws Exception {
		// ZERO-WIDTH
		verify((char) 47, "॒"); // vedik _ anudātta accent
	}

	@Test
	public void testConvert_46() throws Exception {
		verify((char) 46, "॥"); // double danda
	}

	@Test
	public void testConvert_45() throws Exception {
		// PASS THRU
		verify((char) 45, "-"); // hyphen
	}

	@Test
	public void testConvert_44() throws Exception {
		verify((char) 44, "।"); // danda
	}

	@Test
	public void testConvert_43() throws Exception {
		// ZERO-WIDTH
		verify((char) 43, "र"); // ra matra used retroflex group
		verify("q+ Q+ f+ F+", "ट्र ठ्र ड्र ढ्र");
	}

	@Test
	public void testConvert_42() throws Exception {
		verify((char) 42, "द्य"); // dya
	}

	@Test
	public void testConvert_41() throws Exception {
		verify((char) 41, "फ"); // pha (complete)
	}

	@Test
	public void testConvert_40() throws Exception {
		// HALF-GLYPH
		verify((char) 40, "्य"); // ya right-hand half ligature (e.g. with bhrya
									// in iTranslator99)
	}

	@Test
	public void testConvert_39() throws Exception {
		verify((char) 39, "ङ"); // ~Na (complete)
	}

	@Test
	public void testConvert_38() throws Exception {
		// ZERO-WIDTH
		verify((char) 38, "ृ"); // R^i for stem to right (e.g. with gha not ka)
	}

	@Test
	public void testConvert_37() throws Exception {
		verify((char) 37, "उ"); // u (complete)
	}

	@Test
	public void testConvert_36() throws Exception {
		verify((char) 36, "ई"); // I (complete)
	}

	@Test
	public void testConvert_35() throws Exception {
		verify((char) 35, "इ"); // i (complete)
	}

	@Test
	public void testConvert_34() throws Exception {
		verify((char) 34, "घ"); // gha
	}

	@Test
	public void testConvert_33() throws Exception {
		// ZERO-WIDTH
		verify((char) 33, "्"); // virama
	}

	@Test
	public void testConvert_32() throws Exception {
		// PASS THRU
		verify((char) 32, " "); // space
	}
	
	// TODO add pass thru tests for 00 - 31 control characters

}
