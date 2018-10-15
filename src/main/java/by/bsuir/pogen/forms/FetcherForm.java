package by.bsuir.pogen.forms;

import by.bsuir.pogen.constants.Constants;
import by.bsuir.pogen.utils.Http;
import by.bsuir.pogen.utils.IO;
import by.bsuir.pogen.utils.WebDriverHelper;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * Created by Alexei Khilchuk on 13.02.2017.
 */
public class FetcherForm extends JFrame {
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
    private JButton loadAppButton;
    private JTextField tbApk;
    private JTextField tbPackage;
    private JTextField tbActivity;

    private String pagePath;
    private RemoteWebDriver webDriver;
    private AndroidDriver androidDriver;

    public FetcherForm() {
        super("Fetch the page");

        btnAnalyze.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                analyzePage();
            }
        });

        fetchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkAccessabilityOfUrl();
            }
        });

        browseAFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseAFile();
            }
        });

        btnPreview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previewPage();

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
        loadAppButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAndroidAppAppium();
            }
        });
    }

    private void loadAndroidAppAppium() {
        try {
            System.setProperty("app", tbApk.getText());
            System.setProperty("appPackage", tbPackage.getText());
            System.setProperty("appActivity", tbActivity.getText());
            androidDriver = (AndroidDriver) WebDriverHelper.getWebDriver(Constants.BrowserType.ANDROID, null);
            lblSource.setText("(apk)");
            lblHtmlLoaded.setText(Constants.LoadStatus.LOADED.toString());
            lblHtmlLoaded.setForeground(Color.GREEN);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to load apk. \n" + e.getMessage());
        }
    }

    private void analyzePage() {
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

                setVisible(false);
                GeneratorForm generatorForm = new GeneratorForm(webDriver);
                generatorForm.setContentPane(generatorForm.mainPanel);
                generatorForm.setSize(500, 400);
                generatorForm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                generatorForm.setVisible(true);
                generatorForm.setResizable(true);
                generatorForm.setLocationRelativeTo(null);
                generatorForm.pack();
            }
        } else if (lblSource.getText().contains("apk")) {

            setVisible(false);
            GeneratorForm generatorForm = new GeneratorForm(androidDriver);
            generatorForm.setContentPane(generatorForm.mainPanel);
            generatorForm.setSize(500, 400);
            generatorForm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            generatorForm.setVisible(true);
            generatorForm.setResizable(true);
            generatorForm.setLocationRelativeTo(null);
            generatorForm.pack();
        } else {
            JOptionPane.showMessageDialog(null, "HTML was not loaded.");
        }
    }

    private void checkAccessabilityOfUrl() {
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

    private void browseAFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileFilter fileFilter = new FileNameExtensionFilter("HTML Page", "html");
        fileChooser.addChoosableFileFilter(fileFilter);
        fileChooser.setFileFilter(fileFilter);
        if (fileChooser.showOpenDialog(mainPanel) == JFileChooser.APPROVE_OPTION) {
            pagePath = fileChooser.getSelectedFile().getPath();
            if (IO.isPageReadable(new File(pagePath))) {
                lblHtmlLoaded.setText(Constants.LoadStatus.LOADED.toString());
                lblHtmlLoaded.setForeground(Color.GREEN);
                lblSource.setText("(file)");
                btnPreview.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(null, "Unable to access the file.");

            }
        }
    }

    private void previewPage() {
        if (lblHtmlLoaded.getText().equals(Constants.LoadStatus.LOADED.toString())) {
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
        mainPanel.setLayout(new GridLayoutManager(14, 8, new Insets(0, 0, 0, 0), -1, -1));
        btnAnalyze = new JButton();
        Font btnAnalyzeFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnAnalyze.getFont());
        if (btnAnalyzeFont != null) btnAnalyze.setFont(btnAnalyzeFont);
        btnAnalyze.setText("Analyze Source");
        mainPanel.add(btnAnalyze, new GridConstraints(12, 1, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 30), null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Segoe UI Semibold", -1, 18, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Using Web Page URL:");
        mainPanel.add(label1, new GridConstraints(0, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tbUrl = new JTextField();
        Font tbUrlFont = this.$$$getFont$$$("Segoe UI", -1, 16, tbUrl.getFont());
        if (tbUrlFont != null) tbUrl.setFont(tbUrlFont);
        mainPanel.add(tbUrl, new GridConstraints(1, 2, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 30), null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("Segoe UI Semibold", -1, 18, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Using Web Page .html file:");
        mainPanel.add(label2, new GridConstraints(2, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        browseAFileButton = new JButton();
        Font browseAFileButtonFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, browseAFileButton.getFont());
        if (browseAFileButtonFont != null) browseAFileButton.setFont(browseAFileButtonFont);
        browseAFileButton.setText("Browse a file");
        mainPanel.add(browseAFileButton, new GridConstraints(3, 2, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 30), null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$("Segoe UI Semibold", -1, 18, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Status:");
        mainPanel.add(label3, new GridConstraints(11, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(85, 16), null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(0, 7, 14, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(10, 10), null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(0, 0, 14, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(10, 10), new Dimension(9, 11), null, 0, false));
        final Spacer spacer3 = new Spacer();
        mainPanel.add(spacer3, new GridConstraints(13, 1, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(10, 10), null, null, 0, false));
        lblHtmlLoaded = new JLabel();
        lblHtmlLoaded.setForeground(new Color(-4521962));
        lblHtmlLoaded.setText("NOT LOADED");
        mainPanel.add(lblHtmlLoaded, new GridConstraints(11, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(70, 16), null, 0, false));
        btnPreview = new JButton();
        btnPreview.setEnabled(false);
        Font btnPreviewFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnPreview.getFont());
        if (btnPreviewFont != null) btnPreview.setFont(btnPreviewFont);
        btnPreview.setText("Preview");
        mainPanel.add(btnPreview, new GridConstraints(11, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(97, 30), null, 0, false));
        lblSource = new JLabel();
        Font lblSourceFont = this.$$$getFont$$$("Segoe UI Semibold", -1, 18, lblSource.getFont());
        if (lblSourceFont != null) lblSource.setFont(lblSourceFont);
        lblSource.setText("");
        mainPanel.add(lblSource, new GridConstraints(11, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), new Dimension(79, 0), null, 0, false));
        fetchButton = new JButton();
        Font fetchButtonFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, fetchButton.getFont());
        if (fetchButtonFont != null) fetchButton.setFont(fetchButtonFont);
        fetchButton.setText("Fetch");
        mainPanel.add(fetchButton, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(97, 30), null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$("Segoe UI Semibold", -1, 18, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Using Android Mobile App:");
        mainPanel.add(label4, new GridConstraints(5, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tbApk = new JTextField();
        Font tbApkFont = this.$$$getFont$$$("Segoe UI", -1, 16, tbApk.getFont());
        if (tbApkFont != null) tbApk.setFont(tbApkFont);
        tbApk.setText("/Users/alexei_khilchuk/taxi-android-passenger_develop-0.23.1-DEVELOP-12937-prod-release.apk");
        mainPanel.add(tbApk, new GridConstraints(6, 3, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 30), null, 0, false));
        tbActivity = new JTextField();
        Font tbActivityFont = this.$$$getFont$$$("Segoe UI", -1, 16, tbActivity.getFont());
        if (tbActivityFont != null) tbActivity.setFont(tbActivityFont);
        tbActivity.setText("com.multibrains.taxi.android.presentation.LauncherActivity");
        mainPanel.add(tbActivity, new GridConstraints(8, 3, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 30), null, 0, false));
        final JSeparator separator1 = new JSeparator();
        mainPanel.add(separator1, new GridConstraints(4, 1, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$("Segoe UI Semibold", -1, 18, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("Apk:");
        mainPanel.add(label5, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(85, 22), null, 0, false));
        tbPackage = new JTextField();
        Font tbPackageFont = this.$$$getFont$$$("Segoe UI", -1, 16, tbPackage.getFont());
        if (tbPackageFont != null) tbPackage.setFont(tbPackageFont);
        tbPackage.setText("com.multibrains.taxi.passenger.develop");
        mainPanel.add(tbPackage, new GridConstraints(7, 3, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 30), null, 0, false));
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$("Segoe UI Semibold", -1, 18, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setText("Package:");
        mainPanel.add(label6, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(85, 22), null, 0, false));
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$("Segoe UI Semibold", -1, 18, label7.getFont());
        if (label7Font != null) label7.setFont(label7Font);
        label7.setText("L. Activity:");
        mainPanel.add(label7, new GridConstraints(8, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(85, 22), null, 0, false));
        final JSeparator separator2 = new JSeparator();
        mainPanel.add(separator2, new GridConstraints(10, 1, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        loadAppButton = new JButton();
        Font loadAppButtonFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, loadAppButton.getFont());
        if (loadAppButtonFont != null) loadAppButton.setFont(loadAppButtonFont);
        loadAppButton.setText("Load App");
        mainPanel.add(loadAppButton, new GridConstraints(9, 2, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(97, 30), null, 0, false));
        label1.setLabelFor(tbUrl);
        label4.setLabelFor(tbUrl);
        label5.setLabelFor(tbUrl);
        label6.setLabelFor(tbUrl);
        label7.setLabelFor(tbUrl);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
