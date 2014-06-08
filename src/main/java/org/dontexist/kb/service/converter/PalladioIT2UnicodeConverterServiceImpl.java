package org.dontexist.kb.service.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

import static org.apache.commons.lang3.StringUtils.replaceEach;

/**
 * Converts Romanized Sanskrit text encoded in the non-standard PalladioIT font into standard Unicode.
 * <p/>
 * Uses {@link LinkedHashMap}s to preserve insert order for the conversion since certain replacements must occur in a
 * particular sequence.
 */
@Service
public class PalladioIT2UnicodeConverterServiceImpl extends AbstractText2UnicodeService implements PalladioIT2UnicodeConverterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PalladioIT2UnicodeConverterServiceImpl.class);

    private final LinkedHashMap<String, String> it2UnicodeMap = new LinkedHashMap<String, String>();

    private final LinkedHashMap<String, String> palladioPostReplacementCorrections;

    @Autowired
    public PalladioIT2UnicodeConverterServiceImpl(@Value("#{palladioPostReplacementCorrections}")
                                                  LinkedHashMap<String, String> palladioPostReplacementCorrections) {
        initializeMappings();
        this.palladioPostReplacementCorrections = palladioPostReplacementCorrections;
    }

    /**
     * Converts PalladioIT palladioInput to Unicode convertedUtf8.
     *
     * @param palladioInput unescaped palladioInput in the PalladioIT font
     * @return converted unescaped UTF-8 text
     */
    @Override
    public String convert(final String palladioInput) {

        final String output = convertPalladioToUnicode(palladioInput, it2UnicodeMap);

        // correct non-sanskrit accented words that might have been incorrectly changed
        final String finalOutput = performPostReplacementCorrections(output);

        LOGGER.debug("Converted palladioInput [{}] to unicode convertedUtf8 [{}]", palladioInput, finalOutput);
        return finalOutput;
    }

    /**
     * Performs the straightforward character replacement for PalladioIT to Unicode conversion. <p>Note: all characters
     * are treated equally, so potentially other accented words may be adversely affected.
     *
     * @param palladioInput the original input text
     * @param it2UnicodeMap a map of PalladioIT keys to Unicode replacement values
     * @return the replaced Unicode text, with possible typos in any non-Sanskrit accented words originally passed in
     */
    private String convertPalladioToUnicode(final String palladioInput, final LinkedHashMap<String, String> it2UnicodeMap) {
        String[] searchList = it2UnicodeMap.keySet().toArray(new String[0]);
        String[] replacementList = it2UnicodeMap.values().toArray(new String[0]);
        // Note: replaceEach (unlike replaceEachRepeatedly) will not replace replacements, and thus will automatically
        // respect the few order-depedenant replacements in PalladioIT conversion
        String output = replaceEach(palladioInput, searchList, replacementList);
        return output;
    }

    /**
     * Performs special replacements to correct non-sanskrit accented words that might have been incorrectly changed
     * after performing PalladioIT to Unicode conversion. <p> For example, the word <i>fiancé</i> after initial
     * conversion is changed to <i>fiancī</i>. After calling this method, it is changed back to <i>fiancé</i>. </p>
     *
     * @param convertedUtf8 the unicode string after running PalladioIT to Unicode conversion
     * @return the final corrected unicode string
     */
    private String performPostReplacementCorrections(final String convertedUtf8) {
        String correctedOutput = convertPalladioToUnicode(convertedUtf8, palladioPostReplacementCorrections);
        LOGGER.debug("Performed post replacements corrections on [{}] to [{}]", convertedUtf8, correctedOutput);
        return correctedOutput;
    }


    /**
     * Initializes key-value mappings of all 32 PalladioIT characters for replacement.
     */
    private void initializeMappings() {

        // @sonar:off
        // @formatter:off

        // ------ START OF ORDER-DEPENDANT REPLACEMENTS --------
        // to prevent unintentional replacements-of-replacements,
        // the following pairs must be replaced in order given below

        addToMap(0x00D1, 0x1E62);              // Ñ -> Ṣ
        addToMap(0x00CF, 0x00D1);              // Ï -> Ñ

        addToMap(0x00F1, 0x1E63);              // ñ -> ṣ
        addToMap(0x00EF, 0x00F1);              // ï -> ñ

        // ------ END OF ORDER-DEPENDANT REPLACEMENTS --------

        addToMap(0x00C4, 0x0100);             // Ä -> Ā
        addToMap(0x00E4, 0x0101);             // ä -> ā
        addToMap(0x00C9, 0x012A);             // É -> Ī
        addToMap(0x00E9, 0x012B);             // é -> ī
        addToMap(0x00C7, 0x015A);             // Ç -> Ś
        addToMap(0x00E7, 0x015B);             // ç -> ś
        addToMap(0x00DC, 0x016A);             // Ü -> Ū
        addToMap(0x00FC, 0x016B);             // ü -> ū
        addToMap(0x00D2, 0x1E0C);             // Ò -> Ḍ
        addToMap(0x00F2, 0x1E0D);             // ò -> ḍ
        addToMap(0x00D9, 0x1E24);             // Ù -> Ḥ
        addToMap(0x00F9, 0x1E25);             // ù -> ḥ
        addToMap(0x00DF, 0x1E36);             // ß -> Ḷ
        addToMap(0x00C0, 0x1E40);             // À -> Ṁ
        addToMap(0x00E0, 0x1E41);             // à -> ṁ
        addToMap(0x00CC, 0x1E44);             // Ì -> Ṅ
        addToMap(0x00EC, 0x1E45);             // ì -> ṅ
        addToMap(0x00CB, 0x1E46);             // Ë -> Ṇ
        addToMap(0x00EB, 0x1E47);             // ë -> ṇ
        addToMap(0x00C5, 0x1E5A);             // Å -> Ṛ
        addToMap(0x00E5, 0x1E5B);             // å -> ṛ
        addToMap(0x00C8, 0x1E5C);             // È -> Ṝ
        addToMap(0x00E8, 0x1E5D);             // è -> ṝ
        addToMap(0x00D6, 0x1E6C);             // Ö -> Ṭ
        addToMap(0x00F6, 0x1E6D);             // ö -> ṭ
        addToMap(0x00D7, 0x030D);             // × -> Vedik accent '
        addToMap(0x00DE, 0x0331);             // Þ -> Vedik accent _
        addToMap(0x00F7, 0x030E);             // ÷ -> Vedik accent ''

        // @sonar:on
        // @formatter:on
    }

    /**
     * Convenience method for initializing the main PalladioIT conversion map used by this class.
     *
     * @param nonUnicodeCodePoint the non-Unicode PalladioIT code point to convert from
     * @param unicodeCodePoint    the Unicode code point to convert to
     */
    private void addToMap(int nonUnicodeCodePoint, int unicodeCodePoint) {
        final String nonUnicodeString = new Character((char) nonUnicodeCodePoint).toString();
        final String unicodeString = new Character((char) unicodeCodePoint).toString();
        it2UnicodeMap.put(nonUnicodeString, unicodeString);
    }

}
