package org.dontexist.kb.service.converter;

import static junit.framework.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.UnsupportedEncodingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:app-context.xml"})
public class JavascriptSanskrit99ToUnicodeServiceImplTest {

    @Autowired
    private JavascriptSanskrit99ToUnicodeServiceImpl converter;

    // ----------- HELPER METHODS --------------------

    private void verify(final Character sanskrit99InputChar, final Character expected) throws UnsupportedEncodingException {
        String actual = converter.convert(sanskrit99InputChar.toString());
        System.out.println(expected.toString().getBytes("UTF8"));
        System.out.println(actual.getBytes("UTF8"));
        assertEquals(expected, actual);
    }

    private void verify(final Character sanskrit99InputChar, final String expected) {
        String actual = converter.convert(sanskrit99InputChar.toString());
        assertEquals(expected, actual);
    }

    private void verify(final String sanskrit99InputString, final String expected) {
        String actual = converter.convert(sanskrit99InputString);
        assertEquals(expected, actual);
    }

    private void verifyHtml(final String sanskrit99InputString, final String expected) {
        String actual = converter.convertHtmlBlock(sanskrit99InputString);
        assertEquals(expected, actual);
    }

    /**
     * Convenience method for when character cannot be printed (e.g. control characters. Verifies character value passes through unchanged
     */
    private void verifyUnused(int decimalCharPoint) {
        verify((char) decimalCharPoint, new Character((char) decimalCharPoint).toString());
    }

    // ----------- WORD CONVERSION TESTS -------------

    @Test
    public void testConvert_1() {
        // test of literal "<" is used here rather than "&lt;"
        String input = "nih< AnIit nih< kDu àÉuta$, sunhu krhu jae tuMhih saeha$.";
        String expected = "नहिं अनीति नहिं कछु प्रभुताई। सुनहु करहु जो तुम्हहि सोहाई॥";
        verify(input, expected);
    }

    @Test
    public void testConvert2() {
        // literal "<" is used here rather than "&lt;"
        String input = "jgit àaßuyaTSwangaErv<";
        String expected = "जगति प्राप्नुयात्स्थानगौरवं";
        verify(input, expected);
    }

    @Test
    public void testConvert_1_Negative() {
        // "&lt;" is incorrectly used here without calling convertHtmlBlock
        String input = "nih< AnIit nih&lt; kDu àÉuta$, sunhu krhu jae tuMhih saeha$.";
        String expected = "नहिं अनीति नहिृलतष कछु प्रभुताई। सुनहु करहु जो तुम्हहि सोहाई॥";
        verify(input, expected);
    }

    @Test
    public void testConvert2_Negative() {
        // "&lt;" is incorrectly used here without calling convertHtmlBlock
        String input = "jgit àaßuyaTSwangaErv&lt;";
        String expected = "जगति प्राप्नुयात्स्थानगौरवृलतष";
        verify(input, expected);
    }

    @Test
    public void testConvertHtmlBlock1() {
        String input = "<div>nih&lt; AnIit nih&lt; kDu àÉuta$, sunhu krhu jae tuMhih saeha$.</div>";
        String expected = "<div>नहिं अनीति नहिं कछु प्रभुताई। सुनहु करहु जो तुम्हहि सोहाई॥</div>";
        verifyHtml(input, expected);
    }

    @Test
    public void testConvertHtmlBlock2() {
        String input = "<div>jgit àaßuyaTSwangaErv&lt;</div>";
        String expected = "<div>जगति प्राप्नुयात्स्थानगौरवं</div>";
        verifyHtml(input, expected);
    }

    @Test
    //
    public void testConvertHtmlBlock3() {
        String input = "<p class=\"justify1\"><span class=\"santext\">ivvahidnimd&lt; Évtu h;Rdm!,<br class=\"calibre4\"/>m¼l&lt; twa va&lt; c ]emdm!.1.</span></p>";
        String expected = "<p class=\"justify1\"><span class=\"santext\">विवाहदिनमिदं भवतु हर्षदम्।<br class=\"calibre4\"/>मङ्गलं तथा वां च क्षेमदम्॥१॥</span></p>";
        verifyHtml(input, expected);
    }

    @Test
    public void testConvert3_kRiShNa() {
        String input = "k«:[";
        String expected = "कृष्ण";
        verify(input, expected);
    }

    @Test
    public void testConvertHtmlBlock4_FromMrityunjaya() {
        String input = "<span class=\"sans\">` ÈyMbk&lt; yjamhe sugiNx&lt; puiòvxRnm!,</span>";
        String expected = "<span class=\"sans\">ॐ त्र्यम्बकं यजामहे सुगन्धिं पुष्टिवर्धनम्।</span>";
        verifyHtml(input, expected);

    }

