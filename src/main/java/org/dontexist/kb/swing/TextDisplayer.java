package org.dontexist.kb.swing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

/**
 * Modeled after {@link org.dontexist.kb.swing.TextEditor}, but only displays non-editable text.
 */
public class TextDisplayer extends JFrame {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextDisplayer.class);

    private static final String DEFAULT_TEXT_AREA_TEXT = "";
    private static final int DEFAULT_WINDOW_HEIGHT = 30;
    private static final int DEFAULT_WINDOW_WIDTH = 70;
    private static final Optional<CountDownLatch> NO_OUTSIDE_LISTENER = Optional.<CountDownLatch>empty();
    private final String textFrameText;

    private JTextArea textArea;

    public TextDisplayer() {
        this.textFrameText = DEFAULT_TEXT_AREA_TEXT;
        init(NO_OUTSIDE_LISTENER);
    }

    public TextDisplayer(final String windowTitle, final String textFrameText) {
        super(windowTitle);
        this.textFrameText = textFrameText;
        init(NO_OUTSIDE_LISTENER);
    }

    public TextDisplayer(final String windowTitle, final String textFrameText, final CountDownLatch outsideListener) {
        super(windowTitle);
        this.textFrameText = textFrameText;
        init(Optional.of(outsideListener));
    }

    private void init(final Optional<CountDownLatch> optionalOutsideListener) {
        textArea = new JTextArea(textFrameText, DEFAULT_WINDOW_HEIGHT, DEFAULT_WINDOW_WIDTH);
        add(new JScrollPane(textArea));
        add(new JPanel() {{
            final JButton button = new JButton(new AbstractAction("Continue") {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    optionalOutsideListener.ifPresent(l -> l.countDown());
                }
            });

            add(button);
        }}, BorderLayout.SOUTH);
    }

    /**
     * Create the GUI and show it.  For thread safety, this method should be invoked from the event dispatch thread.
     */
    private static void createAndShowGui(final TextDisplayer frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.toFront();
    }

    public static void main(String[] args) {
        displayFrame(new TextDisplayer());

    }

    public static void displayFrame(final TextDisplayer textEditor) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui(textEditor);
            }
        });
    }

    public void closeFrame() {
        dispose();
    }

}