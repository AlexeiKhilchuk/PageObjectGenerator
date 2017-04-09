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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    static Logger LOG = LoggerFactory.getLogger(GeneratorForm.class.getName());
    private LocatorBuilder locatorBuilder = new LocatorBuilder();
    private ArrayList listOfPrepearedElements = new ArrayList<WebElement>();

    public JPanel mainPanel;
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
    private RemoteWebDriver webDriver;
    private WebElementNode htmlTree;
    private WebElement selectedElement;

    public GeneratorForm(RemoteWebDriver currentWebDriver) {
        super("Page Object Generator: " + currentWebDriver.getTitle());
        webDriver = currentWebDriver;
        $$$setupUI$$$();
        webDriver.manage().window().setPosition(new Point(-2000, 0));
        Document doc = Jsoup.parse(webDriver.getPageSource());
        htmlTree = new WebElementNode(new WebElement(doc.body()));
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
                    webDriver.close();
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
                String className = webDriver.getTitle()
                        .toLowerCase()
                        .replaceAll(" ", "_")
                        .replaceAll("-", "")
                        .replaceAll("__", "_");
                Template classTemplate = null;
                Constants.ProgrammingLanguage language = (Constants.ProgrammingLanguage) cbProgrammingLanguage.getSelectedItem();
                switch (language) {
                    case JAVA: {
                        classTemplate = new JavaClassTemplate(className,
                                listOfPrepearedElements, chbGenerateMethods.isSelected());
                        break;
                    }
                    case C_SHARP: {
                        classTemplate = new CSharpClassTemplate(className,
                                listOfPrepearedElements, chbGenerateMethods.isSelected());
                        break;
                    }
                }

                if (classTemplate != null) {
                    ClassEditorForm classEditorForm = new ClassEditorForm(classTemplate, language);
                    classEditorForm.setContentPane(classEditorForm.mainPanel);
                    classEditorForm.setSize(800, 600);
                    classEditorForm.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                    classEditorForm.setVisible(true);
                    classEditorForm.setResizable(true);
                    classEditorForm.setLocationRelativeTo(null);
                    classEditorForm.pack();
                } else {
                    JOptionPane.showMessageDialog(null, "Unable to generate class.");
                }
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
                String currentLocatorValue = new String();
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
        final JFileChooser saveAsFileChooser = new JFileChooser();
        saveAsFileChooser.setApproveButtonText("Save");
        saveAsFileChooser.setFileFilter(extensionFilter);
        int actionDialog = saveAsFileChooser.showOpenDialog(this);
        if (actionDialog != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = saveAsFileChooser.getSelectedFile();
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
        label1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label1.setText("Web Elements (right click to set ready):");
        splitPane2.setLeftComponent(label1);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        splitPane2.setRightComponent(panel1);
        btnGenerateAllLocators = new JButton();
        btnGenerateAllLocators.setFont(new Font("Segoe UI Light", btnGenerateAllLocators.getFont().getStyle(), 18));
        btnGenerateAllLocators.setText("Generate Locators For Elements");
        panel1.add(btnGenerateAllLocators, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        trWebElements = new JTree();
        trWebElements.setFont(new Font("Segoe UI Light", trWebElements.getFont().getStyle(), 14));
        trWebElements.putClientProperty("JTree.lineStyle", "");
        scrollPane1.setViewportView(trWebElements);
        btnGenerateClass = new JButton();
        btnGenerateClass.setFont(new Font("Segoe UI Light", btnGenerateClass.getFont().getStyle(), 18));
        btnGenerateClass.setText("Generate Class for \"Ready\" Elements");
        panel1.add(btnGenerateClass, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        chbGenerateMethods = new JCheckBox();
        chbGenerateMethods.setFont(new Font("Segoe UI Light", chbGenerateMethods.getFont().getStyle(), 18));
        chbGenerateMethods.setText("Generate Methods");
        panel1.add(chbGenerateMethods, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbProgrammingLanguage = new JComboBox();
        cbProgrammingLanguage.setFont(new Font("Segoe UI", cbProgrammingLanguage.getFont().getStyle(), 14));
        panel1.add(cbProgrammingLanguage, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setFont(new Font("Segoe UI Light", label2.getFont().getStyle(), 18));
        label2.setHorizontalAlignment(4);
        label2.setText("Programming Language for Class:");
        panel1.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(213, 25), null, 0, false));
        btnOpenTree = new JButton();
        btnOpenTree.setFont(new Font("Segoe UI Light", btnOpenTree.getFont().getStyle(), 18));
        btnOpenTree.setText("Open");
        panel1.add(btnOpenTree, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnSaveTree = new JButton();
        btnSaveTree.setFont(new Font("Segoe UI Light", btnSaveTree.getFont().getStyle(), 18));
        btnSaveTree.setText("Save");
        panel1.add(btnSaveTree, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        final JSplitPane splitPane3 = new JSplitPane();
        splitPane3.setDividerSize(2);
        splitPane3.setEnabled(false);
        splitPane3.setOrientation(0);
        splitPane1.setRightComponent(splitPane3);
        splitPane3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null));
        final JLabel label3 = new JLabel();
        label3.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label3.setText("Web Element Properties:");
        splitPane3.setLeftComponent(label3);
        pnlWebElementProps = new JPanel();
        pnlWebElementProps.setLayout(new GridLayoutManager(15, 7, new Insets(0, 0, 0, 0), -1, -1));
        splitPane3.setRightComponent(pnlWebElementProps);
        tbElementName = new JTextField();
        tbElementName.setFont(new Font("Segoe UI", tbElementName.getFont().getStyle(), 14));
        pnlWebElementProps.add(tbElementName, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        tbTagName = new JTextField();
        tbTagName.setFont(new Font("Segoe UI", tbTagName.getFont().getStyle(), 14));
        pnlWebElementProps.add(tbTagName, new GridConstraints(8, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        tbLinkText = new JTextField();
        tbLinkText.setFont(new Font("Segoe UI", tbLinkText.getFont().getStyle(), 14));
        pnlWebElementProps.add(tbLinkText, new GridConstraints(9, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label4.setText("Locators (right click to generate):");
        pnlWebElementProps.add(label4, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(258, 25), null, 0, false));
        tbCss = new JTextField();
        tbCss.setFont(new Font("Segoe UI", tbCss.getFont().getStyle(), 14));
        pnlWebElementProps.add(tbCss, new GridConstraints(10, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnSaveTagNameLocator = new JButton();
        btnSaveTagNameLocator.setFont(new Font("Segoe UI Light", btnSaveTagNameLocator.getFont().getStyle(), 18));
        btnSaveTagNameLocator.setText("Save");
        pnlWebElementProps.add(btnSaveTagNameLocator, new GridConstraints(8, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), null, null, 0, false));
        btnSaveLinkTextLocator = new JButton();
        btnSaveLinkTextLocator.setFont(new Font("Segoe UI Light", btnSaveLinkTextLocator.getFont().getStyle(), 18));
        btnSaveLinkTextLocator.setText("Save");
        pnlWebElementProps.add(btnSaveLinkTextLocator, new GridConstraints(9, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 30), null, 0, false));
        btnSaveCssLocator = new JButton();
        btnSaveCssLocator.setFont(new Font("Segoe UI Light", btnSaveCssLocator.getFont().getStyle(), 18));
        btnSaveCssLocator.setText("Save");
        pnlWebElementProps.add(btnSaveCssLocator, new GridConstraints(10, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 30), null, 0, false));
        btnSaveXpathLocator = new JButton();
        btnSaveXpathLocator.setFont(new Font("Segoe UI Light", btnSaveXpathLocator.getFont().getStyle(), 18));
        btnSaveXpathLocator.setText("Save");
        pnlWebElementProps.add(btnSaveXpathLocator, new GridConstraints(11, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setFont(new Font("Segoe UI Semibold", label5.getFont().getStyle(), 18));
        label5.setHorizontalAlignment(4);
        label5.setText("Tag Name:");
        pnlWebElementProps.add(label5, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(213, 25), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setFont(new Font("Segoe UI Semibold", label6.getFont().getStyle(), 18));
        label6.setHorizontalAlignment(4);
        label6.setText("Link Text:");
        pnlWebElementProps.add(label6, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(213, 25), null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setFont(new Font("Segoe UI Semibold", label7.getFont().getStyle(), 18));
        label7.setHorizontalAlignment(4);
        label7.setText("CSS:");
        pnlWebElementProps.add(label7, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(213, 25), null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setFont(new Font("Segoe UI Semibold", label8.getFont().getStyle(), 18));
        label8.setHorizontalAlignment(4);
        label8.setText("XPath:");
        pnlWebElementProps.add(label8, new GridConstraints(11, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(213, 25), null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setFont(new Font("Segoe UI Semibold", label9.getFont().getStyle(), 18));
        label9.setHorizontalAlignment(4);
        label9.setText("Element Name:");
        pnlWebElementProps.add(label9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(213, 25), null, 0, false));
        cbPrefferedLocator = new JComboBox();
        cbPrefferedLocator.setFont(new Font("Segoe UI", cbPrefferedLocator.getFont().getStyle(), 14));
        pnlWebElementProps.add(cbPrefferedLocator, new GridConstraints(13, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setFont(new Font("Segoe UI Semibold", label10.getFont().getStyle(), 18));
        label10.setHorizontalAlignment(4);
        label10.setText("Preferred Locator:");
        pnlWebElementProps.add(label10, new GridConstraints(13, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(213, 53), null, 0, false));
        lblReadyForGeneration = new JLabel();
        lblReadyForGeneration.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblReadyForGeneration.setHorizontalAlignment(2);
        lblReadyForGeneration.setHorizontalTextPosition(0);
        lblReadyForGeneration.setText("STATUS");
        pnlWebElementProps.add(lblReadyForGeneration, new GridConstraints(1, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(258, 25), null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setFont(new Font("Segoe UI Semibold", label11.getFont().getStyle(), 18));
        label11.setHorizontalAlignment(4);
        label11.setText("Ready For Generation:");
        pnlWebElementProps.add(label11, new GridConstraints(1, 0, 2, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(213, 25), null, 0, false));
        lblLocatorActions = new JLabel();
        lblLocatorActions.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLocatorActions.setText("Locator Actions:");
        pnlWebElementProps.add(lblLocatorActions, new GridConstraints(4, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(164, 25), null, 0, false));
        btnGenerateTagNameLocator = new JButton();
        btnGenerateTagNameLocator.setFont(new Font("Segoe UI Light", btnGenerateTagNameLocator.getFont().getStyle(), 18));
        btnGenerateTagNameLocator.setText("Generate");
        pnlWebElementProps.add(btnGenerateTagNameLocator, new GridConstraints(8, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnGenerateLinkTextLocator = new JButton();
        btnGenerateLinkTextLocator.setFont(new Font("Segoe UI Light", btnGenerateLinkTextLocator.getFont().getStyle(), 18));
        btnGenerateLinkTextLocator.setText("Generate");
        pnlWebElementProps.add(btnGenerateLinkTextLocator, new GridConstraints(9, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnGenerateCssLocator = new JButton();
        btnGenerateCssLocator.setFont(new Font("Segoe UI Light", btnGenerateCssLocator.getFont().getStyle(), 18));
        btnGenerateCssLocator.setText("Generate");
        pnlWebElementProps.add(btnGenerateCssLocator, new GridConstraints(10, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnGenerateXpathLocator = new JButton();
        btnGenerateXpathLocator.setFont(new Font("Segoe UI Light", btnGenerateXpathLocator.getFont().getStyle(), 18));
        btnGenerateXpathLocator.setText("Generate");
        pnlWebElementProps.add(btnGenerateXpathLocator, new GridConstraints(11, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnGenerateLocatorsForElement = new JButton();
        btnGenerateLocatorsForElement.setFont(new Font("Segoe UI Light", btnGenerateLocatorsForElement.getFont().getStyle(), 18));
        btnGenerateLocatorsForElement.setText("Generate Locators");
        pnlWebElementProps.add(btnGenerateLocatorsForElement, new GridConstraints(14, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnValidatePrefferedLocator = new JButton();
        btnValidatePrefferedLocator.setFont(new Font("Segoe UI Light", btnValidatePrefferedLocator.getFont().getStyle(), 18));
        btnValidatePrefferedLocator.setText("Validate Preferred Locator");
        pnlWebElementProps.add(btnValidatePrefferedLocator, new GridConstraints(14, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        tbXpath = new JTextField();
        tbXpath.setFont(new Font("Segoe UI", tbXpath.getFont().getStyle(), 14));
        pnlWebElementProps.add(tbXpath, new GridConstraints(11, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setFont(new Font("Segoe UI Semibold", label12.getFont().getStyle(), 18));
        label12.setHorizontalAlignment(4);
        label12.setText("Name:");
        pnlWebElementProps.add(label12, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(213, 25), null, 0, false));
        tbName = new JTextField();
        tbName.setFont(new Font("Segoe UI", tbName.getFont().getStyle(), 14));
        pnlWebElementProps.add(tbName, new GridConstraints(5, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnGenerateNameLocator = new JButton();
        btnGenerateNameLocator.setFont(new Font("Segoe UI Light", btnGenerateNameLocator.getFont().getStyle(), 18));
        btnGenerateNameLocator.setText("Generate");
        pnlWebElementProps.add(btnGenerateNameLocator, new GridConstraints(5, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnSaveNameLocator = new JButton();
        btnSaveNameLocator.setFont(new Font("Segoe UI Light", btnSaveNameLocator.getFont().getStyle(), 18));
        btnSaveNameLocator.setText("Save");
        pnlWebElementProps.add(btnSaveNameLocator, new GridConstraints(5, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, 30), new Dimension(-1, 30), null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setFont(new Font("Segoe UI Semibold", label13.getFont().getStyle(), 18));
        label13.setHorizontalAlignment(4);
        label13.setText("Id:");
        pnlWebElementProps.add(label13, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(213, 25), null, 0, false));
        tbId = new JTextField();
        tbId.setFont(new Font("Segoe UI", tbId.getFont().getStyle(), 14));
        pnlWebElementProps.add(tbId, new GridConstraints(6, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnGenerateId = new JButton();
        btnGenerateId.setFont(new Font("Segoe UI Light", btnGenerateId.getFont().getStyle(), 18));
        btnGenerateId.setText("Generate");
        pnlWebElementProps.add(btnGenerateId, new GridConstraints(6, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setFont(new Font("Segoe UI Semibold", label14.getFont().getStyle(), 18));
        label14.setHorizontalAlignment(4);
        label14.setText("Class Name:");
        pnlWebElementProps.add(label14, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(213, 25), null, 0, false));
        tbClassName = new JTextField();
        tbClassName.setFont(new Font("Segoe UI", tbClassName.getFont().getStyle(), 14));
        pnlWebElementProps.add(tbClassName, new GridConstraints(7, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnGenerateClassNameLocator = new JButton();
        btnGenerateClassNameLocator.setFont(new Font("Segoe UI Light", btnGenerateClassNameLocator.getFont().getStyle(), 18));
        btnGenerateClassNameLocator.setText("Generate");
        pnlWebElementProps.add(btnGenerateClassNameLocator, new GridConstraints(7, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnSaveElementName = new JButton();
        btnSaveElementName.setFont(new Font("Segoe UI Light", btnSaveElementName.getFont().getStyle(), 18));
        btnSaveElementName.setText("Save");
        pnlWebElementProps.add(btnSaveElementName, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnSetElementReady = new JButton();
        btnSetElementReady.setFont(new Font("Segoe UI Light", btnSetElementReady.getFont().getStyle(), 18));
        btnSetElementReady.setText("Set Ready");
        pnlWebElementProps.add(btnSetElementReady, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnSetElementUnready = new JButton();
        btnSetElementUnready.setFont(new Font("Segoe UI Light", btnSetElementUnready.getFont().getStyle(), 18));
        btnSetElementUnready.setText("Set Not Ready");
        pnlWebElementProps.add(btnSetElementUnready, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnShowOnPage = new JButton();
        btnShowOnPage.setFont(new Font("Segoe UI Light", btnShowOnPage.getFont().getStyle(), 18));
        btnShowOnPage.setText("Highlight On Page");
        pnlWebElementProps.add(btnShowOnPage, new GridConstraints(14, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnSaveAllLocators = new JButton();
        btnSaveAllLocators.setFont(new Font("Segoe UI Light", Font.BOLD, 18));
        btnSaveAllLocators.setText("Save");
        pnlWebElementProps.add(btnSaveAllLocators, new GridConstraints(14, 3, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        btnSavePrefferedLocator = new JButton();
        btnSavePrefferedLocator.setFont(new Font("Segoe UI Light", btnSavePrefferedLocator.getFont().getStyle(), 18));
        btnSavePrefferedLocator.setText("Save");
        pnlWebElementProps.add(btnSavePrefferedLocator, new GridConstraints(13, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        label2.setLabelFor(tbElementName);
        label5.setLabelFor(tbTagName);
        label6.setLabelFor(tbLinkText);
        label7.setLabelFor(tbCss);
        label8.setLabelFor(tbXpath);
        label9.setLabelFor(tbElementName);
        label11.setLabelFor(tbElementName);
        label12.setLabelFor(tbName);
        label13.setLabelFor(tbName);
        label14.setLabelFor(tbName);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