    @Test
    public void testConvertHtmlBlock5_FromMrityunjaya() {
        // note: akshara bridge messes up converting the "yae" in bNxnaNm&amp;TyaemuR]Iy back to ITRANS but it's actually correct!
        String input = "<span class=\"sans\">` ÈyMbk&lt; yjamhe sugiNx&lt; puiòvxRnm!,<br class=\"line-height\" />  %vaRékimv bNxnaNm&amp;TyaemuR]Iy ma=m&amp;tat! .</span>";
        String expected = "<span class=\"sans\">ॐ त्र्यम्बकं यजामहे सुगन्धिं पुष्टिवर्धनम्।<br class=\"line-height\" />  उर्वारुकमिव बन्धनान्मृत्योर्मुक्षीय माऽमृतात् ॥</span>";
        verifyHtml(input, expected);

    }

    // ----------- CHARACTER CONVERSION TESTS -------------


    /**
     * <table>
     * <tr><td>itrans</td>
     * <td>unicode dec</td>
     * <td>unicode devanagari</td>
     * </tr>
     * <tr><td>hra</td><td>255</td><td>"ह्र"</td></tr>
     * </table>
     */
    @Test
    public void testConvert_255() {
        verify((char) 255, "ह्र"); // hra
    }

    @Test
    public void testConvert_254() {
        verify((char) 254, "ह्व"); // hva
    }

    @Test
    public void testConvert_253() {
        verify((char) 253, "ह्य"); // hya
    }

    @Test
    public void testConvert_252() {
        verify((char) 252, "ह्म"); // hma
    }

    @Test
    public void testConvert_251() {
        verify((char) 251, "ह्न"); // hna
    }

    @Test
    public void testConvert_250() {
        verify((char) 250, "क्ष्"); // kSh (i.e. kSha + virAma)
    }

    @Test
    public void testConvert_249() {
        verify((char) 249, "हृ"); // hR^i
    }

    @Test
    public void testConvert_248() {
        verify((char) 248, "हू"); // hU
    }

    @Test
    public void testConvert_247() {
        verify((char) 247, "हु"); // hu
    }

    @Test
    public void testConvert_246() {
        verify((char) 246, "स्र"); // sra
    }

    @Test
    public void testConvert_245() {
        verify((char) 245, "स्न"); // sna
    }

    @Test
    public void testConvert_244() {
        verify((char) 244, "स्त्र"); // stra
    }

    @Test
    public void testConvert_243() {
        verify((char) 243, "ष्ठ"); // ShTha
    }

    @Test
    public void testConvert_242() {
        verify((char) 242, "ष्ट"); // ShTa
    }

    @Test
    public void testConvert_241() {
        verify((char) 241, "श्व"); // zva
    }

    @Test
    public void testConvert_240() {
        verify((char) 240, "श्ल"); // zla
    }

    @Test
    public void testConvert_239() {
        verify((char) 239, "श्र"); // zri
    }

    @Test
    public void testConvert_238() {
        verify((char) 238, "श्न"); // shna
    }

    @Test
    public void testConvert_237() {
        verify((char) 237, "श्च"); // shcha
    }

    @Test
    public void testConvert_236() {
        verify((char) 236, "व्र"); // vra
    }

    @Test
    public void testConvert_235() {
        // MANUALLY ADDED CONVERSION
        verify((char) 235, "व्न"); // vna
    }

    @Test
    public void testConvert_234() {
        verify((char) 234, "रू"); // rU
    }

    @Test
    public void testConvert_233() {
        verify((char) 233, "रु"); // ru
    }

    @Test
    public void testConvert_232() {
        verify((char) 232, "म्र"); // mra
    }

    @Test
    public void testConvert_231() {
        verify((char) 231, "म्न"); // mna
    }

    @Test
    public void testConvert_230() {
        verify((char) 230, "भ्र"); // bhra
    }

    @Test
    public void testConvert_229() {
        // MANUALLY-ADDED CONVERSION
        verify((char) 229, "भ्न"); // bhna
    }

    @Test
    public void testConvert_228() {
        verify((char) 228, "ब्र"); // bra
    }

    @Test
    public void testConvert_227() {
        // MANUALLY-ADDED CONVERSION
        verify((char) 227, "ब्न"); // bna
    }

    @Test
    public void testConvert_226() {
        // MANUALLY-ADDED CONVERSION
        verify((char) 226, "ब्ज"); // bja
    }

    @Test
    public void testConvert_225() {
        // HALF-GLYPH
        verify((char) 225, "फ्"); // ph (pha + virama)
    }

    @Test
    public void testConvert_224() {
        verify((char) 224, "प्र"); // pra
    }

    @Test
    public void testConvert_223() {
        verify((char) 223, "प्न"); // pna
    }

