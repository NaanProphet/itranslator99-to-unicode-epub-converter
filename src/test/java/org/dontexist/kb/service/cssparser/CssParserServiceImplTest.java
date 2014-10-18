package org.dontexist.kb.service.cssparser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dontexist.kb.util.FileUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.CollectionUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class CssParserServiceImplTest {

    private CssParserServiceImpl parserService = new CssParserServiceImpl();

    private static final String cssFile = "src/test/resources/stylesheet.css";
    
    @Before
    public void setup() {

    }

    @Test
    public void readElementsTest() throws Exception {
        String stylesheetText = FileUtils.readFileToString(new File(cssFile));
        System.out.println(stylesheetText);
        List<String> actual = parserService.readElements(stylesheetText);
        String[] expectedArray = {"bodySanskrit", "bodySanskrit1", "bodytext", "bodytext1", "bodytext2", "bodytext3", "bodytext4", "bodytext5", "bodytext6", "bodytext7", "bodytext8", "bodytext9", "bodytextnew", "calibre", "calibre1", "calibre2", "calibre3", "calibre4", "calibre5", "calibre6", "calibre7", "calibre8", "center", "center1", "center2", "center3", "gery", "italictext", "italictext1", "just", "just1", "just2", "just3", "lh", "mar", "right", "size"};
        List<String> expected = CollectionUtils.arrayToList(expectedArray);

        Assert.assertEquals(true, expected.containsAll(actual));
        Assert.assertEquals(true, actual.containsAll(expected));

    }

}
