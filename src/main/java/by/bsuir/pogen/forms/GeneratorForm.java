package by.bsuir.pogen.forms;

import by.bsuir.pogen.constants.Constants;
import by.bsuir.pogen.models.WebElement;
import by.bsuir.pogen.models.WebElementNode;
import by.bsuir.pogen.template.CSharpClassTemplate;
import by.bsuir.pogen.template.JavaClassTemplate;
import by.bsuir.pogen.utils.LocatorBuilder;
import by.bsuir.pogen.utils.WebDriverHelper;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import io.appium.java_client.android.AndroidDriver;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.watertemplate.Template;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

import static by.bsuir.pogen.constants.Constants.GENERATION_IN_PROGRESS;

/**
 * Created by Alexei Khilchuk on 13.02.2017.
 */
public class GeneratorForm extends JFrame {
    static Logger LOG = Logger.getLogger(GeneratorForm.class.getName());
    public JPanel mainPanel;
    private LocatorBuilder locatorBuilder = new LocatorBuilder();
    private ArrayList listOfPrepearedElements = new ArrayList<WebElement>();
    private JTree trWebElements;
    private JTextField tbElementName;
    private JTextField tbName;
    private JTextField tbTagName;
    private JTextField tbLinkText;
    private JTextField tbCss;
    private JTextField tbXpath;
    private JButton btnGenerateNameLocator;
    private JButton btnGenerateTagNameLocator;
    private JButton btnGenerateLinkTextLocator;
    private JButton btnGenerateCssLocator;
    private JButton btnGenerateXpathLocator;
    private JButton btnSaveNameLocator;
    private JButton btnSaveTagNameLocator;
    private JButton btnSaveLinkTextLocator;
    private JButton btnSaveCssLocator;
    private JButton btnSaveXpathLocator;
    private JButton btnSaveElementName;
    private JButton btnGenerateAllLocators;
    private JComboBox cbPrefferedLocator;
    private JButton btnSavePrefferedLocator;
    private JButton btnSetElementReady;
    private JButton btnValidatePrefferedLocator;
    private JButton btnShowOnPage;
    private JPanel pnlWebElementProps;
    private JButton btnGenerateLocatorsForElement;
    private JButton btnSaveAllLocators;
    private JLabel lblReadyForGeneration;
    private JButton btnSetElementUnready;
    private JTextField tbId;
    private JButton btnGenerateId;
    private JTextField tbClassName;
    private JButton btnGenerateClassNameLocator;
    private JLabel lblLocatorActions;
    private JButton btnGenerateClass;
    private JCheckBox chbGenerateMethods;
    private JComboBox cbProgrammingLanguage;
    private JButton btnOpenTree;
    private JButton btnSaveTree;
    private JLabel lblLinkText;
    private JLabel lblCss;
    private RemoteWebDriver webDriver;
    private WebElementNode htmlTree;
    private WebElement selectedElement;

