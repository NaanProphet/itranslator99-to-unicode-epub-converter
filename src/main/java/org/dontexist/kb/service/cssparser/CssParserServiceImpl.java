package org.dontexist.kb.service.cssparser;

import com.sun.javafx.css.Rule;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.Stylesheet;
import com.sun.javafx.css.parser.CSSParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dontexist.kb.swing.TextEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Service for parsing data from CSS files (like stylesheet.css)
 */
@Service
public class CssParserServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(CssParserServiceImpl.class);

    /**
     * Reads CSS file and retuns a list of all the elements.
     *
     * @param stylesheetText the contents of a .css file read as one String
     * @return a list of all the elements found. e.g. for
     * <pre>
     * .just {
     * display: block;
     * text-align: justify
     * }
     * .just1 {
     * display: block;
     * font-size: 1em;
     * text-align: justify;
     * margin: 1em 0
     * }
     * </pre>
     * "just","just1" will be returned
     */
    public List<String> readElements(final String stylesheetText) {

        final CSSParser parser = CSSParser.getInstance();
        final Stylesheet stylesheet = parser.parse(stylesheetText);

        final List<String> cssElementNames = new ArrayList<>();
        for (final Rule cssRule : stylesheet.getRules()) {
            Selector cssElement = cssRule.getUnobservedSelectorList().get(0);
            String prependStringToRemove = "*.";
            String cssElementCleanName = StringUtils.substring(cssElement.toString(), prependStringToRemove.length());
            LOGGER.debug("Cleaned elementName from [{}] to [{}]", cssElement, cssElementCleanName);
            cssElementNames.add(cssElementCleanName);
        }

        return cssElementNames;

    }

    public String editFileText(final String originalFileContent) throws IOException {
        final CountDownLatch latch = new CountDownLatch(1);
        final TextEditor textEditor = new TextEditor(originalFileContent, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                latch.countDown();
            }
        });

        TextEditor.askUserToEditText(textEditor);
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new IllegalStateException("Interrupted while editing file!", e);
        }
        return textEditor.getSubmittedText();
    }

    public List<String> askForSelections(final String stylesheetText) {

        final List<String> cssElements = readElements(stylesheetText);
        // special thanks to:
        // http://stackoverflow.com/questions/8899605/multiple-choices-from-a-joptionpane
        JList<String> list = new JList(cssElements.toArray());
        JOptionPane.showMessageDialog(
                null, list, "Please select which css elements are currently used for Devanagari text", JOptionPane.PLAIN_MESSAGE);
        final List<String> selectedVals = list.getSelectedValuesList();
        LOGGER.debug("Selected values were: {}", selectedVals);
        return selectedVals;
    }


}
