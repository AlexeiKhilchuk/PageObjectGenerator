package by.bsuir.pogen.forms;

import by.bsuir.pogen.constants.Constants;
import by.bsuir.pogen.utils.Http;
import by.bsuir.pogen.utils.IO;
import by.bsuir.pogen.utils.PageObjectGenerator;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Alexei Khilchuk on 13.02.2017.
 */
public class MainForm extends JFrame {
    public JPanel mainPanel;
    private JTextField tbUrl;
    private JRadioButton rbLanguageJava;
    private JRadioButton rbLanguageCSharp;
    private JButton btnGenerate;
    private JButton browseAFileButton;
    private JButton fetchButton;
    private JLabel lblHtmlLoaded;
    private JButton btnPreview;
    private JLabel lblSource;

    private String htmlCode;
    private ArrayList<Constants.ProgrammingLanguage> languageList;

    public MainForm() {
        super("MainForm");

        btnGenerate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                languageList.clear();
                if (rbLanguageJava.isSelected()) languageList.add(Constants.ProgrammingLanguage.JAVA);
                if (rbLanguageCSharp.isSelected()) languageList.add(Constants.ProgrammingLanguage.C_SHARP);

                for (Constants.ProgrammingLanguage language: languageList){
                    //TODO: save dialog
                    PageObjectGenerator.generateClass(htmlCode, language);
                }


            }
        });
        fetchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String url = tbUrl.getText();
                try {
                    htmlCode = Http.getUrlSource(url);
                    lblHtmlLoaded.setText(Constants.LoadStatus.LOADED.toString());
                    lblHtmlLoaded.setForeground(Color.GREEN);
                    lblSource.setText("(url)");
                    btnPreview.setEnabled(true);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage());
                    btnPreview.setEnabled(false);
                    lblHtmlLoaded.setText(Constants.LoadStatus.NOT_LOADED.toString());
                    lblHtmlLoaded.setForeground(Color.RED);
                    lblSource.setText("");
                }
            }
        });
        browseAFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileFilter fileFilter = new FileNameExtensionFilter("HTML Page", "html");
                fileChooser.addChoosableFileFilter(fileFilter);
                fileChooser.setFileFilter(fileFilter);
                if (fileChooser.showOpenDialog(mainPanel) == JFileChooser.APPROVE_OPTION) {
                    try {
                        htmlCode = IO.getFileSource(fileChooser.getSelectedFile());
                        lblHtmlLoaded.setText(Constants.LoadStatus.LOADED.toString());
                        lblHtmlLoaded.setForeground(Color.GREEN);
                        lblSource.setText("(file)");
                        btnPreview.setEnabled(true);
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage());
                        btnPreview.setEnabled(false);
                        lblHtmlLoaded.setText(Constants.LoadStatus.NOT_LOADED.toString());
                        lblHtmlLoaded.setForeground(Color.RED);
                        lblSource.setText("");
                    }
                    // load from file
                }
            }
        });
        btnPreview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lblHtmlLoaded.getText() == Constants.LoadStatus.LOADED.toString()) {
                    PreviewForm previewForm = new PreviewForm();
                    previewForm.setContentPane(previewForm.mainPanel);
                    previewForm.setSize(700, 500);
                    previewForm.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                    previewForm.setVisible(true);
                    previewForm.setResizable(true);
                    previewForm.setLocationRelativeTo(null);
                    previewForm.editorPane.setEditable(false);
                    previewForm.editorPane.setContentType("text/html");
                    previewForm.editorPane.setText(htmlCode);
                    previewForm.pack();
                } else {
                    JOptionPane.showMessageDialog(null, "HTML was not loaded.");
                }

            }
        });

    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(10, 7, new Insets(0, 0, 0, 0), -1, -1));
        rbLanguageJava = new JRadioButton();
        rbLanguageJava.setText("Java");
        mainPanel.add(rbLanguageJava, new GridConstraints(6, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnGenerate = new JButton();
        btnGenerate.setText("Generate");
        mainPanel.add(btnGenerate, new GridConstraints(8, 1, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Using URL:");
        mainPanel.add(label1, new GridConstraints(0, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tbUrl = new JTextField();
        mainPanel.add(tbUrl, new GridConstraints(1, 2, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Using .html file:");
        mainPanel.add(label2, new GridConstraints(2, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        browseAFileButton = new JButton();
        browseAFileButton.setText("Browse a file");
        mainPanel.add(browseAFileButton, new GridConstraints(3, 2, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rbLanguageCSharp = new JRadioButton();
        rbLanguageCSharp.setText("C#");
        mainPanel.add(rbLanguageCSharp, new GridConstraints(7, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fetchButton = new JButton();
        fetchButton.setText("Fetch");
        mainPanel.add(fetchButton, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(101, 32), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Status:");
        mainPanel.add(label3, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(39, 16), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("HTML code of the page");
        mainPanel.add(label4, new GridConstraints(0, 1, 6, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Target Programming language");
        mainPanel.add(label5, new GridConstraints(6, 1, 2, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(0, 6, 10, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(10, 10), null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(0, 0, 10, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(10, 10), new Dimension(9, 11), null, 0, false));
        final Spacer spacer3 = new Spacer();
        mainPanel.add(spacer3, new GridConstraints(9, 1, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(10, 10), null, null, 0, false));
        lblHtmlLoaded = new JLabel();
        lblHtmlLoaded.setForeground(new Color(-4521962));
        lblHtmlLoaded.setText("NOT LOADED");
        mainPanel.add(lblHtmlLoaded, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(70, 16), null, 0, false));
        btnPreview = new JButton();
        btnPreview.setEnabled(false);
        btnPreview.setText("Preview");
        mainPanel.add(btnPreview, new GridConstraints(5, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(101, 32), null, 0, false));
        lblSource = new JLabel();
        lblSource.setText("");
        mainPanel.add(lblSource, new GridConstraints(5, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), new Dimension(38, 0), null, 0, false));
        label1.setLabelFor(tbUrl);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
