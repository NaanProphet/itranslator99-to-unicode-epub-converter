package org.dontexist.kb.service.converter;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:app-context.xml")
public class SpanContainerTest {

    @Autowired
    private SpanContainerService spanContainerService;

    private void verify(String inputCorrectSpanOpeningTag, String expectedAttribute) {
        String actual = spanContainerService.parseSpanAttribute(inputCorrectSpanOpeningTag);
        Assert.assertEquals(expectedAttribute, actual);
    }

    @Test
    public void testParseSpanAttribute_1() throws Exception {
        verify("<span class=\"san1\">", "san1");
    }

    @Test
    public void testParseSpanAttribute_2() throws Exception {
        verify("<span class=\"\">", "");
    }

    @Test
    public void testParseSpanAttribute_3() throws Exception {
        verify("<span class=\"hullo\"  >", "hullo");
    }

    @Test
    public void testParseSpanAttribute_4() throws Exception {
        verify("<span>", null);
    }

    @Test
    public void testParseSpanAttribute_5() throws Exception {
        verify("<span class = \"hullo\"  >", "hullo");
    }
}