    @Test
    public void testConvert_222() {
        // HALF-GLYPH
        // MANUALLY-ADDED CONVERSION
        verify((char) 222, "प्त्"); // pt (i.e. pa + virAma + ta + virAma)
    }

    @Test
    public void testConvert_221() {
        verify((char) 221, "प्त"); // pta
    }

    @Test
    public void testConvert_220() {
        verify((char) 220, "न्र"); // nra
    }

    @Test
    public void testConvert_219() {
        // HALF-GLYPH
        // MANUALLY-ADDED CONVERSION
        verify((char) 219, "न्न्"); // nn (i.e. na + viRama + na + virAma)
    }

    @Test
    public void testConvert_218() {
        verify((char) 218, "न्न"); // nna
    }

    @Test
    public void testConvert_217() {
        verify((char) 217, "न्त्र"); // ntra
    }

    @Test
    public void testConvert_216() {
        verify((char) 216, "ध्र"); // dhra
    }

    @Test
    public void testConvert_215() {
        // MANUALLY-ADDED CONVERSION
        verify((char) 215, "ध्न"); // dhna
    }

    @Test
    public void testConvert_214() {
        verify((char) 214, "द्व"); // dva
    }

    @Test
    public void testConvert_213() {
        verify((char) 213, "द्र्य"); // drya
    }

    @Test
    public void testConvert_212() {
        verify((char) 212, "द्र"); // dra
    }

    @Test
    public void testConvert_211() {
        verify((char) 211, "द्ब"); // dba
    }

    @Test
    public void testConvert_210() {
        // MANUALLY-ADDED CONVERSION
        verify((char) 210, "द्म"); // dma
    }

    @Test
    public void testConvert_209() {
        verify((char) 209, "द्भ"); // dbha
    }

    @Test
    public void testConvert_208() {
        // MANUALLY-ADDED CONVERSION
        verify((char) 208, "द्न"); // dna
    }

    @Test
    public void testConvert_207() {
        verify((char) 207, "द्ध"); // ddha
    }

    @Test
    public void testConvert_206() {
        verify((char) 206, "द्द"); // dda
    }

    @Test
    public void testConvert_205() {
        // MANUALLY-ADDED CONVERSION
        verify((char) 205, "द्ग्र"); // dgra
    }

    @Test
    public void testConvert_204() {
        verify((char) 204, "द्ग"); // dga
    }

    @Test
    public void testConvert_203() {
        verify((char) 203, "दू"); // dU (requires special placement)
    }

    @Test
    public void testConvert_202() {
        verify((char) 202, "दु"); // du (requires special placement)
    }

    @Test
    public void testConvert_201() {
        verify((char) 201, "भ"); // bha
    }

    @Test
    public void testConvert_200() {
        // HALF-GLYPH
        verify((char) 200, "त्र्"); // tr (ta + virama + ra + virama)
        verify("Èm", "त्र्म"); // trma
    }

    @Test
    public void testConvert_199() {
        verify((char) 199, "त्र"); // tra
    }

    @Test
    public void testConvert_198() {
        verify((char) 198, "त्न"); // tna
    }

    @Test
    public void testConvert_197() {
        // HALF-GLYPH
        verify((char) 197, "त्त्"); // tt (ta + virama + ta + virama)
    }

    @Test
    public void testConvert_196() {
        verify((char) 196, "त्त"); // tta
    }

    @Test
    public void testConvert_195() {
        verify((char) 195, "ञ्ज"); // ~nja
    }

    @Test
    public void testConvert_194() {
        verify((char) 194, "ञ्च"); // ~nca
    }

    @Test
    public void testConvert_193() {
        // HALF-GLYPH
        verify((char) 193, "ञ्"); // ~n combining (i.e. ~na + virama)
    }

    @Test
    public void testConvert_192() {
        verify((char) 192, "छ्र"); // chhra
    }

    @Test
    public void testConvert_191() {
        verify((char) 191, "ज्र"); // jra
    }

    @Test
    public void testConvert_190() {
        verify((char) 190, "ज्ज"); // jja
    }

    @Test
    public void testConvert_189() {
        verify((char) 189, "च्च"); // cca
    }

    @Test
    public void testConvert_188() {
        verify((char) 188, "ङ्ग"); // ~Nga
    }

    @Test
    public void testConvert_187() {
        verify((char) 187, "ङ्क"); // ~Nka
    }

    @Test
    public void testConvert_186() {
        verify((char) 186, "घ्र"); // ghra
    }

    @Test
    public void testConvert_185() {
        verify((char) 185, "घ्न"); // ghna
    }

    @Test
    public void testConvert_184() {
        // HALF-GLYPH
        // MANUALLY-CORRECTED CONVERSION
        verify((char) 184, "घ्"); // gh (gha + virama)
        verify("¸m", "घ्म"); // ghma
    }

