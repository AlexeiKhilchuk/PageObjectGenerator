package by.bsuir.pogen.utils;

import by.bsuir.pogen.models.WebElement;
import by.bsuir.pogen.models.WebElementNode;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;

/**
 * Created by alexei.khilchuk on 31/03/2017.
 */
public class LocatorBuilder {
    static Logger LOG = LoggerFactory.getLogger(LocatorBuilder.class.getName());

    public String getCssLocator(WebElement element){
        try {
            return element.getElement().cssSelector();
        }
        catch (IllegalArgumentException ex){
            LOG.info("There was an error during getting CSS Locator", ex);
            return "";
        }

    }

    public String getXpathLocator(WebElement element, RemoteWebDriver webDriver){
        LOG.info(String.format("Building XPath locator for '%s' element", element.getElementName()));
        try {
            String locator = (String) ((JavascriptExecutor) webDriver).executeScript(
                    "getXPath=function(node)" +
                            "{" +
                                "if (node.id !== '')" +
                                "{" +
                                     "return '//' + node.tagName.toLowerCase() + '[@id=\"' + node.id + '\"]'" +
                                "}" +
                                "if (node === document.body)" +
                                "{" +
                                     "return node.tagName.toLowerCase()" +
                                "}" +
                                "var nodeCount = 0;" +
                                "var childNodes = node.parentNode.childNodes;" +
                                "for (var i=0; i<childNodes.length; i++)" +
                                "{" +
                                      "var currentNode = childNodes[i];" +
                                      "if (currentNode === node)" +
                                      "{" +
                                            "return getXPath(node.parentNode) + '/' + node.tagName.toLowerCase() + '[' + (nodeCount+1) + ']'" +
                                      "}" +
                                      "if (currentNode.nodeType === 1 && currentNode.tagName.toLowerCase() === node.tagName.toLowerCase())" +
                                      "{" +
                                            "nodeCount++" +
                                      "}" +
                                "}" +
                            "};" +
                            "return getXPath(arguments[0]);", webDriver.findElement(By.cssSelector(element.getElement().cssSelector())));

            if (!locator.substring(0, 2).equals("//")) {
                locator = "//" + locator;
            }
            return locator;
        }
        catch (Exception e){
            LOG.error("The was an error during generating xpath locator",e);
            return "";
        }

    }

    public String getNameLocator(WebElement element){
        LOG.info(String.format("Building Name locator for '%s' element", element.getElementName()));
        return element.getElement().attr("name");
    }

    public String getClassNameLocator(WebElement element) {
        LOG.info(String.format("Building Class Name locator for '%s' element", element.getElementName()));
        return element.getElement().attr("class");
    }

    public String getTagNameLocator(WebElement element) {
        LOG.info(String.format("Building Tag Name locator for '%s' element", element.getElementName()));
        return element.getElement().tagName();
    }

    public String getLinkTextLocator(WebElement element){
        LOG.info(String.format("Building Link Text locator for '%s' element", element.getElementName()));
        if (!element.getElement().attr("href").equals(null) || !element.getElement().attr("href").equals(""))
            return element.getElement().text();
        else
            return "";
    }

    public String getIdLocator(WebElement element){
        LOG.info(String.format("Building Id locator for '%s' element", element.getElementName()));
        return element.getElement().attr("id");
    }

    public void generateLocatorsForNodes(WebElementNode node, final RemoteWebDriver webDriver) {
        WebElement element = (WebElement) node.getUserObject();
        String tagName = element.getElement().tagName();
        if (tagName.contains("div")
         || tagName.contains("section")
         || tagName.contains("script")
         || tagName.contains("form")
         || tagName.contains("ul")){
            LOG.info(String.format("Skipping building locator for '%s' tag element", tagName));
        }
        else {
            element.setNameLocator(getNameLocator(element));
            element.setTagNameLocator(getTagNameLocator(element));
            element.setLinkTextLocator(getLinkTextLocator(element));
            element.setCssLocator(getCssLocator(element));
            element.setXpathLocator(getXpathLocator(element, webDriver));
        }
        final Enumeration children = node.children();
        if (children != null) {
            while (children.hasMoreElements()) {
                generateLocatorsForNodes((WebElementNode) children.nextElement(), webDriver);
            }
        }
    }
}
