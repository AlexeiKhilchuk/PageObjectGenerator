package by.bsuir.pogen.forms;

import by.bsuir.pogen.constants.Constants;
import by.bsuir.pogen.utils.Http;
import by.bsuir.pogen.utils.WebDriverHelper;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.openqa.selenium.remote.RemoteWebDriver;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * Created by Alexei Khilchuk on 13.02.2017.
 */
public class MainForm extends JFrame {
    public JPanel mainPanel;
    private JTextField tbUrl;
    private JRadioButton rbLanguageJava;
    private JRadioButton rbLanguageCSharp;
    private JButton btnAnalyze;
    private JButton browseAFileButton;
    private JButton fetchButton;
    private JLabel lblHtmlLoaded;
    private JButton btnPreview;
    private JLabel lblSource;

    private String pagePath;
    private RemoteWebDriver webDriver;
    private ArrayList<Constants.ProgrammingLanguage> languageList = new ArrayList<Constants.ProgrammingLanguage>();

    public MainForm() {
        super("Fetch the page");

        btnAnalyze.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (lblSource.getText().contains("url") || lblSource.getText().contains("file")) {
                    if (webDriver != null) {
                        try {
                            webDriver.close();
                        } catch (Exception ignored) {
                        }
                        webDriver.quit();
                    }
                    if (lblHtmlLoaded.getText().equals(Constants.LoadStatus.LOADED.toString())) {
                        webDriver = WebDriverHelper.getWebDriver(Constants.BrowserType.CHROME, null);
                        if (lblSource.getText().contains("file")) {
                            webDriver.get("file:///" + pagePath);
                        } else if (lblSource.getText().contains("url")) {
                            webDriver.get(pagePath);
                        }
                    }

                    setVisible(false);
                    GeneratorForm generatorForm = new GeneratorForm(webDriver);
                    generatorForm.setContentPane(generatorForm.mainPanel);
                    generatorForm.setSize(500, 400);
                    generatorForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    generatorForm.setVisible(true);
                    generatorForm.setResizable(true);
                    generatorForm.setLocationRelativeTo(null);
                    generatorForm.pack();
                } else {
                    JOptionPane.showMessageDialog(null, "HTML was not loaded.");
                }
            }
        });

        fetchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String url = tbUrl.getText();
                if (!url.equals("")) {
                    if (Http.isPageExist(url)) {
                        lblHtmlLoaded.setText(Constants.LoadStatus.LOADED.toString());
                        lblHtmlLoaded.setForeground(Color.GREEN);
                        lblSource.setText("(url)");
                        pagePath = url;
                        btnPreview.setEnabled(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Page is not available.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please, enter a valid URL.");
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
                    pagePath = fileChooser.getSelectedFile().getPath();
                    lblHtmlLoaded.setText(Constants.LoadStatus.LOADED.toString());
                    lblHtmlLoaded.setForeground(Color.GREEN);
                    lblSource.setText("(file)");
                    btnPreview.setEnabled(true);
                }
            }
        });

        btnPreview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lblHtmlLoaded.getText() == Constants.LoadStatus.LOADED.toString()) {
                    webDriver = WebDriverHelper.getWebDriver(Constants.BrowserType.CHROME, null);
                    if (lblSource.getText().contains("file")) {
                        webDriver.get("file:///" + pagePath);
                    } else if (lblSource.getText().contains("url")) {
                        webDriver.get(pagePath);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "HTML was not loaded.");
                }

            }
        });

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (webDriver != null) {
                    webDriver.close();
                    webDriver.quit();
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
        mainPanel.setLayout(new GridLayoutManager(8, 7, new Insets(0, 0, 0, 0), -1, -1));
        btnAnalyze = new JButton();
        btnAnalyze.setFont(new Font("Segoe UI Light", btnAnalyze.getFont().getStyle(), 18));
        btnAnalyze.setText("Analyze Page");
        mainPanel.add(btnAnalyze, new GridConstraints(6, 1, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 30), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setFont(new Font("Segoe UI Semibold", label1.getFont().getStyle(), 18));
        label1.setText("Using URL:");
        mainPanel.add(label1, new GridConstraints(0, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tbUrl = new JTextField();
        tbUrl.setFont(new Font("Segoe UI", tbUrl.getFont().getStyle(), 16));
        mainPanel.add(tbUrl, new GridConstraints(1, 2, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 30), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setFont(new Font("Segoe UI Semibold", label2.getFont().getStyle(), 18));
        label2.setText("Using .html file:");
        mainPanel.add(label2, new GridConstraints(2, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        browseAFileButton = new JButton();
        browseAFileButton.setFont(new Font("Segoe UI Light", browseAFileButton.getFont().getStyle(), 18));
        browseAFileButton.setText("Browse a file");
        mainPanel.add(browseAFileButton, new GridConstraints(3, 2, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 30), null, 0, false));
        fetchButton = new JButton();
        fetchButton.setFont(new Font("Segoe UI Light", fetchButton.getFont().getStyle(), 18));
        fetchButton.setText("Fetch");
        mainPanel.add(fetchButton, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(101, 30), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setFont(new Font("Segoe UI Semibold", label3.getFont().getStyle(), 18));
        label3.setText("Status:");
        mainPanel.add(label3, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(39, 16), null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(0, 6, 8, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(10, 10), null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(0, 0, 8, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(10, 10), new Dimension(9, 11), null, 0, false));
        final Spacer spacer3 = new Spacer();
        mainPanel.add(spacer3, new GridConstraints(7, 1, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(10, 10), null, null, 0, false));
        lblHtmlLoaded = new JLabel();
        lblHtmlLoaded.setForeground(new Color(-4521962));
        lblHtmlLoaded.setText("NOT LOADED");
        mainPanel.add(lblHtmlLoaded, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(70, 16), null, 0, false));
        btnPreview = new JButton();
        btnPreview.setEnabled(false);
        btnPreview.setFont(new Font("Segoe UI Light", btnPreview.getFont().getStyle(), 18));
        btnPreview.setText("Preview");
        mainPanel.add(btnPreview, new GridConstraints(5, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(101, 30), null, 0, false));
        lblSource = new JLabel();
        lblSource.setFont(new Font("Segoe UI Semibold", lblSource.getFont().getStyle(), 18));
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