    @Test
    public void testConvert_183() {
        // PASS THRU
        verify((char) 183, "·"); // centered period (aka middle dot). same in
        // both. note: will be escaped to HTML4
        // escape sequence &middot; by
        // conversion
    }

    @Test
    public void testConvert_182() {
        verify((char) 182, "ग्न"); // gna
    }

    @Test
    public void testConvert_181() {
        verify((char) 181, "ख्र"); // khra
    }

    @Test
    public void testConvert_180() {
        verify((char) 180, "क्त"); // kta
    }

    @Test
    public void testConvert_179() {
        verify((char) 179, "क्र"); // kra
    }

    @Test
    public void testConvert_178() {
        // MANUALLY-ADDED CONVERSION
        verify((char) 178, "क्न"); // kna
    }

    @Test
    public void testConvert_177() {
        // MANUALLY-ADDED CONVERSION
        verify((char) 177, "न्क्र"); // TODO confirm nkra
    }

    @Test
    public void testConvert_176() {
        // MANUALLY-ADDED CONVERSION
        verify((char) 176, "ॄ"); // vocalic R^I offset down (used e.g. with
        // ~NkR^I)
        verify("»°", "ङ्कॄ"); // ~NkR^I
    }

    @Test
    public void testConvert_175() {
        verify((char) 175, "ृ"); // R^i combining
    }

    @Test
    public void testConvert_174() {
        // HALF-GLYPH
        verify((char) 174, "क्त्"); // kta (i.e. ka + virama + ta + virama) used
        // e.g. in ktla
        verify("É®l", "भक्त्ल"); // bhaktla (might not be a real word)
    }

    @Test
    public void testConvert_173() {
        // PASS THRU
        verify((char) 173, "­"); // soft hyphen. same in both. will be escaped
        // to HTML4 escape sequence &shy; by
        // conversion
    }

    @Test
    public void testConvert_172() {
        verify((char) 172, "क्ल"); // kla
    }

    @Test
    public void testConvert_171() {
        // ZERO-WIDTH
        verify((char) 171, "ृ"); // R^i combining
    }

    @Test
    public void testConvert_170() {
        // ZERO-WIDTH
        verify((char) 170, "ू"); // U combining
    }

    @Test
    public void testConvert_169() {
        // ZERO-WIDTH
        verify((char) 169, "ॄ"); // combining R^I normal position (e.g. used
        // with
        // k)
        verify("k©:[", "कॄष्ण"); // kR^IShNa

    }

    @Test
    public void testConvert_168() {
        // ZERO-WIDTH
        verify((char) 168, "ॄ"); // combining R^I offset right position (e.g.
        // used
        // with t)
        verify("t¨[", "तॄण"); // tR^INa
        verify("Ã¨", "ञ्जॄ");// ~njR^I
    }

    @Test
    public void testConvert_167() {
        verify((char) 167, "ॠ"); // RR^I
    }

    @Test
    public void testConvert_166() {
        verify((char) 166, "ळ"); // LLa
    }

    @Test
    public void testConvert_165() {
        verify((char) 165, "ॡ"); // LL^I
    }

    @Test
    public void testConvert_164() {
        verify((char) 164, "ऌ"); // LL^i
    }

    @Test
    public void testConvert_163() {
        // ZERO-WIDTH
        verify((char) 163, "ॅ"); // ardhacandra for Hindi only
    }

    @Test
    public void testConvert_162() {
        verify((char) 162, "ग्र"); // gra
    }

