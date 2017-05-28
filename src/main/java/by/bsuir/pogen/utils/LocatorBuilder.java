package by.bsuir.pogen.utils;

import by.bsuir.pogen.models.WebElement;
import by.bsuir.pogen.models.WebElementNode;
import org.apache.log4j.Logger;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;


import java.util.Enumeration;

/**
 * Created by alexei.khilchuk on 31/03/2017.
 */
public class LocatorBuilder {
    static Logger LOG = Logger.getLogger(LocatorBuilder.class.getName());


    public String getCssLocator(WebElement element){
        try {
            if(element.getElement().id().length() > 0) {
                return "#" + element.getElement().id();
            } else {
                String tagName = element.getElement().tagName().replace(':', '|');
                StringBuilder selector = new StringBuilder(tagName);
                String classes = StringUtil.join(element.getElement().classNames(), ".");
                if(classes.length() > 0) {
                    selector.append('.').append(classes);
                }

                if(element.getElement().parent() != null && !(element.getElement().parent() instanceof Document)) {
                    selector.insert(0, " > ");
                    if(element.getElement().parent().select(selector.toString()).size() > 1) {
                        selector.append(String.format(":nth-child(%d)", new Object[]{Integer.valueOf(element.getElement().elementSiblingIndex().intValue() + 1)}));
                    }

                    return element.getElement().parent().cssSelector() + selector.toString();
                } else {
                    return selector.toString();
                }
            }
        }
        catch (IllegalArgumentException ex){
            LOG.info("There was an error during getting CSS Locator", ex);
            return "";
        }

    }

    public String getXpathLocator(WebElement element, RemoteWebDriver webDriver){
        LOG.info(String.format("Building XPath locator for '%s' element", element.getElementName()));
        org.openqa.selenium.WebElement el = webDriver.findElement(By.cssSelector(element.getElement().cssSelector()));

        StringBuffer sb = new StringBuffer();
        sb.append("    	function getPathTo(element) {\n");
        sb.append("    	    if (element.id!=='')\n");
        sb.append("    	        return 'id(\"'+element.id+'\")';\n");
        sb.append("    	    if (element===document.body)\n");
        sb.append("    	        return element.tagName.toLowerCase();\n");
        sb.append("    	    var ix= 0;\n");
        sb.append("    	    var siblings= element.parentNode.childNodes;\n");
        sb.append("    	    for (var i= 0; i<siblings.length; i++) {\n");
        sb.append("    	        var sibling= siblings[i];\n");
        sb.append("    	        if (sibling===element){\n");
        sb.append("    	            var attr = ''");
        sb.append("                     +(element.hasAttribute('class')?'[contains(@class,\"'+element.getAttribute('class')+'\")]':'')");
        sb.append("                     +(element.hasAttribute('name')?'[contains(@name,\"'+element.getAttribute('name')+'\")]':'');\n");
        sb.append("    	            return getPathTo(element.parentNode)+'/'+element.tagName.toLowerCase()");
        sb.append("                     +(attr==''?'['+(ix+1)+']':attr)");
        sb.append("             ;}\n");
        sb.append("    	        if (sibling.nodeType===1 && sibling.tagName===element.tagName)\n");
        sb.append("    	            ix++;\n");
        sb.append("    }}\n");
        sb.append("return getPathTo(arguments[0]);");
        String res = "";
        try {
            res = (String) webDriver.executeScript(sb.toString(), el);
            if(!res.startsWith("id")){
                res = "//"+res;
            }
            if (el.findElements(By.xpath("./*")).isEmpty()
                    && !"".equals(el.getText())) {
                res = res
                        + "[contains(.,'" + el.getText().replace("/", "[slh]") + "')]";
            }
        }
        catch (Exception e){
            LOG.error("The was an error during generating xpath locator",e);
        }
        return res;
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
