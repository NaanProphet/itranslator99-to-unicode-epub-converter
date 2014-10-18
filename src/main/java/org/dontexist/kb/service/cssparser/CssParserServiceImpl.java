package org.dontexist.kb.service.cssparser;

import com.sun.javafx.css.Rule;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.Stylesheet;
import com.sun.javafx.css.parser.CSSParser;
import org.apache.commons.lang3.StringUtils;
import org.dontexist.kb.swing.TextDisplayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    public Set<String> askForCssSelections(final String stylesheetText) {

        final List<String> cssElements = readElements(stylesheetText);

        displayMessageDialog("In the following screen, the stylesheet.css file inside the eBook will appear. Please review the file and make a <b>mental note of all</b> the styles that apply to <b>custom Devanagari fonts only</b> (e.g. Sanskrit99), as they will be asked for shortly afterwards.");

        CountDownLatch latch = new CountDownLatch(1);
        TextDisplayer viewSheetDisplay = new TextDisplayer("stylesheet.css", stylesheetText, latch);
        TextDisplayer.displayFrame(viewSheetDisplay);
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        displayMessageDialog("Next, please select all the styles that apply to <b>custom Devanagari fonts only</b> using CTRL. After this, the Unicode conversion process will begin.");

        // special thanks to:
        // http://stackoverflow.com/questions/8899605/multiple-choices-from-a-joptionpane
        JList<String> list = new JList(cssElements.toArray());
        JOptionPane.showMessageDialog(
                null, list, "Which are Devanagari elements?",
                JOptionPane.QUESTION_MESSAGE);
        final List<String> selectedVals = list.getSelectedValuesList();
        LOGGER.debug("Selected values were: {}", selectedVals);

        // close CSS viewer
        viewSheetDisplay.closeFrame();

        // TODO this is a hack
        return new HashSet<String>(selectedVals);
    }

    public void displayMessageDialog(String s) {
        String html1 = "<html><body style='width: ";
        String html2 = "px'>";
        JOptionPane.showMessageDialog(null, new JLabel(html1+"300"+html2+s));
    }

    /**
     * Place a String on the clipboard, and make this class the
     * owner of the Clipboard's contents.
     */
    private void setClipboardContents(String aString){
        StringSelection stringSelection = new StringSelection(aString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }


}