    @Test
    public void testConvert_161() {
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
    public void testConvert_160() {
        // PASS THRU
        verify((char) 160, " "); // no break space
    }

    // ---------- CONTROL CHARACTERS START TODO cleanup? ---------------

    @Test
    public void testConvert_159() {
        verify((char) 159, "ह्ण"); // hNa (Akshara Bridge 376, because of
        // Western - Windows encoding)
    }

    @Test
    public void testConvert_159_Win_376() {
        verify((char) 376, "ह्ण"); // Windows-1252 for Ÿ
        // note: Mac Roman equiv is 252 for ü ... causes clash
    }

    @Test
    public void testConvert_158() throws UnsupportedEncodingException {
        verifyUnused(158);
    }

    @Test
    public void testConvert_158_Win_382() throws UnsupportedEncodingException {
        // Windows-1252 equiv is ž
        verifyUnused(382);
    }

    @Test
    public void testConvert_157() throws UnsupportedEncodingException {
        verifyUnused(157);
        // Windows-1252 equiv is also 157
    }

    @Ignore
    @Test
    public void testConvert_156() {
        // HALF-GLYPH
        verify((char) 156, "्"); // Windows-1252 equiv is œ
    }

    @Test
    public void testConvert_156_Win_339() {
        // HALF-GLYPH
        verify((char) 339, "्"); // Windows-1252 equiv is œ
    }

    @Test
    @Ignore
    public void testConvert_155() {
        verify((char) 155, ""); // TODO not sure how to create word
    }

    @Ignore
    @Test
    public void testConvert_154() {
        verify((char) 154, "᳚"); // vedik double svarita \'' (not \")
    }

    @Test
    public void testConvert_154_Win_353() {
        verify((char) 353, "᳚"); // Windows-1252 equiv is š
        verify("yš", "य᳚");
    }

    @Ignore
    @Test
    public void testConvert_153() {
        verify((char) 153, "द्व्य"); // dvya
    }

    @Test
    public void testConvert_153_Win_8482() {
        verify((char) 8482, "द्व्य"); // Windows-1252 equiv is ™
    }

    @Ignore
    @Test
    public void testConvert_152() {
        verify((char) 152, "द्घ"); // dgha
    }

    @Test
    public void testConvert_152_Win_732() {
        verify((char) 732, "द्घ"); // Windows-1252 equiv is ˜
    }

    @Ignore
    @Test
    public void testConvert_151() {
        verify((char) 151, "ं"); // anusvara in upper right, e.g. in kIM
        // TODO test word too: kI + control character
    }

    @Test
    public void testConvert_151_Win_8212() {
        verify((char) 8212, "ं"); // Windows-1252 equiv is em dash —
    }

    @Ignore
    @Test
    public void testConvert_150() {
        verify((char) 150, "़"); // "nukta" dot for the hindi "rDa character" .Da
    }

    @Test
    public void testConvert_150_Win_8211() {
        // uses nukta in Unicode 2364 (center under)
        // this will only appear with with another letter, never by itself
        // verify pass thru
        verify((char) 8211, "–"); // Windows-1252 equiv is en dash –
        verify("–f", "ड़"); // .Da letter
        verify("–F", "ढ़"); // .Dha letter
    }

    @Test
    @Ignore
    public void testConvert_149() {
        verify((char) 149, ""); // Marathi character: R
        // Windows-1252 equiv is bullet 8226 •
    }

    @Ignore
    @Test
    public void testConvert_148() {
        verify((char) 148, "ट्ट"); // TTa
    }

    @Test
    public void testConvert_148_Win_8221() {
        verify((char) 8221, "ट्ट"); // Windows-1252 equiv is ”
    }

    @Ignore
    @Test
    public void testConvert_147() {
        verify((char) 147, "ङ्क्त"); // ~Nkta
    }

    @Test
    public void testConvert_147_Win_8220() {
        verify((char) 8220, "ङ्क्त"); // Windows-1252 equiv is “
    }

    @Ignore
    @Test
    public void testConvert_146() {
        verify((char) 146, "ह्ल"); // hla
    }

    @Test
    public void testConvert_146_Win_8217() {
        verify((char) 8217, "ह्ल"); // Windows-1252 equiv is ’
    }

    @Ignore
    @Test
    public void testConvert_145() {
        verify((char) 145, "ल्ल"); // lla
    }

    @Test
    public void testConvert_145_Win_8216() {
        verify((char) 8216, "ल्ल"); // Windows-1252 equiv is ‘
    }

    @Test
    public void testConvert_144() {
        verifyUnused(144);
    }


    @Test
    public void testConvert_143() {
        verifyUnused(143);
    }

    @Test
    public void testConvert_142() {
        verifyUnused(142);
    }

    @Test
    public void testConvert_141() {
        verifyUnused(141);
    }

    @Ignore
    @Test
    public void testConvert_140() {
        verify((char) 140, "ङ्ख"); // ~Nkha
    }

    @Test
    public void testConvert_140_Win_338() {
        verify((char) 338, "ङ्ख"); // Windows-1252 equiv is Œ
    }

    @Ignore
    @Test
    public void testConvert_139() {
        verify((char) 139, "क्च"); // kcha or kca
    }

    @Test
    public void testConvert_139_Win_8249() {
        verify((char) 8249, "क्च"); // Windows-1252 equiv is ‹
    }

    @Ignore
    @Test
    public void testConvert_138() {
        verify((char) 138, "क्क"); // kka
    }

    @Test
    public void testConvert_138_Win_352() {
        verify((char) 352, "क्क"); // Windows-1252 equiv is Š
    }

    @Ignore
    @Test
    public void testConvert_137() {
        verify((char) 137, "्"); // lowered virama, e.g. in dn
    }

    @Test
    public void testConvert_137_Win_8240() {
        verify((char) 8240, "्"); // Windows-1252 equiv is ‰
        verify("Ð‰", "द्न्"); // word test Ð‰ for dn
    }

    @Ignore
    @Test
    public void testConvert_136() {
        verify((char) 136, "़"); // Hindi nukta dot on left, e.g. in qa
        // TODO word test
    }

    @Test
    public void testConvert_136_Win_710() {
        // nukta always appears in the following pairs, never by itself
        // corresponds to unicode 2364 (same as center nukta)
        // verify pass thru if by itself (b/c replacement order is switched)
        verify((char) 710, "ˆ"); // Windows-1252 equiv is ˆ
        verify("ˆk", "क़"); // qa
        verify("ˆo", "ख़"); // Ka
        verify("ˆg", "ग़"); // Ga
        verify("ˆj", "ज़"); // Ja
        verify("ˆ)", "फ़"); // fa
    }

    @Test
    @Ignore
    public void testConvert_135() {
        verify((char) 135, ""); // {\m+} i.e. anunasika with virama
    }

    @Test
    @Ignore
    public void testConvert_135_Win_8225() {
        verify((char) 8225, ""); // Windows-1252 equiv is ‡
    }

    @Ignore
    @Test
    public void testConvert_134() {
        verify((char) 134, "दृ"); // dR^i
    }

    @Test
    public void testConvert_134_Win_8224() {
        verify((char) 8224, "दृ"); // Windows-1252 equiv is †
    }

    @Ignore
    @Test
    public void testConvert_133() {
        verify((char) 133, "ु"); // short u e.g. with ph
    }

    @Test
    public void testConvert_133_Win_8230() {
        verify((char) 8230, "ु"); // Windows-1252 equiv is …
    }

    @Ignore
    @Test
    public void testConvert_132() {
        verify((char) 132, "कॢ"); // kL^i
    }

    @Test
    public void testConvert_132_Win_8222() {
        verify((char) 8222, "कॢ"); // Windows-1252 equiv is „
    }

    @Ignore
    @Test
    public void testConvert_131() {
        // ZERO-WIDTH
        verify((char) 131, "ू"); // low U combining, e.g. in ~NgU
    }

    @Test
    public void testConvert_131_Win_402() {
        // ZERO-WIDTH
        verify((char) 402, "ू"); // Windows-1252 equiv is ƒ
        verify("¼ƒ", "ङ्गू"); // ~NgU
    }

    @Ignore
    @Test
    public void testConvert_130() {
        // ZERO-WIDTH
        verify((char) 130, "ङ्गु"); // low u combining, e.g. in ~Ngu
    }

    @Test
    public void testConvert_130_Win_8218() {
        // ZERO-WIDTH
        verify((char) 8218, "ु"); // Windows-1252 equiv is ‚
        verify("¼‚", "ङ्गु"); // ~Ngu
    }

    @Test
    public void testConvert_129() {
        verifyUnused(129);
    }

    @Test
    public void testConvert_128() {
        verifyUnused(128);
    }

    @Test
    public void testConvert_127() {
        verifyUnused(127);
    }

    // ---------- CONTROL CHARACTERS END --------------------------

    @Test
    public void testConvert_126() {
        // ZERO-WIDTH
        verify((char) 126, "ँ"); // candrabindu
    }

    @Test
    public void testConvert_125() {
        verify((char) 125, "ज्ञ"); // j~na
    }

    @Test
    public void testConvert_124() {
        verify((char) 124, "ञ"); // ~na
    }

    @Test
    public void testConvert_123() {
        // HALF-GLYPH
        verify((char) 123, "ण्"); // N (Na + virama)
    }

    @Test
    public void testConvert_122() {
        verify((char) 122, "श"); // sha
    }

    @Test
    public void testConvert_121() {
        verify((char) 121, "य"); // ya
    }

    @Test
    public void testConvert_120() {
        verify((char) 120, "ध"); // dha
    }

    @Test
    public void testConvert_119() {
        verify((char) 119, "थ"); // tha
    }

    @Test
    public void testConvert_118() {
        verify((char) 118, "व"); // va
    }

    @Test
    public void testConvert_117() {
        // ZERO-WIDTH
        verify((char) 117, "ु"); // u combining
    }

    @Test
    public void testConvert_116() {
        verify((char) 116, "त"); // ta
    }

    @Test
    public void testConvert_115() {
        verify((char) 115, "स"); // sa
    }

    @Test
    public void testConvert_114() {
        verify((char) 114, "र"); // ra
    }

    @Test
    public void testConvert_113() {
        verify((char) 113, "ट"); // Ta
    }

    @Test
    public void testConvert_112() {
        verify((char) 112, "प"); // pa
    }

    @Test
    public void testConvert_111() {
        verify((char) 111, "ख"); // kha
    }

    @Test
    public void testConvert_110() {
        verify((char) 110, "न"); // na
    }

    @Test
    public void testConvert_109() {
        verify((char) 109, "म"); // ma
    }

    @Test
    public void testConvert_108() {
        verify((char) 108, "ल"); // la
    }

    @Test
    public void testConvert_107() {
        verify((char) 107, "क"); // ka
    }

    @Test
    public void testConvert_106() {
        verify((char) 106, "ज"); // ja
    }

    @Test
    public void testConvert_105() {
        verify((char) 105, "ि"); // short combining i
        verify("ik", "कि"); // test letter reordering
    }

    @Test
    public void testConvert_104() {
        verify((char) 104, "ह"); // ha
    }

    @Test
    public void testConvert_103() {
        verify((char) 103, "ग"); // ga
    }

    @Test
    public void testConvert_102() {
        verify((char) 102, "ड"); // Da
    }

    @Test
    public void testConvert_101() {
        // ZERO-WIDTH
        verify((char) 101, "े"); // e (combining)
    }

    @Test
    public void testConvert_100() {
        verify((char) 100, "द"); // da
    }

    @Test
    public void testConvert_99() {
        verify((char) 99, "च"); // cha
    }

    @Test
    public void testConvert_98() {
        verify((char) 98, "ब"); // ba
    }

    @Test
    public void testConvert_97() {
        verify((char) 97, "ा"); // A (combining)
    }

    @Test
    public void testConvert_96() {
        verify((char) 96, "ॐ"); // OM
    }

    @Test
    public void testConvert_95() {
        // HALF-GLYPH
        verify((char) 95, "भ्"); // bh (bha + virama)
    }

    @Test
    public void testConvert_94() {
        verify((char) 94, "ऊ"); // U (complete)
    }

    @Test
    public void testConvert_93() {
        verify((char) 93, "क्ष"); // kSha
    }

    @Test
    public void testConvert_92() {
        verify((char) 92, "ऋ"); // RR^i
    }

    @Test
    public void testConvert_91() {
        verify((char) 91, "ण"); // Na
    }

    @Test
    public void testConvert_90() {
        // HALF-GLYPH
        verify((char) 90, "श्"); // sh (sha + virama)
    }

    @Test
    public void testConvert_89() {
        // HALF-GLYPH
        verify((char) 89, "य्"); // y (ya + virama)
    }

    @Test
    public void testConvert_88() {
        // HALF-GLYPH
        verify((char) 88, "ध्"); // dh (dha + virama)
    }

    @Test
    public void testConvert_87() {
        // HALF-GLYPH
        verify((char) 87, "थ्"); // th (tha + virama)
    }

    @Test
    public void testConvert_86() {
        // HALF-GLYPH
        verify((char) 86, "व्"); // v (va + virama)
    }

    @Test
    public void testConvert_85() {
        // ZERO-GLYPH
        verify((char) 85, "ू"); // U combining
    }

    @Test
    public void testConvert_84() {
        // HALF-GLYPH
        verify((char) 84, "त्"); // t (ta + virama)
    }

    @Test
    public void testConvert_83() {
        // HALF-GLYPH
        verify((char) 83, "स्"); // s (sa + virama)
    }

    @Test
    public void testConvert_82() {
        // ZERO-WIDTH
        verify((char) 82, "R"); // combining r. the logic will not
        // replace by itself, but only when next to
        // letters b/c order needs to be swapped
        verify("kmR", "कर्म"); // example word with R
    }

    @Test
    public void testConvert_81() {
        verify((char) 81, "ठ"); // Tha
    }

    @Test
    public void testConvert_80() {
        // HALF-GLYPH
        verify((char) 80, "प्"); // p (pa + virama)
    }

    @Test
    public void testConvert_79() {
        // HALF-GLYPH
        verify((char) 79, "ख्"); // kh (kha + virama)
    }

    @Test
    public void testConvert_78() {
        // HALF-GLYPH
        verify((char) 78, "न्"); // n (na + virama)
    }

    @Test
    public void testConvert_77() {
        // HALF-GLYPH
        verify((char) 77, "म्"); // m (ma + virama)
    }

    @Test
    public void testConvert_76() {
        // HALF-GLYPH
        verify((char) 76, "ल्"); // l (la + virama)
    }

    @Test
    public void testConvert_75() {
        // HALF-GLYPH
        verify((char) 75, "क्"); // k (ka + virama)
    }

    @Test
    public void testConvert_74() {
        // HALF-GLYPH
        verify((char) 74, "ज्"); // j (ja + virama)
    }

    @Test
    public void testConvert_73() {
        verify((char) 73, "ी"); // I (combining)
    }

    @Test
    public void testConvert_72() {
        verify((char) 72, "झ"); // jha
    }

    @Test
    public void testConvert_71() {
        // HALF-GLYPH
        verify((char) 71, "ग्"); // g (ga + virama)
    }

    @Test
    public void testConvert_70() {
        verify((char) 70, "ढ"); // Dha
    }

    @Test
    public void testConvert_69() {
        // ZERO-WIDTH
        verify((char) 69, "ै"); // ai conjunct
        verify("kE", "कै"); // kai
    }

    @Test
    public void testConvert_68() {
        verify((char) 68, "छ"); // chha
    }

    @Test
    // HALF-GLYPH
    public void testConvert_67() {
        verify((char) 67, "च्"); // c (i.e. ca + virama)
    }

    @Test
    public void testConvert_66() {
        // HALF-GLYPH
        verify((char) 66, "ब्"); // b (i.e. ba + virama)
    }

    @Test
    public void testConvert_65() {
        verify((char) 65, "अ"); // a (complete)
    }

    @Test
    public void testConvert_64() {
        verify((char) 64, "ए"); // e (complete)
    }

    @Test
    public void testConvert_63() {
        // ZERO-WIDTH
        verify((char) 63, "॑"); // vedik ' udatta
    }

    @Test
    public void testConvert_62() {
        verify((char) 62, "ः"); // visarga
    }

    @Test
    public void testConvert_61() {
        verify((char) 61, "ऽ"); // avagraha (i.e. apostrophe)
        verify("kae=hm!", "कोऽहम्"); // ko.aham
    }

    @Test
    public void testConvert_60() {
        // ZERO-WIDTH
        verify((char) 60, "ं"); // anusvara
    }

    @Test
    public void testConvert_59() {
        verify((char) 59, "ष"); // Sha
    }

    @Test
    public void testConvert_58() {
        // HALF-GLYPH
        verify((char) 58, "ष्"); // Sha left-hand half ligature
    }

    @Test
    public void testConvert_57() {
        verify((char) 57, "९"); // 9
    }

    @Test
    public void testConvert_56() {
        verify((char) 56, "८"); // 8
    }

    @Test
    public void testConvert_55() {
        verify((char) 55, "७"); // 7
    }

    @Test
    public void testConvert_54() {
        verify((char) 54, "६"); // 6
    }

    @Test
    public void testConvert_53() {
        verify((char) 53, "५"); // 5
    }

    @Test
    public void testConvert_52() {
        verify((char) 52, "४"); // 4
    }

    @Test
    public void testConvert_51() {
        verify((char) 51, "३"); // 3
    }

    @Test
    public void testConvert_50() {
        verify((char) 50, "२"); // 2
    }

    @Test
    public void testConvert_49() {
        verify((char) 49, "१"); // 1
    }

    @Test
    public void testConvert_48() {
        verify((char) 48, "०"); // 0
    }

    @Test
    public void testConvert_47() {
        // ZERO-WIDTH
        verify((char) 47, "॒"); // vedik _ anudātta accent
    }

    @Test
    public void testConvert_46() {
        verify((char) 46, "॥"); // double danda
    }

    @Test
    public void testConvert_45() {
        // PASS THRU
        verify((char) 45, "-"); // hyphen
    }

    @Test
    public void testConvert_44() {
        verify((char) 44, "।"); // danda
    }

    @Test
    public void testConvert_43() {
        // ZERO-WIDTH
        verify((char) 43, "र"); // ra matra used retroflex group
        verify("q+ Q+ f+ F+", "ट्र ठ्र ड्र ढ्र");
    }

    @Test
    public void testConvert_42() {
        verify((char) 42, "द्य"); // dya
    }

    @Test
    public void testConvert_41() {
        verify((char) 41, "फ"); // pha (complete)
    }

    @Test
    public void testConvert_40() {
        // HALF-GLYPH
        verify((char) 40, "्य"); // ya right-hand half ligature (e.g. with bhrya
        // in iTranslator99)
    }

    @Test
    public void testConvert_39() {
        verify((char) 39, "ङ"); // ~Na (complete)
    }

    @Test
    public void testConvert_38() {
        // ZERO-WIDTH
        verify((char) 38, "ृ"); // R^i for stem to right (e.g. with gha not ka)
    }

    @Test
    public void testConvert_37() {
        verify((char) 37, "उ"); // u (complete)
    }

    @Test
    public void testConvert_36() {
        verify((char) 36, "ई"); // I (complete)
    }

    @Test
    public void testConvert_35() {
        verify((char) 35, "इ"); // i (complete)
    }

    @Test
    public void testConvert_34() {
        verify((char) 34, "घ"); // gha
    }

    @Test
    public void testConvert_33() {
        // ZERO-WIDTH
        verify((char) 33, "्"); // virama
    }

    @Test
    public void testConvert_32() {
        // PASS THRU
        verify((char) 32, " "); // space
    }

    // TODO add pass thru tests for 00 - 31 control characters

}
