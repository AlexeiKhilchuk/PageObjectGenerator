package by.bsuir.pogen.utils;

import by.bsuir.pogen.models.WebElement;
import by.bsuir.pogen.models.WebElementNode;
import io.appium.java_client.android.AndroidDriver;
import org.apache.log4j.Logger;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Alexei Khilchuk on 20.07.2018.
 */
public class LocatorBuilder {
    static Logger LOG = Logger.getLogger(LocatorBuilder.class.getName());


    public String getCssLocator(WebElement element) {
        try {
            if (element.getElement().id().length() > 0) {
                return "#" + element.getElement().id();
            } else {
                String tagName = element.getElement().tagName().replace(':', '|');
                StringBuilder selector = new StringBuilder(tagName);
                String classes = StringUtil.join(element.getElement().classNames(), ".");
                if (classes.length() > 0) {
                    selector.append('.').append(classes);
                }

                if (element.getElement().parent() != null && !(element.getElement().parent() instanceof Document)) {
                    selector.insert(0, " > ");
                    if (element.getElement().parent().select(selector.toString()).size() > 1) {
                        selector.append(String.format(":nth-child(%d)", new Object[]{Integer.valueOf(element.getElement().elementSiblingIndex().intValue() + 1)}));
                    }

                    return element.getElement().parent().cssSelector() + selector.toString();
                } else {
                    return selector.toString();
                }
            }
        } catch (IllegalArgumentException ex) {
            LOG.info("There was an error during getting CSS Locator", ex);
            return "";
        }

    }

    public String getXpathLocator(WebElement element, RemoteWebDriver webDriver) {
        LOG.info(String.format("Building XPath locator for '%s' element", element.getElementName()));

        if (!(webDriver instanceof AndroidDriver)) {
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
                if (!res.startsWith("id")) {
                    res = "//" + res;
                }
                if (el.findElements(By.xpath("./*")).isEmpty()
                        && !"".equals(el.getText())) {
                    res = res
                            + "[contains(.,'" + el.getText().replace("/", "[slh]") + "')]";
                }
                res = res.replace("[", "_");
                res = res.replace("]", "");
                res = res.replace("\"", "'");
            } catch (Exception e) {
                LOG.error("The was an error during generating xpath locator", e);
            }
            return res;
        } else {
            StringBuilder absPath = new StringBuilder();
            Elements parents = element.getElement().parents();

            for (int j = parents.size() - 3; j >= 0; j--) {
                Element el = parents.get(j);

                AtomicInteger index = new AtomicInteger(1);

                absPath.append("/");
                absPath.append(getAppiumAndroidElementTag(el));

                boolean includeIndex = false;

                for (Element sibling : el.siblingElements()) {

                    if (getAppiumAndroidElementTag(sibling).equalsIgnoreCase(getAppiumAndroidElementTag(el))) {
                        if (sibling.siblingIndex() >= el.siblingIndex()) {
                            includeIndex = true;
                        }
                        else
                        {
                            index.getAndIncrement();
                        }
                    }
                }
                if (includeIndex)
                {
                    absPath.append("[" + index + "]");
                }
            }

            AtomicInteger index = new AtomicInteger(1);
            boolean includeIndex = false;
            for (Element sibling : element.getElement().siblingElements()) {
                if (getAppiumAndroidElementTag(sibling).equalsIgnoreCase(getAppiumAndroidElementTag(element.getElement()))) {
                    if (sibling.siblingIndex() >= element.getElement().siblingIndex()) {
                        includeIndex = true;
                    }
                    else {
                        index.getAndIncrement();
                    }
                }
            }

            return absPath.append("/")
                    .append(getAppiumAndroidElementTag(element.getElement()))
                    .append(includeIndex ? "[" + index + "]" : "")
                    .toString();
        }
    }

    private String getAppiumAndroidElementTag(Element el) {
        return el.hasAttr("class") ? el.attr("class") : el.tagName();
    }

    public String getNameLocator(WebElement element) {
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

    public String getLinkTextLocator(WebElement element) {
        LOG.info(String.format("Building Link Text locator for '%s' element", element.getElementName()));
        if (!element.getElement().attr("href").equals(null) || !element.getElement().attr("href").equals(""))
            return element.getElement().text();
        else
            return "";
    }

    public String getIdLocator(WebElement element) {
        LOG.info(String.format("Building Id locator for '%s' element", element.getElementName()));
        return element.getElement().hasAttr("id") ? element.getElement().attr("id") : element.getElement().attr("resource-id");
    }

    public void generateLocatorsForNodes(WebElementNode node, final RemoteWebDriver webDriver) {
        WebElement element = (WebElement) node.getUserObject();
        String tagName = element.getElement().tagName();
        if (tagName.contains("div")
                || tagName.contains("section")
                || tagName.contains("script")
                || tagName.contains("form")
                || tagName.contains("ul")) {
            LOG.info(String.format("Skipping building locator for '%s' tag element", tagName));
        } else {
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
