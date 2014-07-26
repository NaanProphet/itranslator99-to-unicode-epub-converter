package org.dontexist.kb.service.converter;

import com.google.common.collect.ListMultimap;
import org.apache.commons.collections4.SetUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:app-context.xml"})
public class UnicodeConverterHelperTest {

    @Autowired
    private UnicodeConverterHelper unicodeConverterHelperReal;

    private UnicodeConverterHelper unicodeConverterHelper;

    private static final String SANSKRIT_SPAN_SET_FIELD_NAME = "sanskrit99SpanClasses";
    private static final String SANSKRIT_SPAN_CSV_FIELD_NAME = "sanskrit99SpanClassCsv";

    private static Set<String> expectedSetOfThreeSpans() {
        Set<String> expected = new HashSet<String>();
        // order does not matter for HashSet equals() method
        expected.add("span1");
        expected.add("span2");
        expected.add("span3");
        return expected;
    }

    @Before
    public void setup() {
        this.unicodeConverterHelper = new UnicodeConverterHelper();
    }

    @Test
    public void testAddSanskritSpanClasses_NullList() throws Exception {
        String csvString = null;
        Set<String> expected = SetUtils.emptySet();
        ReflectionTestUtils.invokeMethod(unicodeConverterHelper, "addSanskritSpanClasses", csvString);
        Set actual = (Set) ReflectionTestUtils.getField(unicodeConverterHelper, SANSKRIT_SPAN_SET_FIELD_NAME);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddSanskritSpanClasses_GoodListNoDuplicates() throws Exception {
        String csvString = "span1,span2,span3";
        Set<String> expected = expectedSetOfThreeSpans();
        ReflectionTestUtils.invokeMethod(unicodeConverterHelper, "addSanskritSpanClasses", csvString);
        Set actual = (Set) ReflectionTestUtils.getField(unicodeConverterHelper, SANSKRIT_SPAN_SET_FIELD_NAME);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddSanskritSpanClasses_GoodListWithDuplicates() throws Exception {
        String csvString = "span1,span2,span1,span3";
        Set<String> expected = expectedSetOfThreeSpans();
        ReflectionTestUtils.invokeMethod(unicodeConverterHelper, "addSanskritSpanClasses", csvString);
        Set actual = (Set) ReflectionTestUtils.getField(unicodeConverterHelper, SANSKRIT_SPAN_SET_FIELD_NAME);
        assertEquals(expected, actual);
    }

    @Test
    public void testAfterPropertiesSet() throws Exception {
        ReflectionTestUtils.setField(unicodeConverterHelper, SANSKRIT_SPAN_CSV_FIELD_NAME, "span1,span2,span3");
        Set<String> expected = expectedSetOfThreeSpans();
        unicodeConverterHelper.afterPropertiesSet();
        Set actual = (Set) ReflectionTestUtils.getField(unicodeConverterHelper, SANSKRIT_SPAN_SET_FIELD_NAME);
        assertEquals(expected, actual);
    }

    // TODO add nested span tests here


    @Test
    public void testSplitUpSpanString() throws Exception {
        final String s1 = "<span class=\"sans\">";
        final String s2 = "hullo";
        final String s3 = "<span class=\"times\">";
        final String s4 = "to be or not to be";
        final String s5 = "</span>";
        final String s6 = "is not the question";
        final String s7 = "<br class=\"calibre5\"/>";
        final String s8 = "</span>";
        final String input = s1 + s2 + s3 + s4 + s5 + s6 + s7 + s8;

        final List<ParseContainer> textBlocks = unicodeConverterHelperReal.splitUpSpanString(input);
        assertEquals(8, textBlocks.size());
        Iterator<ParseContainer> it = textBlocks.iterator();

        ParseContainer value = it.next();
        assertEquals(s1, value.getRawText());
        assertEquals(unicodeConverterHelperReal.getRomanizedSanskritConverter(), value.getConverter());

        value = it.next();
        assertEquals(s2, value.getRawText());
        assertEquals(unicodeConverterHelperReal.getDevanagariSanskritConverter(), value.getConverter());

        value = it.next();
        assertEquals(s3, value.getRawText());
        assertEquals(unicodeConverterHelperReal.getRomanizedSanskritConverter(), value.getConverter());

        value = it.next();
        assertEquals(s4, value.getRawText());
        assertEquals(unicodeConverterHelperReal.getRomanizedSanskritConverter(), value.getConverter());

        value = it.next();
        assertEquals(s5, value.getRawText());
        assertEquals(unicodeConverterHelperReal.getRomanizedSanskritConverter(), value.getConverter());

        value = it.next();
        assertEquals(s6, value.getRawText());
        assertEquals(unicodeConverterHelperReal.getDevanagariSanskritConverter(), value.getConverter());

        value = it.next();
        assertEquals(s7, value.getRawText());
        assertEquals(unicodeConverterHelperReal.getRomanizedSanskritConverter(), value.getConverter());

        value = it.next();
        assertEquals(s8, value.getRawText());
        assertEquals(unicodeConverterHelperReal.getRomanizedSanskritConverter(), value.getConverter());

        assertFalse(it.hasNext());
    }

    @Test
    public void trickyStringWithLotsOfTags() {
        final String s1 = "<p class=\"justify3\">";
        final String s2 = "<span class=\"bodytext\">";
        final String s3 = "<a href=\"part0006.html#ch1\">";
        final String s4 = "Marriage – A Melody";
        final String s5 = "</a>";
        final String s6 = "</span>";
        final String s7 = "</p>";
        final String input = s1 + s2 + s3 + s4 + s5 + s6 + s7;

        final List<ParseContainer> textBlocks = unicodeConverterHelperReal.splitUpSpanString(input);
        assertEquals(7, textBlocks.size());
        Iterator<ParseContainer> it = textBlocks.iterator();

        ParseContainer value = it.next();
        assertEquals(s1, value.getRawText());
        assertEquals(unicodeConverterHelperReal.getRomanizedSanskritConverter(), value.getConverter());

        value = it.next();
        assertEquals(s2, value.getRawText());
        assertEquals(unicodeConverterHelperReal.getRomanizedSanskritConverter(), value.getConverter());

        value = it.next();
        assertEquals(s3, value.getRawText());
        assertEquals(unicodeConverterHelperReal.getRomanizedSanskritConverter(), value.getConverter());

        value = it.next();
        assertEquals(s4, value.getRawText());
        assertEquals(unicodeConverterHelperReal.getRomanizedSanskritConverter(), value.getConverter());

        value = it.next();
        assertEquals(s5, value.getRawText());
        assertEquals(unicodeConverterHelperReal.getRomanizedSanskritConverter(), value.getConverter());

        value = it.next();
        assertEquals(s6, value.getRawText());
        assertEquals(unicodeConverterHelperReal.getRomanizedSanskritConverter(), value.getConverter());

        value = it.next();
        assertEquals(s7, value.getRawText());
        assertEquals(unicodeConverterHelperReal.getRomanizedSanskritConverter(), value.getConverter());

        assertFalse(it.hasNext());
    }

    @Test
    public void testSanskritBlock() throws Exception {
        final String s1 = "<p class=\"justify1\">";
        final String s2 = "<span class=\"santext\">";
        final String s3 = "विवाहदिनमिदं भवतु हर्षदम्।";
        final String s4 = "<br class=\"calibre4\"/>";
        final String s5 = "मङ्गलं तथा वां च क्षेमदम्॥१॥";
        final String s6 = "</span>";
        final String s7 = "</p>";
        final String input = s1 + s2 + s3 + s4 + s5 + s6 + s7;

        final List<ParseContainer> textBlocks = unicodeConverterHelperReal.splitUpSpanString(input);
        assertEquals(7, textBlocks.size());
        Iterator<ParseContainer> it = textBlocks.iterator();

        ParseContainer value = it.next();
        assertEquals(s1, value.getRawText());
        assertEquals(unicodeConverterHelperReal.getRomanizedSanskritConverter(), value.getConverter());

        value = it.next();
        assertEquals(s2, value.getRawText());
        assertEquals(unicodeConverterHelperReal.getRomanizedSanskritConverter(), value.getConverter());

        value = it.next();
        assertEquals(s3, value.getRawText());
        assertEquals(unicodeConverterHelperReal.getDevanagariSanskritConverter(), value.getConverter());

        value = it.next();
        assertEquals(s4, value.getRawText());
        assertEquals(unicodeConverterHelperReal.getRomanizedSanskritConverter(), value.getConverter());

        value = it.next();
        assertEquals(s5, value.getRawText());
        assertEquals(unicodeConverterHelperReal.getDevanagariSanskritConverter(), value.getConverter());

        value = it.next();
        assertEquals(s6, value.getRawText());
        assertEquals(unicodeConverterHelperReal.getRomanizedSanskritConverter(), value.getConverter());

        value = it.next();
        assertEquals(s7, value.getRawText());
        assertEquals(unicodeConverterHelperReal.getRomanizedSanskritConverter(), value.getConverter());

        assertFalse(it.hasNext());
    }
}
