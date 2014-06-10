package org.dontexist.kb.service;

import org.apache.commons.collections4.SetUtils;
import org.dontexist.kb.service.epuboperations.EpubReaderService;
import org.dontexist.kb.service.epuboperations.ParentEpubReaderServiceImpl;
import org.dontexist.kb.service.epuboperations.ZipEpubReaderServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.Set;

public class ParentEpubReaderServiceImplTest {

    // TODO rename
    private ParentEpubReaderServiceImpl epubReaderService;

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
    public void setUp() {
        epubReaderService = new ParentEpubReaderServiceImpl();
    }

    @Test
    public void testAddSanskritSpanClasses_NullList() throws Exception {
        String csvString = null;
        Set<String> expected = SetUtils.emptySet();
        ReflectionTestUtils.invokeMethod(epubReaderService, "addSanskritSpanClasses", csvString);
        Set actual = (Set) ReflectionTestUtils.getField(epubReaderService, SANSKRIT_SPAN_SET_FIELD_NAME);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddSanskritSpanClasses_GoodListNoDuplicates() throws Exception {
        String csvString = "span1,span2,span3";
        Set<String> expected = expectedSetOfThreeSpans();
        ReflectionTestUtils.invokeMethod(epubReaderService, "addSanskritSpanClasses", csvString);
        Set actual = (Set) ReflectionTestUtils.getField(epubReaderService, SANSKRIT_SPAN_SET_FIELD_NAME);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddSanskritSpanClasses_GoodListWithDuplicates() throws Exception {
        String csvString = "span1,span2,span1,span3";
        Set<String> expected = expectedSetOfThreeSpans();
        ReflectionTestUtils.invokeMethod(epubReaderService, "addSanskritSpanClasses", csvString);
        Set actual = (Set) ReflectionTestUtils.getField(epubReaderService, SANSKRIT_SPAN_SET_FIELD_NAME);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAfterPropertiesSet() throws Exception {
        ReflectionTestUtils.setField(epubReaderService, SANSKRIT_SPAN_CSV_FIELD_NAME, "span1,span2,span3");
        Set<String> expected = expectedSetOfThreeSpans();
        epubReaderService.afterPropertiesSet();
        Set actual = (Set) ReflectionTestUtils.getField(epubReaderService, SANSKRIT_SPAN_SET_FIELD_NAME);
        Assert.assertEquals(expected, actual);
    }

}
