package org.dontexist.kb.service.cssparser;

import com.sun.javafx.css.Rule;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.Stylesheet;
import com.sun.javafx.css.parser.CSSParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for parsing data from CSS files (like stylesheet.css)
 */
@Service
public class CssParserServiceImpl {

    public List<String> readElements(String stylesheetText) {

        CSSParser parser = CSSParser.getInstance();
        System.out.println("parser is " + parser);
        Stylesheet stylesheet = parser.parse(stylesheetText);

        final List<String> cssElementNames = new ArrayList<>();
        for(Rule cssRule : stylesheet.getRules()) {
            Selector cssElement = cssRule.getUnobservedSelectorList().get(0);
            String prependStringToRemove = "*.";
            String cssElementCleanName = StringUtils.substring(cssElement.toString(), prependStringToRemove.length());
            // TODO change to logger
            System.out.println(cssElementCleanName);
            cssElementNames.add(cssElementCleanName);
        }

        return cssElementNames;

    }

}
