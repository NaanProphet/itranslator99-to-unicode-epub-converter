/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.dontexist.kb;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Simple GUI selector for choosing the type of ePUB conversion. Modeled after the <a
 * href="http://docs.oracle.com/javase/tutorial/uiswing/components/html.html">ButtonHtmlDemo Java example</a>.
 * <p>
 * To trigger the GUI prompt, simply call {@link #displayDialog(java.awt.event.ActionListener)} with your listener to
 * receive the button press event. The {@link java.awt.event.ActionEvent#getActionCommand()} will be the name of the
 * {@link EpubSelectGui.PromptOptions} enum pressed, and can be looked up using {@link
 * EpubSelectGui.PromptOptions#valueOf(String)} without any fear of null pointers.
 * </p>
 * <p>
 * Note: the dialog will automatically close after the first button is pressed.
 * </p>
 */
public class EpubSelectGui extends JPanel implements ActionListener {

    public static final String WINDOW_TITLE = "Epub Unicode Converter v0.1";
    private static JFrame frame;
    private final ActionListener outsideListener;
    private JButton b1 = new JButton(), b2 = new JButton(), b3 = new JButton();

    private EpubSelectGui(final ActionListener outsideListener) {
        this.outsideListener = outsideListener;
        setupButton(b1, PromptOptions.SINGLE_FILE);
        setupButton(b2, PromptOptions.ALL_FILES_IN_A_SINGLE_FOLDER);
        setupButton(b3, PromptOptions.ALL_FILES_IN_FOLDER_RECURSIVELY);
    }

    /**
     * Create the GUI and show it.  For thread safety, this method should be invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI(final ActionListener outsideListener) {

        //Create and set up the window.
        frame = new JFrame(WINDOW_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        EpubSelectGui newContentPane = new EpubSelectGui(outsideListener);
        //content panes must be opaque
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * @param outsideListener your listener to receive the event. Can be null if no listener required.
     */
    public static void displayDialog(final ActionListener outsideListener) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(outsideListener);
            }
        });
    }

    private void setupButton(JButton button, PromptOptions option) {
        button.setText(option.getDisplayText());
        button.setMnemonic(option.getKeyMnemonic());
        button.setActionCommand(option.getActionCommand());
        button.setToolTipText(option.getTooltipText());

        // Listen for actions on this button
        button.addActionListener(this);
        button.addActionListener(outsideListener);

        //Add Components to this container, using the default FlowLayout.
        add(button);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final String actionCommand = e.getActionCommand();
        frame.dispose();
        PromptOptions optionSelected = PromptOptions.valueOf(actionCommand);
        System.out.println(optionSelected);
    }

    public enum PromptOptions {
        SINGLE_FILE(
                "Convert a single ePUB file",
                KeyEvent.VK_S,
                "Use this option to convert a single ePUB file."),
        ALL_FILES_IN_A_SINGLE_FOLDER(
                "Convert all ePUB files inside a folder",
                KeyEvent.VK_A,
                "Use this option to convert all ePUB files in a single folder."),
        ALL_FILES_IN_FOLDER_RECURSIVELY(
                "Convert all ePUB files inside a folder (recursive)",
                KeyEvent.VK_R,
                "Use this option to recursively drilldown and convert all ePUBs in the selected folder.");

        private final String displayText;
        private final int keyMnemonic;
        private final String actionCommand;
        private final String tooltipText;


        PromptOptions(final String displayText, final int keyMnemonic,
                      final String tooltipText) {
            this.displayText = displayText;
            this.keyMnemonic = keyMnemonic;
            this.actionCommand = this.name();
            this.tooltipText = tooltipText;
        }


        public String getDisplayText() {
            return displayText;
        }

        public int getKeyMnemonic() {
            return keyMnemonic;
        }

        public String getActionCommand() {
            return actionCommand;
        }

        public String getTooltipText() {
            return tooltipText;
        }
    }


}