    GeneratorForm(RemoteWebDriver currentWebDriver) {
        super("Page Object Generator: " + ((currentWebDriver instanceof AndroidDriver) ? "Mobile Activity" : currentWebDriver.getTitle()));
        webDriver = currentWebDriver;
        $$$setupUI$$$();
        Document doc = Jsoup.parse(webDriver.getPageSource());
        if (!(webDriver instanceof AndroidDriver)) {
            webDriver.manage().window().setPosition(new Point(-2000, 0));
            htmlTree = new WebElementNode(new WebElement(doc.body()));
        } else {
            tbLinkText.setVisible(false);
            btnSaveLinkTextLocator.setVisible(false);
            lblLinkText.setVisible(false);
            tbCss.setVisible(false);
            btnGenerateCssLocator.setVisible(false);
            lblCss.setVisible(false);
            htmlTree = new WebElementNode(new WebElement(doc.body().child(0)));
        }


        this.trWebElements.setModel(new DefaultTreeModel(htmlTree));
        this.cbPrefferedLocator.setModel(new DefaultComboBoxModel(Constants.LocatorType.values()));
        this.cbProgrammingLanguage.setModel(new DefaultComboBoxModel(Constants.ProgrammingLanguage.values()));
        this.trWebElements.setSelectionRow(0);
        this.trWebElements.setSelectionPath(trWebElements.getSelectionPath());

        /////////////////////
        btnSaveNameLocator.setVisible(false);
        btnSaveTagNameLocator.setVisible(false);
        btnSaveLinkTextLocator.setVisible(false);
        btnSaveCssLocator.setVisible(false);
        btnSaveXpathLocator.setVisible(false);
        btnSavePrefferedLocator.setVisible(false);

        lblLocatorActions.setVisible(false);
        btnGenerateNameLocator.setVisible(false);
        btnGenerateTagNameLocator.setVisible(false);
        btnGenerateLinkTextLocator.setVisible(false);
        btnGenerateCssLocator.setVisible(false);
        btnGenerateXpathLocator.setVisible(false);
        btnGenerateId.setVisible(false);
        btnGenerateClassNameLocator.setVisible(false);
        /////////////////////

        centerFrame();

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (webDriver != null) {
                    if (!(webDriver instanceof AndroidDriver)) {
                        webDriver.close();
                    }
                    webDriver.quit();
                }
            }
        });

        trWebElements.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                if (trWebElements.isSelectionEmpty()) {
                    selectedElement = null;
                    return;
                }
                TreePath treePath = trWebElements.getSelectionPath();
                DefaultMutableTreeNode mutableTreeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
                selectedElement = (WebElement) mutableTreeNode.getUserObject();
                tbElementName.setText(selectedElement.getElementName());
                tbName.setText(selectedElement.getNameLocator());
                tbId.setText(selectedElement.getIdLocator());
                tbClassName.setText(selectedElement.getClassNameLocator());
                tbTagName.setText(selectedElement.getTagNameLocator());
                tbLinkText.setText(selectedElement.getLinkTextLocator());
                tbCss.setText(selectedElement.getCssLocator());
                tbXpath.setText(selectedElement.getXpathLocator());
                cbPrefferedLocator.setSelectedItem(selectedElement.getPreferredLocatorType());
                lblReadyForGeneration.setText(selectedElement.isForGeneration() ? "Ready" : "Unready");
            }
        });
        btnGenerateNameLocator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedElement != null) {
                    tbName.setText(locatorBuilder.getNameLocator(selectedElement));
                }
            }
        });
        btnGenerateTagNameLocator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedElement != null) {
                    tbTagName.setText(locatorBuilder.getTagNameLocator(selectedElement));
                }
            }
        });
        btnGenerateLinkTextLocator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedElement != null) {
                    tbLinkText.setText(locatorBuilder.getLinkTextLocator(selectedElement));
                }
            }
        });
        btnGenerateId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedElement != null) {
                    tbId.setText(locatorBuilder.getIdLocator(selectedElement));
                }
            }
        });
        btnGenerateClassNameLocator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedElement != null) {
                    tbClassName.setText(locatorBuilder.getClassNameLocator(selectedElement));
                }
            }
        });
        btnGenerateCssLocator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedElement != null) {
                    tbCss.setText(locatorBuilder.getCssLocator(selectedElement));
                }
            }
        });
        btnGenerateXpathLocator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedElement != null) {
                    tbXpath.setText(locatorBuilder.getXpathLocator(selectedElement, webDriver));
                }
            }
        });
        btnGenerateAllLocators.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread t1 = new Thread(new Runnable() {
                    public void run() {
                        long tStart = System.currentTimeMillis();
                        LocatorBuilder builder = new LocatorBuilder();
                        builder.generateLocatorsForNodes(((WebElementNode) trWebElements.getModel().getRoot()), webDriver);
                        long tEnd = System.currentTimeMillis();
                        long tDelta = tEnd - tStart;
                        double elapsedSeconds = tDelta / 1000.0;
                        JOptionPane.showMessageDialog(null, String.format("Elements locators were generated in %s seconds.", elapsedSeconds));
                        GeneratorForm.super.setTitle(GeneratorForm.super.getTitle().replace(GENERATION_IN_PROGRESS, ""));
                    }
                });
                t1.start();
                JOptionPane.showMessageDialog(null, "Background Locators generation in progress...");
                GeneratorForm.super.setTitle(GeneratorForm.super.getTitle() + GENERATION_IN_PROGRESS);
            }

        });
        btnSaveElementName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedElement != null) {
                    if (!tbElementName.getText().equals("")) {
                        selectedElement.setElementName(tbElementName.getText());
                        trWebElements.repaint();

                    } else {
                        JOptionPane.showMessageDialog(null, "Element name can not be empty.");
                        tbElementName.setText(selectedElement.getElementName());
                    }

                }
            }
        });
        btnSaveNameLocator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedElement != null) {
                    selectedElement.setNameLocator(tbName.getText());
                    trWebElements.repaint();
                    JOptionPane.showMessageDialog(null, "Name locator was saved as ' " + tbName.getText() + " '");
                }
            }
        });
        btnSaveTagNameLocator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedElement != null) {
                    selectedElement.setTagNameLocator(tbTagName.getText());
                    trWebElements.repaint();
                    JOptionPane.showMessageDialog(null, "Tag Name locator was saved as ' " + tbTagName.getText() + " '");
                }
            }
        });
        btnSaveLinkTextLocator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedElement != null) {
                    selectedElement.setLinkTextLocator(tbLinkText.getText());
                    trWebElements.repaint();
                    JOptionPane.showMessageDialog(null, "Link Text locator was saved as ' " + tbLinkText.getText() + " '");
                }
            }
        });
        btnSaveCssLocator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedElement != null) {
                    selectedElement.setCssLocator(tbCss.getText());
                    trWebElements.repaint();
                    JOptionPane.showMessageDialog(null, "CSS locator was saved as ' " + tbCss.getText() + " '");
                }
            }
        });
        btnSaveXpathLocator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedElement != null) {
                    selectedElement.setXpathLocator(tbXpath.getText());
                    trWebElements.repaint();
                    JOptionPane.showMessageDialog(null, "XPath locator was saved as ' " + tbXpath.getText() + " '");
                }
            }
        });
        btnSavePrefferedLocator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedElement != null) {
                    selectedElement.setPreferredLocatorType((Constants.LocatorType) cbPrefferedLocator.getSelectedItem());
                    JOptionPane.showMessageDialog(null, "Preferred locator for generation was saved as ' " + ((Constants.LocatorType) cbPrefferedLocator.getSelectedItem()).toString() + " '");

                }
            }
        });
        btnSetElementReady.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedElement != null) {
                    selectedElement.setForGeneration(true);
                    trWebElements.repaint();

                    selectedElement.setNameLocator(tbName.getText());
                    selectedElement.setTagNameLocator(tbTagName.getText());
                    selectedElement.setIdLocator(tbId.getText());
                    selectedElement.setClassNameLocator(tbClassName.getText());
                    selectedElement.setLinkTextLocator(tbLinkText.getText());
                    selectedElement.setCssLocator(tbCss.getText());
                    selectedElement.setXpathLocator(tbXpath.getText());
                    selectedElement.setPreferredLocatorType((Constants.LocatorType) cbPrefferedLocator.getSelectedItem());
                    trWebElements.repaint();

                    validateSelectedElement();
                    lblReadyForGeneration.setText("Ready");
                    if (listOfPrepearedElements.indexOf(selectedElement) == -1)
                        listOfPrepearedElements.add(selectedElement);
                    JOptionPane.showMessageDialog(null, "Element will be included into generated code with preferred locator.");

                }
            }
        });
        btnSetElementUnready.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedElement != null) {
                    selectedElement.setForGeneration(false);
                    trWebElements.repaint();
                    lblReadyForGeneration.setText("Unready");
                    if (listOfPrepearedElements.indexOf(selectedElement) != -1)
                        listOfPrepearedElements.remove(selectedElement);
                    JOptionPane.showMessageDialog(null, "Element will not be included into generated code.");

                }
            }
        });
        btnGenerateLocatorsForElement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedElement != null) {
                    tbName.setText(locatorBuilder.getNameLocator(selectedElement));
                    tbId.setText(locatorBuilder.getIdLocator(selectedElement));
                    tbClassName.setText(locatorBuilder.getClassNameLocator(selectedElement));
                    tbTagName.setText(locatorBuilder.getTagNameLocator(selectedElement));
                    tbLinkText.setText(locatorBuilder.getLinkTextLocator(selectedElement));
                    tbCss.setText(locatorBuilder.getCssLocator(selectedElement));
                    tbXpath.setText(locatorBuilder.getXpathLocator(selectedElement, webDriver));
                    trWebElements.repaint();
                    JOptionPane.showMessageDialog(null, "Locators were saved.");
                }
            }
        });
        btnSaveAllLocators.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedElement != null) {
                    selectedElement.setNameLocator(tbName.getText());
                    selectedElement.setTagNameLocator(tbTagName.getText());
                    selectedElement.setIdLocator(tbId.getText());
                    selectedElement.setClassNameLocator(tbClassName.getText());
                    selectedElement.setLinkTextLocator(tbLinkText.getText());
                    selectedElement.setCssLocator(tbCss.getText());
                    selectedElement.setXpathLocator(tbXpath.getText());
                    selectedElement.setPreferredLocatorType((Constants.LocatorType) cbPrefferedLocator.getSelectedItem());
                    trWebElements.repaint();
                    JOptionPane.showMessageDialog(null, "Locators and preferred locator were saved.");
                }
            }
        });
        btnValidatePrefferedLocator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateSelectedElement();
            }
        });


        btnShowOnPage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedElement != null) {
                    if (!(webDriver instanceof AndroidDriver)) {
                        Constants.LocatorType type = (Constants.LocatorType) cbPrefferedLocator.getSelectedItem();
                        org.openqa.selenium.WebElement element = null;
                        boolean isHandled = false;
                        try {
                            String currentLocatorValue = new String();
                            switch (type) {
                                case ID: {
                                    currentLocatorValue = tbId.getText();
                                    if (currentLocatorValue.equals("") || currentLocatorValue.equals(" ")) {
                                        JOptionPane.showMessageDialog(null, String.format("%s is empty.", type.toString()));
                                    } else {
                                        element = webDriver.findElement(By.id(currentLocatorValue));
                                        isHandled = true;
                                    }
                                    break;
                                }
                                case NAME: {
                                    currentLocatorValue = tbName.getText();
                                    if (currentLocatorValue.equals("") || currentLocatorValue.equals(" ")) {
                                        JOptionPane.showMessageDialog(null, String.format("%s is empty.", type.toString()));
                                    } else {
                                        element = webDriver.findElement(By.name(currentLocatorValue));
                                        isHandled = true;
                                    }
                                    break;
                                }
                                case CLASS_NAME: {
                                    currentLocatorValue = tbClassName.getText();
                                    if (currentLocatorValue.equals("") || currentLocatorValue.equals(" ")) {
                                        JOptionPane.showMessageDialog(null, String.format("%s is empty.", type.toString()));
                                    } else {
                                        element = webDriver.findElement(By.className(currentLocatorValue));
                                        isHandled = true;
                                    }

                                    break;
                                }
                                case LINK_TEXT: {
                                    currentLocatorValue = tbLinkText.getText();
                                    if (currentLocatorValue.equals("") || currentLocatorValue.equals(" ")) {
                                        JOptionPane.showMessageDialog(null, String.format("%s is empty.", type.toString()));
                                    } else {
                                        element = webDriver.findElement(By.linkText(currentLocatorValue));
                                        isHandled = true;
                                    }

                                    break;
                                }
                                case TAG_NAME: {
                                    currentLocatorValue = tbTagName.getText();
                                    if (currentLocatorValue.equals("") || currentLocatorValue.equals(" ")) {
                                        JOptionPane.showMessageDialog(null, String.format("%s is empty.", type.toString()));
                                    } else {
                                        element = webDriver.findElement(By.tagName(currentLocatorValue));
                                        isHandled = true;
                                    }
                                    break;
                                }
                                case CSS: {
                                    currentLocatorValue = tbCss.getText();
                                    if (currentLocatorValue.equals("") || currentLocatorValue.equals(" ")) {
                                        JOptionPane.showMessageDialog(null, String.format("%s is empty.", type.toString()));
                                    } else {
                                        element = webDriver.findElement(By.cssSelector(currentLocatorValue));
                                        isHandled = true;
                                    }
                                    break;
                                }
                                case XPATH: {
                                    currentLocatorValue = tbXpath.getText();
                                    if (currentLocatorValue.equals("") || currentLocatorValue.equals(" ")) {
                                        JOptionPane.showMessageDialog(null, String.format("%s is empty.", type.toString()));
                                    } else {
                                        element = webDriver.findElement(By.xpath(currentLocatorValue));
                                        isHandled = true;
                                    }
                                    break;
                                }
                            }
                            if (isHandled) {
                                webDriver.manage().window().maximize();
                                WebDriverHelper.highlightElement(element, webDriver);

                            }
                        } catch (Exception ex) {
                            int index = ex.getMessage().indexOf("\n");
                            index = ex.getMessage().indexOf("\n", index);
                            JOptionPane.showMessageDialog(null, "Selenium has thrown Exception during find element by current preferred locator: \n"
                                    + ex.getMessage().substring(0, index));
                        }
                    } else {
                        AndroidDriver androidDriver = (AndroidDriver) webDriver;
                        LayoutForm layoutForm = new LayoutForm(androidDriver, selectedElement);
                        layoutForm.setContentPane(layoutForm.pnlLayout);
                        layoutForm.setSize(480, 720);
                        layoutForm.setVisible(true);
                        layoutForm.setResizable(true);
                        layoutForm.setLocationRelativeTo(null);
                        layoutForm.pack();
                    }
                }
            }
        });
        tbName.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (selectedElement != null) {
                        tbName.setText(locatorBuilder.getNameLocator(selectedElement));
                    }
                }
            }
        });
        tbId.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (selectedElement != null) {
                        tbId.setText(locatorBuilder.getIdLocator(selectedElement));
                    }
                }
            }
        });
        tbClassName.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (selectedElement != null) {
                        tbClassName.setText(locatorBuilder.getClassNameLocator(selectedElement));
                    }
                }
            }
        });
        tbTagName.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (selectedElement != null) {
                        tbTagName.setText(locatorBuilder.getTagNameLocator(selectedElement));
                    }
                }
            }
        });
        tbLinkText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (selectedElement != null) {
                        tbLinkText.setText(locatorBuilder.getLinkTextLocator(selectedElement));
                    }
                }
            }
        });
        tbCss.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (selectedElement != null) {
                        tbCss.setText(locatorBuilder.getCssLocator(selectedElement));
                    }
                }
            }
        });
        tbXpath.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (selectedElement != null) {
                        tbXpath.setText(locatorBuilder.getXpathLocator(selectedElement, webDriver));
                    }
                }
            }
        });


        trWebElements.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (selectedElement != null) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        selectedElement.setForGeneration(!selectedElement.isForGeneration());
                        trWebElements.repaint();

                        if (selectedElement.isForGeneration()) {
                            selectedElement.setForGeneration(true);
                            trWebElements.repaint();

                            selectedElement.setNameLocator(tbName.getText());
                            selectedElement.setTagNameLocator(tbTagName.getText());
                            selectedElement.setIdLocator(tbId.getText());
                            selectedElement.setClassNameLocator(tbClassName.getText());
                            selectedElement.setLinkTextLocator(tbLinkText.getText());
                            selectedElement.setCssLocator(tbCss.getText());
                            selectedElement.setXpathLocator(tbXpath.getText());
                            selectedElement.setPreferredLocatorType((Constants.LocatorType) cbPrefferedLocator.getSelectedItem());
                            trWebElements.repaint();

                            validateSelectedElement();
                            lblReadyForGeneration.setText("Ready");
                            if (listOfPrepearedElements.indexOf(selectedElement) == -1)
                                listOfPrepearedElements.add(selectedElement);
                        } else {
                            lblReadyForGeneration.setText("Unready");
                            if (listOfPrepearedElements.indexOf(selectedElement) != -1)
                                listOfPrepearedElements.remove(selectedElement);
                            JOptionPane.showMessageDialog(null, "Element will not be included into generated code.");

                        }

                    }
                }
            }
        });
        btnGenerateClass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String className = (
                        (webDriver instanceof AndroidDriver)
                                ? ((AndroidDriver) webDriver).getCurrentPackage()
                                : webDriver.getTitle())
                        .toLowerCase()
                        .replaceAll("\\.", "_")
                        .replaceAll(" ", "_")
                        .replaceAll("-", "")
                        .replaceAll("__", "_");
                Template classTemplate = null;
                Constants.ProgrammingLanguage language = (Constants.ProgrammingLanguage) cbProgrammingLanguage.getSelectedItem();
                switch (language) {
                    case JAVA: {
                        classTemplate = new JavaClassTemplate(className,
                                listOfPrepearedElements, chbGenerateMethods.isSelected(), webDriver instanceof AndroidDriver);
                        break;
                    }
                    case C_SHARP: {
                        classTemplate = new CSharpClassTemplate(className,
                                listOfPrepearedElements, chbGenerateMethods.isSelected(),  webDriver instanceof AndroidDriver);
                        break;
                    }
                }

                ClassEditorForm classEditorForm = new ClassEditorForm(classTemplate, language);
                classEditorForm.setContentPane(classEditorForm.mainPanel);
                classEditorForm.setSize(800, 600);
                classEditorForm.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                classEditorForm.setVisible(true);
                classEditorForm.setResizable(true);
                classEditorForm.setLocationRelativeTo(null);
                classEditorForm.pack();
            }
        });
        btnSaveTree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WebElementNode nodeToSave = (WebElementNode) trWebElements.getModel().getRoot();
                saveElementTreeObject(nodeToSave);
            }
        });
        btnOpenTree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileFilter fileFilter = new FileNameExtensionFilter("Elements tree", "elm");
                fileChooser.addChoosableFileFilter(fileFilter);
                fileChooser.setFileFilter(fileFilter);
                if (fileChooser.showOpenDialog(mainPanel) == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getPath();
                    ObjectInputStream objectinputstream = null;
                    try {
                        FileInputStream streamIn = new FileInputStream(filePath);
                        objectinputstream = new ObjectInputStream(streamIn);
                        WebElementNode webElementNode = null;
                        webElementNode = (WebElementNode) objectinputstream.readObject();

                        trWebElements.setModel(new DefaultTreeModel(webElementNode));
                        trWebElements.setSelectionRow(0);
                        trWebElements.setSelectionPath(trWebElements.getSelectionPath());
                        trWebElements.repaint();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    } finally {
                        if (objectinputstream != null) {
                            try {
                                objectinputstream.close();
                            } catch (IOException e1) {
                                JOptionPane.showMessageDialog(null, e1.getMessage());
                            }
                        }
                    }


                }
            }
        });
    }

    private void validateSelectedElement() {
        if (selectedElement != null) {
            Constants.LocatorType type = (Constants.LocatorType) cbPrefferedLocator.getSelectedItem();
            boolean isHandled = false;
            try {
                java.util.List<org.openqa.selenium.WebElement> elements = new ArrayList<org.openqa.selenium.WebElement>();
                String currentLocatorValue = "";
                switch (type) {
                    case ID: {
                        currentLocatorValue = tbId.getText();
                        if (currentLocatorValue.equals("") || currentLocatorValue.equals(" ")) {
                            JOptionPane.showMessageDialog(null, String.format("%s is empty.", type.toString()));
                        } else {
                            elements = webDriver.findElements(By.id(currentLocatorValue));
                            isHandled = true;
                        }
                        break;
                    }
                    case NAME: {
                        currentLocatorValue = tbName.getText();
                        if (currentLocatorValue.equals("") || currentLocatorValue.equals(" ")) {
                            JOptionPane.showMessageDialog(null, String.format("%s is empty.", type.toString()));
                        } else {
                            elements = webDriver.findElements(By.name(currentLocatorValue));
                            isHandled = true;
                        }
                        break;
                    }
                    case CLASS_NAME: {
                        currentLocatorValue = tbClassName.getText();
                        if (currentLocatorValue.equals("") || currentLocatorValue.equals(" ")) {
                            JOptionPane.showMessageDialog(null, String.format("%s is empty.", type.toString()));
                        } else {
                            elements = webDriver.findElements(By.className(currentLocatorValue));
                            isHandled = true;
                        }
                        break;
                    }
                    case LINK_TEXT: {
                        currentLocatorValue = tbLinkText.getText();
                        if (currentLocatorValue.equals("") || currentLocatorValue.equals(" ")) {
                            JOptionPane.showMessageDialog(null, String.format("%s is empty.", type.toString()));
                        } else {
                            elements = webDriver.findElements(By.linkText(currentLocatorValue));
                            isHandled = true;
                        }
                        break;
                    }
                    case TAG_NAME: {
                        currentLocatorValue = tbTagName.getText();
                        if (currentLocatorValue.equals("") || currentLocatorValue.equals(" ")) {
                            JOptionPane.showMessageDialog(null, String.format("%s is empty.", type.toString()));
                        } else {
                            elements = webDriver.findElements(By.tagName(currentLocatorValue));
                            isHandled = true;
                        }
                        break;
                    }
                    case CSS: {
                        currentLocatorValue = tbCss.getText();
                        if (currentLocatorValue.equals("") || currentLocatorValue.equals(" ")) {
                            JOptionPane.showMessageDialog(null, String.format("%s is empty.", type.toString()));
                        } else {
                            elements = webDriver.findElements(By.cssSelector(currentLocatorValue));
                            isHandled = true;
                        }
                        break;
                    }
                    case XPATH: {
                        currentLocatorValue = tbXpath.getText();
                        if (currentLocatorValue.equals("") || currentLocatorValue.equals(" ")) {
                            JOptionPane.showMessageDialog(null, String.format("%s is empty.", type.toString()));
                        } else {
                            elements = webDriver.findElements(By.xpath(currentLocatorValue));
                            isHandled = true;
                        }
                        break;
                    }
                }
                if (isHandled) {
                    if (elements.size() > 1) {
                        selectedElement.setMultipleElements(true);
                        JOptionPane.showMessageDialog(null, String.format("There are %1d elements found using %2s: \n'%3s'", elements.size(), type.toString(), currentLocatorValue));
                    }
                    if (elements.size() == 1) {
                        selectedElement.setMultipleElements(false);
                        JOptionPane.showMessageDialog(null, String.format("1 Element was found using %1s: \n'%2s'", type.toString(), currentLocatorValue));
                    }
                    if (elements.size() == 0) {
                        selectedElement.setMultipleElements(false);
                        JOptionPane.showMessageDialog(null, String.format("There are no elements with %1s: \n'%2s'", type.toString(), currentLocatorValue));
                    }
                }
            } catch (Exception ex) {
                int index = ex.getMessage().indexOf("\n");
                index = ex.getMessage().indexOf("\n", index + 1);
                JOptionPane.showMessageDialog(null, "Selenium has thrown Exception during find element by current preffered locator: \n"
                        + ex.getMessage().substring(0, index));
            }
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private void centerFrame() {
        Dimension windowSize = getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        java.awt.Point centerPoint = ge.getCenterPoint();

        int dx = centerPoint.x - windowSize.width / 2;
        int dy = centerPoint.y - windowSize.height / 2;
        setLocation(dx, dy);
    }

    private void saveElementTreeObject(WebElementNode node) {
        FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Elements tree", "elm");
        final FileDialog saveAsFileChooser = new FileDialog(this, "Save Elements Tree", FileDialog.SAVE);
        /*
        saveAsFileChooser.setApproveButtonText("Save Class");
        saveAsFileChooser.setFileFilter(extensionFilter);
        int actionDialog = saveAsFileChooser.showOpenDialog(this);
        if (actionDialog != JFileChooser.APPROVE_OPTION) {
            return;
        }
*/
        saveAsFileChooser.setVisible(true);
        String chosenDir = saveAsFileChooser.getDirectory();
        String chosenFile = saveAsFileChooser.getFile();
        saveAsFileChooser.dispose();
        File file;
        if (chosenDir != null && chosenFile != null) {
            file = new File(chosenDir + chosenFile);
            if (!file.getName().endsWith("." + extensionFilter.getExtensions()[0])) {
                file = new File(file.getAbsolutePath() + "." + extensionFilter.getExtensions()[0]);
            }
            FileOutputStream fout = null;
            ObjectOutputStream oos = null;
            try {
                fout = new FileOutputStream(file, false);
                oos = new ObjectOutputStream(fout);
                oos.writeObject(node);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            } finally {
                if (fout != null) {
                    try {
                        fout.close();
                        if (oos != null) oos.close();
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage());
                    }
                }
            }
        }
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
        mainPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setDividerSize(5);
        mainPanel.add(splitPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JSplitPane splitPane2 = new JSplitPane();
        splitPane2.setContinuousLayout(true);
        splitPane2.setDividerSize(2);
        splitPane2.setEnabled(false);
        splitPane2.setOrientation(0);
        splitPane1.setLeftComponent(splitPane2);
        splitPane2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-12828863)), null));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Segoe UI", Font.BOLD, 18, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Elements (right click to set ready):");
        splitPane2.setLeftComponent(label1);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        splitPane2.setRightComponent(panel1);
        btnGenerateAllLocators = new JButton();
        Font btnGenerateAllLocatorsFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnGenerateAllLocators.getFont());
        if (btnGenerateAllLocatorsFont != null) btnGenerateAllLocators.setFont(btnGenerateAllLocatorsFont);
        btnGenerateAllLocators.setText("Generate Locators For Elements");
        panel1.add(btnGenerateAllLocators, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        trWebElements = new JTree();
        Font trWebElementsFont = this.$$$getFont$$$("Segoe UI Light", -1, 14, trWebElements.getFont());
        if (trWebElementsFont != null) trWebElements.setFont(trWebElementsFont);
        trWebElements.putClientProperty("JTree.lineStyle", "");
        scrollPane1.setViewportView(trWebElements);
        btnGenerateClass = new JButton();
        Font btnGenerateClassFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnGenerateClass.getFont());
        if (btnGenerateClassFont != null) btnGenerateClass.setFont(btnGenerateClassFont);
        btnGenerateClass.setText("Generate Class for \"Ready\" Elements");
        panel1.add(btnGenerateClass, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        chbGenerateMethods = new JCheckBox();
        Font chbGenerateMethodsFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, chbGenerateMethods.getFont());
        if (chbGenerateMethodsFont != null) chbGenerateMethods.setFont(chbGenerateMethodsFont);
        chbGenerateMethods.setText("Generate Methods");
        panel1.add(chbGenerateMethods, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbProgrammingLanguage = new JComboBox();
        Font cbProgrammingLanguageFont = this.$$$getFont$$$("Segoe UI", -1, 14, cbProgrammingLanguage.getFont());
        if (cbProgrammingLanguageFont != null) cbProgrammingLanguage.setFont(cbProgrammingLanguageFont);
        panel1.add(cbProgrammingLanguage, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("Segoe UI Light", -1, 18, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setHorizontalAlignment(4);
        label2.setText("Programming Language for Class:");
        panel1.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(213, 25), null, 0, false));
        btnOpenTree = new JButton();
        Font btnOpenTreeFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnOpenTree.getFont());
        if (btnOpenTreeFont != null) btnOpenTree.setFont(btnOpenTreeFont);
        btnOpenTree.setText("Open");
        panel1.add(btnOpenTree, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnSaveTree = new JButton();
        Font btnSaveTreeFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnSaveTree.getFont());
        if (btnSaveTreeFont != null) btnSaveTree.setFont(btnSaveTreeFont);
        btnSaveTree.setText("Save");
        panel1.add(btnSaveTree, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        final JSplitPane splitPane3 = new JSplitPane();
        splitPane3.setDividerSize(2);
        splitPane3.setEnabled(false);
        splitPane3.setOrientation(0);
        splitPane1.setRightComponent(splitPane3);
        splitPane3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$("Segoe UI", Font.BOLD, 18, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Element Properties:");
        splitPane3.setLeftComponent(label3);
        pnlWebElementProps = new JPanel();
        pnlWebElementProps.setLayout(new GridLayoutManager(14, 7, new Insets(0, 0, 0, 0), -1, -1));
        splitPane3.setRightComponent(pnlWebElementProps);
        tbElementName = new JTextField();
        Font tbElementNameFont = this.$$$getFont$$$("Segoe UI", -1, 14, tbElementName.getFont());
        if (tbElementNameFont != null) tbElementName.setFont(tbElementNameFont);
        pnlWebElementProps.add(tbElementName, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        tbTagName = new JTextField();
        Font tbTagNameFont = this.$$$getFont$$$("Segoe UI", -1, 14, tbTagName.getFont());
        if (tbTagNameFont != null) tbTagName.setFont(tbTagNameFont);
        pnlWebElementProps.add(tbTagName, new GridConstraints(8, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), new Dimension(-1, 30), 0, false));
        tbLinkText = new JTextField();
        Font tbLinkTextFont = this.$$$getFont$$$("Segoe UI", -1, 14, tbLinkText.getFont());
        if (tbLinkTextFont != null) tbLinkText.setFont(tbLinkTextFont);
        pnlWebElementProps.add(tbLinkText, new GridConstraints(10, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), new Dimension(-1, 30), 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$("Segoe UI", Font.BOLD, 18, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Locators (right click to generate):");
        pnlWebElementProps.add(label4, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(258, 25), null, 0, false));
        tbCss = new JTextField();
        Font tbCssFont = this.$$$getFont$$$("Segoe UI", -1, 14, tbCss.getFont());
        if (tbCssFont != null) tbCss.setFont(tbCssFont);
        pnlWebElementProps.add(tbCss, new GridConstraints(11, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), new Dimension(-1, 30), 0, false));
        btnSaveTagNameLocator = new JButton();
        Font btnSaveTagNameLocatorFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnSaveTagNameLocator.getFont());
        if (btnSaveTagNameLocatorFont != null) btnSaveTagNameLocator.setFont(btnSaveTagNameLocatorFont);
        btnSaveTagNameLocator.setText("Save");
        pnlWebElementProps.add(btnSaveTagNameLocator, new GridConstraints(8, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), null, null, 0, false));
        btnSaveLinkTextLocator = new JButton();
        Font btnSaveLinkTextLocatorFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnSaveLinkTextLocator.getFont());
        if (btnSaveLinkTextLocatorFont != null) btnSaveLinkTextLocator.setFont(btnSaveLinkTextLocatorFont);
        btnSaveLinkTextLocator.setText("Save");
        pnlWebElementProps.add(btnSaveLinkTextLocator, new GridConstraints(10, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 30), null, 0, false));
        btnSaveCssLocator = new JButton();
        Font btnSaveCssLocatorFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnSaveCssLocator.getFont());
        if (btnSaveCssLocatorFont != null) btnSaveCssLocator.setFont(btnSaveCssLocatorFont);
        btnSaveCssLocator.setText("Save");
        pnlWebElementProps.add(btnSaveCssLocator, new GridConstraints(11, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 30), null, 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$("Segoe UI Semibold", -1, 18, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setHorizontalAlignment(4);
        label5.setText("Tag Name:");
        pnlWebElementProps.add(label5, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(213, 25), null, 0, false));
        lblLinkText = new JLabel();
        Font lblLinkTextFont = this.$$$getFont$$$("Segoe UI Semibold", -1, 18, lblLinkText.getFont());
        if (lblLinkTextFont != null) lblLinkText.setFont(lblLinkTextFont);
        lblLinkText.setHorizontalAlignment(4);
        lblLinkText.setText("Link Text:");
        pnlWebElementProps.add(lblLinkText, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(213, 25), null, 0, false));
        lblCss = new JLabel();
        Font lblCssFont = this.$$$getFont$$$("Segoe UI Semibold", -1, 18, lblCss.getFont());
        if (lblCssFont != null) lblCss.setFont(lblCssFont);
        lblCss.setHorizontalAlignment(4);
        lblCss.setText("CSS:");
        pnlWebElementProps.add(lblCss, new GridConstraints(11, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(213, 25), null, 0, false));
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$("Segoe UI Semibold", -1, 18, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setHorizontalAlignment(4);
        label6.setText("Element Name:");
        pnlWebElementProps.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(213, 25), null, 0, false));
        cbPrefferedLocator = new JComboBox();
        Font cbPrefferedLocatorFont = this.$$$getFont$$$("Segoe UI", -1, 14, cbPrefferedLocator.getFont());
        if (cbPrefferedLocatorFont != null) cbPrefferedLocator.setFont(cbPrefferedLocatorFont);
        pnlWebElementProps.add(cbPrefferedLocator, new GridConstraints(12, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), new Dimension(-1, 30), 0, false));
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$("Segoe UI Semibold", -1, 18, label7.getFont());
        if (label7Font != null) label7.setFont(label7Font);
        label7.setHorizontalAlignment(4);
        label7.setText("Preferred Locator:");
        pnlWebElementProps.add(label7, new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(213, 53), null, 0, false));
        lblReadyForGeneration = new JLabel();
        Font lblReadyForGenerationFont = this.$$$getFont$$$("Segoe UI", Font.BOLD, 20, lblReadyForGeneration.getFont());
        if (lblReadyForGenerationFont != null) lblReadyForGeneration.setFont(lblReadyForGenerationFont);
        lblReadyForGeneration.setHorizontalAlignment(2);
        lblReadyForGeneration.setHorizontalTextPosition(0);
        lblReadyForGeneration.setText("STATUS");
        pnlWebElementProps.add(lblReadyForGeneration, new GridConstraints(1, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(258, 25), null, 0, false));
        final JLabel label8 = new JLabel();
        Font label8Font = this.$$$getFont$$$("Segoe UI Semibold", -1, 18, label8.getFont());
        if (label8Font != null) label8.setFont(label8Font);
        label8.setHorizontalAlignment(4);
        label8.setText("Ready For Generation:");
        pnlWebElementProps.add(label8, new GridConstraints(1, 0, 2, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(213, 25), null, 0, false));
        lblLocatorActions = new JLabel();
        Font lblLocatorActionsFont = this.$$$getFont$$$("Segoe UI", Font.BOLD, 18, lblLocatorActions.getFont());
        if (lblLocatorActionsFont != null) lblLocatorActions.setFont(lblLocatorActionsFont);
        lblLocatorActions.setText("Locator Actions:");
        pnlWebElementProps.add(lblLocatorActions, new GridConstraints(4, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(164, 25), null, 0, false));
        btnGenerateTagNameLocator = new JButton();
        Font btnGenerateTagNameLocatorFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnGenerateTagNameLocator.getFont());
        if (btnGenerateTagNameLocatorFont != null) btnGenerateTagNameLocator.setFont(btnGenerateTagNameLocatorFont);
        btnGenerateTagNameLocator.setText("Generate");
        pnlWebElementProps.add(btnGenerateTagNameLocator, new GridConstraints(8, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnGenerateLinkTextLocator = new JButton();
        Font btnGenerateLinkTextLocatorFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnGenerateLinkTextLocator.getFont());
        if (btnGenerateLinkTextLocatorFont != null) btnGenerateLinkTextLocator.setFont(btnGenerateLinkTextLocatorFont);
        btnGenerateLinkTextLocator.setText("Generate");
        pnlWebElementProps.add(btnGenerateLinkTextLocator, new GridConstraints(10, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnGenerateCssLocator = new JButton();
        Font btnGenerateCssLocatorFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnGenerateCssLocator.getFont());
        if (btnGenerateCssLocatorFont != null) btnGenerateCssLocator.setFont(btnGenerateCssLocatorFont);
        btnGenerateCssLocator.setText("Generate");
        pnlWebElementProps.add(btnGenerateCssLocator, new GridConstraints(11, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnGenerateLocatorsForElement = new JButton();
        Font btnGenerateLocatorsForElementFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnGenerateLocatorsForElement.getFont());
        if (btnGenerateLocatorsForElementFont != null)
            btnGenerateLocatorsForElement.setFont(btnGenerateLocatorsForElementFont);
        btnGenerateLocatorsForElement.setText("Generate Locators");
        pnlWebElementProps.add(btnGenerateLocatorsForElement, new GridConstraints(13, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnValidatePrefferedLocator = new JButton();
        Font btnValidatePrefferedLocatorFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnValidatePrefferedLocator.getFont());
        if (btnValidatePrefferedLocatorFont != null)
            btnValidatePrefferedLocator.setFont(btnValidatePrefferedLocatorFont);
        btnValidatePrefferedLocator.setText("Validate Preferred Locator");
        pnlWebElementProps.add(btnValidatePrefferedLocator, new GridConstraints(13, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), new Dimension(-1, 30), 0, false));
        final JLabel label9 = new JLabel();
        Font label9Font = this.$$$getFont$$$("Segoe UI Semibold", -1, 18, label9.getFont());
        if (label9Font != null) label9.setFont(label9Font);
        label9.setHorizontalAlignment(4);
        label9.setText("Name:");
        pnlWebElementProps.add(label9, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(213, 25), null, 0, false));
        tbName = new JTextField();
        Font tbNameFont = this.$$$getFont$$$("Segoe UI", -1, 14, tbName.getFont());
        if (tbNameFont != null) tbName.setFont(tbNameFont);
        pnlWebElementProps.add(tbName, new GridConstraints(5, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), new Dimension(-1, 30), 0, false));
        btnGenerateNameLocator = new JButton();
        Font btnGenerateNameLocatorFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnGenerateNameLocator.getFont());
        if (btnGenerateNameLocatorFont != null) btnGenerateNameLocator.setFont(btnGenerateNameLocatorFont);
        btnGenerateNameLocator.setText("Generate");
        pnlWebElementProps.add(btnGenerateNameLocator, new GridConstraints(5, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnSaveNameLocator = new JButton();
        Font btnSaveNameLocatorFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnSaveNameLocator.getFont());
        if (btnSaveNameLocatorFont != null) btnSaveNameLocator.setFont(btnSaveNameLocatorFont);
        btnSaveNameLocator.setText("Save");
        pnlWebElementProps.add(btnSaveNameLocator, new GridConstraints(5, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, 30), new Dimension(-1, 30), null, 0, false));
        final JLabel label10 = new JLabel();
        Font label10Font = this.$$$getFont$$$("Segoe UI Semibold", -1, 18, label10.getFont());
        if (label10Font != null) label10.setFont(label10Font);
        label10.setHorizontalAlignment(4);
        label10.setText("Id:");
        pnlWebElementProps.add(label10, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(213, 25), null, 0, false));
        tbId = new JTextField();
        Font tbIdFont = this.$$$getFont$$$("Segoe UI", -1, 14, tbId.getFont());
        if (tbIdFont != null) tbId.setFont(tbIdFont);
        pnlWebElementProps.add(tbId, new GridConstraints(6, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), new Dimension(-1, 30), 0, false));
        btnGenerateId = new JButton();
        Font btnGenerateIdFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnGenerateId.getFont());
        if (btnGenerateIdFont != null) btnGenerateId.setFont(btnGenerateIdFont);
        btnGenerateId.setText("Generate");
        pnlWebElementProps.add(btnGenerateId, new GridConstraints(6, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        final JLabel label11 = new JLabel();
        Font label11Font = this.$$$getFont$$$("Segoe UI Semibold", -1, 18, label11.getFont());
        if (label11Font != null) label11.setFont(label11Font);
        label11.setHorizontalAlignment(4);
        label11.setText("Class Name:");
        pnlWebElementProps.add(label11, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(213, 25), null, 0, false));
        tbClassName = new JTextField();
        Font tbClassNameFont = this.$$$getFont$$$("Segoe UI", -1, 14, tbClassName.getFont());
        if (tbClassNameFont != null) tbClassName.setFont(tbClassNameFont);
        pnlWebElementProps.add(tbClassName, new GridConstraints(7, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), new Dimension(-1, 30), 0, false));
        btnGenerateClassNameLocator = new JButton();
        Font btnGenerateClassNameLocatorFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnGenerateClassNameLocator.getFont());
        if (btnGenerateClassNameLocatorFont != null)
            btnGenerateClassNameLocator.setFont(btnGenerateClassNameLocatorFont);
        btnGenerateClassNameLocator.setText("Generate");
        pnlWebElementProps.add(btnGenerateClassNameLocator, new GridConstraints(7, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnSaveElementName = new JButton();
        Font btnSaveElementNameFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnSaveElementName.getFont());
        if (btnSaveElementNameFont != null) btnSaveElementName.setFont(btnSaveElementNameFont);
        btnSaveElementName.setText("Save Name");
        pnlWebElementProps.add(btnSaveElementName, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnSetElementReady = new JButton();
        Font btnSetElementReadyFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnSetElementReady.getFont());
        if (btnSetElementReadyFont != null) btnSetElementReady.setFont(btnSetElementReadyFont);
        btnSetElementReady.setText("Set Ready");
        pnlWebElementProps.add(btnSetElementReady, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnSetElementUnready = new JButton();
        Font btnSetElementUnreadyFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnSetElementUnready.getFont());
        if (btnSetElementUnreadyFont != null) btnSetElementUnready.setFont(btnSetElementUnreadyFont);
        btnSetElementUnready.setText("Set Not Ready");
        pnlWebElementProps.add(btnSetElementUnready, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnShowOnPage = new JButton();
        Font btnShowOnPageFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnShowOnPage.getFont());
        if (btnShowOnPageFont != null) btnShowOnPage.setFont(btnShowOnPageFont);
        btnShowOnPage.setText("Highlight On Source");
        pnlWebElementProps.add(btnShowOnPage, new GridConstraints(13, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnSaveAllLocators = new JButton();
        Font btnSaveAllLocatorsFont = this.$$$getFont$$$("Segoe UI Light", Font.BOLD, 18, btnSaveAllLocators.getFont());
        if (btnSaveAllLocatorsFont != null) btnSaveAllLocators.setFont(btnSaveAllLocatorsFont);
        btnSaveAllLocators.setText("Save Locators");
        pnlWebElementProps.add(btnSaveAllLocators, new GridConstraints(13, 3, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnSavePrefferedLocator = new JButton();
        Font btnSavePrefferedLocatorFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnSavePrefferedLocator.getFont());
        if (btnSavePrefferedLocatorFont != null) btnSavePrefferedLocator.setFont(btnSavePrefferedLocatorFont);
        btnSavePrefferedLocator.setText("Save");
        pnlWebElementProps.add(btnSavePrefferedLocator, new GridConstraints(12, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        final JLabel label12 = new JLabel();
        Font label12Font = this.$$$getFont$$$("Segoe UI Semibold", -1, 18, label12.getFont());
        if (label12Font != null) label12.setFont(label12Font);
        label12.setHorizontalAlignment(4);
        label12.setText("XPath:");
        pnlWebElementProps.add(label12, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(213, 25), null, 0, false));
        tbXpath = new JTextField();
        Font tbXpathFont = this.$$$getFont$$$("Segoe UI", -1, 14, tbXpath.getFont());
        if (tbXpathFont != null) tbXpath.setFont(tbXpathFont);
        pnlWebElementProps.add(tbXpath, new GridConstraints(9, 1, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), new Dimension(800, 30), 0, false));
        btnGenerateXpathLocator = new JButton();
        Font btnGenerateXpathLocatorFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnGenerateXpathLocator.getFont());
        if (btnGenerateXpathLocatorFont != null) btnGenerateXpathLocator.setFont(btnGenerateXpathLocatorFont);
        btnGenerateXpathLocator.setText("Generate");
        pnlWebElementProps.add(btnGenerateXpathLocator, new GridConstraints(9, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnSaveXpathLocator = new JButton();
        Font btnSaveXpathLocatorFont = this.$$$getFont$$$("Segoe UI Light", -1, 18, btnSaveXpathLocator.getFont());
        if (btnSaveXpathLocatorFont != null) btnSaveXpathLocator.setFont(btnSaveXpathLocatorFont);
        btnSaveXpathLocator.setText("Save");
        pnlWebElementProps.add(btnSaveXpathLocator, new GridConstraints(9, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        label2.setLabelFor(tbElementName);
        label5.setLabelFor(tbTagName);
        lblLinkText.setLabelFor(tbLinkText);
        lblCss.setLabelFor(tbCss);
        label6.setLabelFor(tbElementName);
        label8.setLabelFor(tbElementName);
        label9.setLabelFor(tbName);
        label10.setLabelFor(tbName);
        label11.setLabelFor(tbName);
        label12.setLabelFor(tbXpath);
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
